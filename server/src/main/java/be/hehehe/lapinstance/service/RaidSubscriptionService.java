package be.hehehe.lapinstance.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.controller.ResourceNotFoundException;
import be.hehehe.lapinstance.model.CharacterClass;
import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import be.hehehe.lapinstance.repository.UserRepository;
import be.hehehe.lapinstance.service.discord.DiscordService;
import be.hehehe.lapinstance.service.discord.DiscordService.MessageReactionListener;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RaidSubscriptionService {

	private final UserRepository userRepository;
	private final RaidSubscriptionRepository raidSubscriptionRepository;
	private final RaidRepository raidRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final DiscordService discordService;
	private final URLService urlService;
	private final WebSocketService webSocketService;

	public RaidSubscriptionService(UserRepository userRepository, RaidSubscriptionRepository raidSubscriptionRepository,
			RaidRepository raidRepository, UserCharacterRepository userCharacterRepository, DiscordService discordService,
			URLService urlService, WebSocketService webSocketService) {

		this.userRepository = userRepository;
		this.raidSubscriptionRepository = raidSubscriptionRepository;
		this.raidRepository = raidRepository;
		this.userCharacterRepository = userCharacterRepository;
		this.discordService = discordService;
		this.urlService = urlService;
		this.webSocketService = webSocketService;

		addMessageReactionListener();
	}

	private void addMessageReactionListener() {
		this.discordService.addMessageReactionListener(new MessageReactionListener() {
			@Override
			public void userClickedReaction(String discordUserId, String messageId, CharacterClass characterClass) {
				User user = userRepository.findByDiscordId(discordUserId);
				if (user == null) {
					log.warn("user {} not found", discordUserId);
					sendUnknownUserPrivateMessage(discordUserId);
					return;
				}

				Raid raid = raidRepository.findByDiscordMessageId(messageId);
				if (raid == null) {
					log.warn("raid with discord message {} not found", messageId);
					return;
				}

				Comparator<UserCharacter> comp = Comparator.comparing(UserCharacter::isMain)
						.reversed()
						.thenComparing(Comparator.comparing(UserCharacter::getId));
				List<UserCharacter> userCharacters = userCharacterRepository.findByUserId(user.getId())
						.stream()
						.filter(c -> c.getSpec().getCharacterClass() == characterClass)
						.sorted(comp)
						.collect(Collectors.toList());
				if (userCharacters.isEmpty()) {
					log.info("no character class found {} for user {} ({})", characterClass, user.getName(), user.getId());
					sendUnknownCharacterClassPrivateMessage(discordUserId, characterClass);
					return;
				}

				UserCharacter userCharacter = getUserCharacter(user, raid, userCharacters);

				RaidSubscription sub = new RaidSubscription();
				sub.setDate(new Date());
				sub.setRaid(raid);
				sub.setUser(user);
				sub.setCharacter(userCharacter);
				sub.setResponse(RaidSubscriptionResponse.PRESENT);

				log.info("registering user {} ({}) to raid {} with character {} ({}) and response {}", user.getName(), user.getId(),
						raid.getId(), sub.getCharacter().getName(), sub.getCharacter().getId(), sub.getResponse());
				save(sub);
			}

			@Override
			public void userClickedReaction(String discordUserId, String messageId, RaidSubscriptionResponse response) {
				User user = userRepository.findByDiscordId(discordUserId);
				if (user == null) {
					log.warn("user {} not found", discordUserId);
					sendUnknownUserPrivateMessage(discordUserId);
					return;
				}

				Raid raid = raidRepository.findByDiscordMessageId(messageId);
				if (raid == null) {
					log.warn("raid with discord message {} not found", messageId);
					return;
				}

				RaidSubscription sub = new RaidSubscription();
				sub.setDate(new Date());
				sub.setRaid(raid);
				sub.setUser(user);
				sub.setResponse(response);

				log.info("registering user {} ({}) to raid {} and response {}", user.getName(), user.getId(), raid.getId(),
						sub.getResponse());
				save(sub);
			}

			/**
			 * cycle between user characters
			 */
			private UserCharacter getUserCharacter(User user, Raid raid, List<UserCharacter> userCharacters) {
				Optional<RaidSubscription> existingSubscription = raidSubscriptionRepository.findByRaidIdAndUserId(raid.getId(),
						user.getId());
				if (existingSubscription.isPresent() && existingSubscription.get().getCharacter() != null) {
					long existingCharacterId = existingSubscription.get().getCharacter().getId();
					int existingCharacterIndex = IntStream.range(0, userCharacters.size())
							.filter(i -> userCharacters.get(i).getId() == existingCharacterId)
							.findFirst()
							.orElse(0);
					int newIndex = (existingCharacterIndex + 1) % userCharacters.size();
					return userCharacters.get(newIndex);
				} else {
					return userCharacters.get(0);
				}
			}
		});
	}

	private void sendUnknownUserPrivateMessage(String discordUserId) {
		String message = String.format(
				"Il me semble que l'on ne se connait pas encore. Tu devrais te connecter sur <%s> et enregistrer tes personnages.",
				urlService.getProfileUrl());
		discordService.sendPrivateMessage(discordUserId, message);
	}

	private void sendUnknownCharacterClassPrivateMessage(String discordUserId, CharacterClass characterClass) {
		String message = String.format(
				"Je n'ai pas trouvé de personnage avec la classe %s dans ton profil, est-ce qu'il est enregistré sur <%s> ?",
				characterClass.getName(), urlService.getProfileUrl());
		discordService.sendPrivateMessage(discordUserId, message);
	}

	@Transactional
	public RaidSubscription save(RaidSubscription subscription) {
		if (subscription.getResponse() == RaidSubscriptionResponse.PRESENT && subscription.getCharacter() == null) {
			throw new MissingCharacterException();
		}

		User user = userRepository.findById(subscription.getUser().getId()).orElseThrow(ResourceNotFoundException::new);
		if (user.isDisabled()) {
			throw new UserDisabledException();
		}

		if (subscription.getCharacter() != null) {
			UserCharacter userCharacter = userCharacterRepository.findById(subscription.getCharacter().getId())
					.orElseThrow(ResourceNotFoundException::new);
			if (userCharacter.getUser().getId() != user.getId()) {
				throw new MissingCharacterException();
			}
		}

		Optional<RaidSubscription> existingSubscription = raidSubscriptionRepository.findByRaidIdAndUserId(subscription.getRaid().getId(),
				user.getId());
		if (existingSubscription.isPresent()) {
			subscription.setDate(existingSubscription.get().getDate());
			raidSubscriptionRepository.delete(existingSubscription.get());
			raidSubscriptionRepository.flush();
		}

		// save subscription
		RaidSubscription updatedSubscription = raidSubscriptionRepository.save(subscription);

		// update discord embed
		discordService.sendOrUpdateEmbed(subscription.getRaid().getId());

		// notify clients
		webSocketService.notifyRaidSubscriptionChanged(updatedSubscription);

		return updatedSubscription;
	}

	public List<User> findMissingSubscriptions(long raidId) {
		List<User> users = userRepository.findAll();

		// remove disabled users
		users.removeIf(User::isDisabled);

		// remove users having no characters
		Map<Long, List<UserCharacter>> characters = userCharacterRepository.findAll()
				.stream()
				.collect(Collectors.groupingBy(c -> c.getUser().getId()));
		users.removeIf(u -> characters.getOrDefault(u.getId(), Collections.emptyList()).isEmpty());

		// remove users already subscribed
		List<RaidSubscription> subscriptions = raidSubscriptionRepository.findByRaidId(raidId);
		users.removeIf(u -> subscriptions.stream().anyMatch(s -> s.getUser().getId() == u.getId()));

		return users;
	}

	public void notifyMissingSubscriptions(long raidId, List<Long> userIds) {
		List<User> notifyUsers = userRepository.findAllById(userIds);
		List<User> allMissingUsers = findMissingSubscriptions(raidId);
		notifyUsers.removeIf(u -> allMissingUsers.stream().noneMatch(mu -> mu.getId() == u.getId()));

		Raid raid = raidRepository.findById(raidId).orElseThrow(ResourceNotFoundException::new);
		for (User user : notifyUsers) {
			String message = String.format("Hey, tu ne t'es pas inscrit au raid %s du %s!", raid.getRaidType().getName(),
					raid.getFormattedDate());
			message += System.lineSeparator() + String.format("Tu peux t'inscrire depuis le channel <#%s> ou via la page <%s>",
					raid.getDiscordTextChannelId(), urlService.getRaidUrl(raidId));
			message += System.lineSeparator() + "Si tu es absent, merci de t'indiquer comme tel.";

			log.info("notifying user {} ({}) of missing subscription to raid {}", user.getName(), user.getId(), raidId);
			discordService.sendPrivateMessage(user.getDiscordId(), message);
		}
	}

	public static class UserDisabledException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	public static class MissingCharacterException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

}
