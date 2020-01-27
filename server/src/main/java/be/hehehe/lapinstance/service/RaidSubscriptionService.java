package be.hehehe.lapinstance.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.controller.ResourceNotFoundException;
import be.hehehe.lapinstance.model.CharacterClass;
import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import be.hehehe.lapinstance.model.RosterMember;
import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.repository.RosterMemberRepository;
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
	private final RosterMemberRepository rosterMemberRepository;
	private final DiscordService discordService;
	private final ApplicationSettingsService applicationSettingsService;
	private final URLService urlService;

	@Autowired
	public RaidSubscriptionService(UserRepository userRepository, RaidSubscriptionRepository raidSubscriptionRepository,
			RaidRepository raidRepository, UserCharacterRepository userCharacterRepository, RosterMemberRepository rosterMemberRepository,
			DiscordService discordService, ApplicationSettingsService applicationSettingsService, URLService urlService) {

		this.userRepository = userRepository;
		this.raidSubscriptionRepository = raidSubscriptionRepository;
		this.raidRepository = raidRepository;
		this.userCharacterRepository = userCharacterRepository;
		this.rosterMemberRepository = rosterMemberRepository;
		this.discordService = discordService;
		this.applicationSettingsService = applicationSettingsService;
		this.urlService = urlService;

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

				UserCharacter userCharacter = userCharacterRepository.findByUserId(user.getId())
						.stream()
						.filter(c -> c.getSpec().getCharacterClass() == characterClass)
						.findFirst()
						.orElse(null);
				if (userCharacter == null) {
					log.info("no character found class {} for user {} ({})", characterClass, user.getName(), user.getId());
					sendUnknownCharacterClassPrivateMessage(discordUserId, characterClass);
					return;
				}

				RaidSubscription sub = new RaidSubscription();
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
				sub.setRaid(raid);
				sub.setUser(user);
				sub.setResponse(response);

				log.info("registering user {} ({}) to raid {} and response {}", user.getName(), user.getId(), raid.getId(),
						sub.getResponse());
				save(sub);
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

		Raid raid = raidRepository.findById(subscription.getRaid().getId()).orElseThrow(() -> new ResourceNotFoundException());
		if (raid.getDate().before(new Date())) {
			throw new PastRaidDateException();
		}

		// check that the user is part of the roster for that raid
		if (applicationSettingsService.isRosterEnabled()) {
			Optional<RosterMember> rosterMember = rosterMemberRepository
					.findByRaidTypeAndUserCharacterId(subscription.getRaid().getRaidType(), subscription.getCharacter().getId());
			if (!rosterMember.isPresent()) {
				throw new UserCharacterNotInRosterException();
			}
		}

		// remove existing subscriptions
		User user = subscription.getUser();
		List<RaidSubscription> existingSubscriptions = raidSubscriptionRepository.findByRaidIdAndUserId(subscription.getRaid().getId(),
				user.getId());
		raidSubscriptionRepository.deleteAll(existingSubscriptions);

		// save subscription
		RaidSubscription updatedSubscription = raidSubscriptionRepository.save(subscription);

		// update discord embed
		discordService.sendOrUpdateEmbed(subscription.getRaid().getId());

		return updatedSubscription;
	}

	public List<User> findMissingSubscriptions(long raidId) {
		List<RaidSubscription> subscriptions = raidSubscriptionRepository.findByRaidId(raidId);
		List<User> users = userRepository.findAll();
		users.removeIf(u -> subscriptions.stream().anyMatch(s -> s.getUser().getId() == u.getId()));
		return users;
	}

	public void notifyMissingSubscriptions(long raidId, List<User> users) {
		List<User> actualMissingUsers = findMissingSubscriptions(raidId);
		users.removeIf(u -> actualMissingUsers.stream().noneMatch(mu -> mu.getId() == u.getId()));

		Raid raid = raidRepository.getOne(raidId);
		for (User user : users) {
			String message = String.format("Hey, tu ne t'es pas inscrit au raid %s du %s!", raid.getRaidType().getLongName(),
					raid.getFormattedDate());
			message += System.lineSeparator() + String.format("Tu peux t'inscrire depuis le channel <#%s> ou via la page <%s>",
					discordService.getTextChannelId(), urlService.getRaidUrl(raidId));
			message += System.lineSeparator() + "Si tu es absent, merci de t'indiquer comme tel.";
			discordService.sendPrivateMessage(user.getDiscordId(), message);
		}
	}

	public static class UserCharacterNotInRosterException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	public static class MissingCharacterException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	public static class PastRaidDateException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

}
