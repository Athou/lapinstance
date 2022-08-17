package be.hehehe.lapinstance.service.discord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.security.auth.login.LoginException;

import org.springframework.core.env.Environment;

import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.controller.ResourceNotFoundException;
import be.hehehe.lapinstance.model.CharacterClass;
import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import be.hehehe.lapinstance.model.RaidTextChannel;
import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import be.hehehe.lapinstance.service.URLService;
import be.hehehe.lapinstance.service.discord.DiscordEmbedService.SubscriptionModel;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

@Slf4j
public class DiscordServiceImpl implements DiscordService {

	private final RaidRepository raidRepository;
	private final RaidSubscriptionRepository raidSubscriptionRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final URLService urlService;
	private final DiscordEmbedService discordEmbedService;
	private final DiscordEmoteService discordEmoteService;

	private final String[] userRoles;
	private final String[] adminRoles;
	private final String token;
	private final String guildId;
	private final String[] raidTextChannelIds;

	private JDA jda;
	private Guild guild;
	private List<RaidTextChannel> raidTextChannels;

	private final List<MessageReactionListener> messageReactionListeners = new ArrayList<>();

	public DiscordServiceImpl(RaidRepository raidRepository, RaidSubscriptionRepository raidSubscriptionRepository,
			UserCharacterRepository userCharacterRepository, URLService urlService, DiscordEmbedService discordEmbedService,
			DiscordEmoteService discordEmoteService, Environment env) {

		this.raidRepository = raidRepository;
		this.raidSubscriptionRepository = raidSubscriptionRepository;
		this.userCharacterRepository = userCharacterRepository;
		this.urlService = urlService;
		this.discordEmbedService = discordEmbedService;
		this.discordEmoteService = discordEmoteService;

		this.userRoles = env.getProperty("jda.discord.user-roles").split(",");
		this.adminRoles = env.getProperty("jda.discord.admin-roles").split(",");
		this.token = env.getProperty("jda.discord.token");
		this.guildId = env.getProperty("jda.discord.guild-id");
		this.raidTextChannelIds = env.getProperty("jda.discord.raid-text-channel-ids").split(",");

		connect();
	}

	private void connect() {
		log.info("connecting to Discord");
		if (token != null && !token.isEmpty()) {
			try {
				this.jda = JDABuilder.createDefault(token)
						.setChunkingFilter(ChunkingFilter.ALL)
						.setMemberCachePolicy(MemberCachePolicy.ALL)
						.enableIntents(GatewayIntent.GUILD_MEMBERS)
						.build()
						.awaitReady();
				this.guild = jda.getGuildById(guildId);
				this.raidTextChannels = Stream.of(raidTextChannelIds)
						.map(id -> new RaidTextChannel(id, guild.getTextChannelById(id).getName()))
						.collect(Collectors.toList());

			} catch (LoginException | InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("cannot create jda instance", e);
			}

			this.jda.addEventListener(new ListenerAdapter() {
				@Override
				public void onMessageReactionAdd(MessageReactionAddEvent event) {
					User user = event.getUser();
					if (user.isBot()) {
						return;
					}

					MessageChannel channel = event.getChannel();
					if (raidTextChannels.stream().noneMatch(rtc -> rtc.getId().equals(channel.getId()))) {
						return;
					}

					Message message = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
					if (!message.getAuthor().getId().equals(jda.getSelfUser().getId())) {
						return;
					}

					ReactionEmote reactionEmote = event.getReactionEmote();
					if (reactionEmote.isEmote()) {
						String emoteId = reactionEmote.getEmote().getId();
						CharacterClass klass = discordEmoteService.getCharacterClass(emoteId);
						RaidSubscriptionResponse raidSubscriptionResponse = discordEmoteService.getRaidSubscriptionResponse(emoteId);
						if (klass != null) {
							messageReactionListeners.forEach(l -> l.userClickedReaction(user.getId(), event.getMessageId(), klass));
						} else if (raidSubscriptionResponse != null) {
							messageReactionListeners
									.forEach(l -> l.userClickedReaction(user.getId(), event.getMessageId(), raidSubscriptionResponse));
						}
					}

					event.getReaction().removeReaction(user).complete();
				}
			});
		} else {
			log.info("received no token, not connecting to discord");
		}
	}

	private void disconnect() {
		log.info("disconnecting from Discord");
		jda.shutdownNow();
	}

	@Override
	public void reconnect() {
		log.info("reconnecting to Discord");
		disconnect();
		connect();
	}

	@Override
	public List<RaidTextChannel> getRaidTextChannels() {
		return raidTextChannels;
	}

	@Override
	public void sendOrUpdateEmbed(long raidId) {
		Raid raid = getRaid(raidId);
		List<RaidSubscription> subscriptions = raidSubscriptionRepository.findByRaidId(raidId);
		TextChannel textChannel = guild.getTextChannelById(raid.getDiscordTextChannelId());
		if (textChannel == null) {
			String defaultTextChannelId = this.raidTextChannels.get(0).getId();
			textChannel = guild.getTextChannelById(defaultTextChannelId);
			log.warn("text channel {} does not exist, using default text channel {}", raid.getDiscordTextChannelId(), defaultTextChannelId);
		}

		Map<Long, UserCharacter> mainCharactersByUserId = userCharacterRepository.findAll()
				.stream()
				.filter(UserCharacter::isMain)
				.collect(Collectors.toMap(c -> c.getUser().getId(), c -> c));
		List<SubscriptionModel> subscriptionModels = buildSubscriptionModels(subscriptions, mainCharactersByUserId);
		MessageEmbed embed = discordEmbedService.buildEmbed(raid, subscriptionModels, urlService.getRaidUrl(raid.getId()));

		Message message = null;
		if (raid.getDiscordMessageId() != null) {
			try {
				message = textChannel.retrieveMessageById(raid.getDiscordMessageId()).complete();
			} catch (ErrorResponseException e) {
				if (e.getErrorResponse() == ErrorResponse.UNKNOWN_MESSAGE) {
					if (raid.getDate().before(new Date())) {
						log.info("message {} not found for raid {} in the past, skipping", raid.getDiscordMessageId(), raid.getId());
						return;
					} else {
						log.warn("message {} not found for raid {}", raid.getDiscordMessageId(), raid.getId());
					}
				} else {
					throw e;
				}
			}
		}

		if (message == null) {
			message = textChannel.sendMessageEmbeds(embed).complete();
			for (CharacterClass klass : CharacterClass.values()) {
				String emote = discordEmoteService.getEmote(klass).forReaction();
				message.addReaction(emote).complete();
			}

			for (RaidSubscriptionResponse response : getRaidSubscriptionResponseWithReactions()) {
				String emote = discordEmoteService.getEmote(response).forReaction();
				message.addReaction(emote).complete();
			}

			raid.setDiscordMessageId(message.getId());
			raidRepository.save(raid);
		} else {
			message.editMessageEmbeds(embed).complete();
		}
	}

	private List<SubscriptionModel> buildSubscriptionModels(List<RaidSubscription> subscriptions,
			Map<Long, UserCharacter> mainCharactersByUserId) {
		return subscriptions.stream().map(sub -> {
			final String name;
			UserCharacter mainCharacter = mainCharactersByUserId.get(sub.getUser().getId());
			if (sub.getCharacter() == null) {
				if (mainCharacter == null) {
					name = sub.getUser().getName();
				} else {
					name = mainCharacter.getName();
				}
			} else {
				if (mainCharacter == null) {
					name = sub.getCharacter().getName();
				} else {
					if (sub.getCharacter().isMain()) {
						name = sub.getCharacter().getName();
					} else {
						name = sub.getCharacter().getName() + " (" + mainCharacter.getName() + ")";
					}
				}
			}
			return new SubscriptionModel(name, sub.getResponse(), sub.getCharacter() == null ? null : sub.getCharacter().getSpec());
		}).collect(Collectors.toList());
	}

	@Override
	public void removeEmbed(long raidId) {
		Raid raid = getRaid(raidId);
		if (raid.getDiscordMessageId() != null) {
			try {
				TextChannel textChannel = guild.getTextChannelById(raid.getDiscordTextChannelId());
				textChannel.deleteMessageById(raid.getDiscordMessageId()).complete();
			} catch (Exception e) {
				log.warn("could not remove message", e);
			}

			raid.setDiscordMessageId(null);
			raidRepository.save(raid);
		}
	}

	@Override
	public void sendPrivateMessage(String userId, String message) {
		jda.getUserById(userId).openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
	}

	@Override
	public Optional<String> getUserNickname(String userId) {
		Member member = guild.getMemberById(userId);
		return Optional.ofNullable(member).map(Member::getEffectiveName);
	}

	@Override
	public Set<UserRole> getUserRoles(String userId) {
		Member member = guild.getMemberById(userId);

		Set<UserRole> set = new HashSet<>();
		List<String> roles = member.getRoles().stream().map(Role::getName).collect(Collectors.toList());

		log.info("user {} ({}) has discord roles {}", member.getEffectiveName(), userId, roles);

		if (Stream.of(userRoles).anyMatch(roles::contains)) {
			set.add(UserRole.USER);
		}

		if (Stream.of(adminRoles).anyMatch(roles::contains)) {
			set.add(UserRole.USER);
			set.add(UserRole.ADMIN);
		}

		return set;
	}

	private Raid getRaid(long raidId) {
		return raidRepository.findById(raidId).orElseThrow(ResourceNotFoundException::new);
	}

	private List<RaidSubscriptionResponse> getRaidSubscriptionResponseWithReactions() {
		return Stream.of(RaidSubscriptionResponse.values()).filter(r -> r != RaidSubscriptionResponse.PRESENT).collect(Collectors.toList());
	}

	@Override
	public void addMessageReactionListener(MessageReactionListener listener) {
		this.messageReactionListeners.add(listener);
	}

}
