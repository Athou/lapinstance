package be.hehehe.lapinstance.service.discord;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.ImmutableMap;

import be.hehehe.lapinstance.model.CharacterClass;
import be.hehehe.lapinstance.model.CharacterRole;
import be.hehehe.lapinstance.model.CharacterSpec;
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import lombok.RequiredArgsConstructor;

@Service
public class DiscordEmoteService {

	private static final Map<CharacterRole, DiscordEmoji> CHARACTER_ROLE_EMOTES = ImmutableMap.of(CharacterRole.DPS_CAC,
			new DiscordEmoji("⚔️"), CharacterRole.DPS_RANGED, new DiscordEmoji("🏹"), CharacterRole.HEAL, new DiscordEmoji("💉"));
	private static final DiscordEmote PARTICIPANTS_EMOTE = new DiscordEmoji("✅ ");
	private static final DiscordEmote MISSING_EMOTE = new DiscordEmoji("❔");

	private final BiMap<CharacterClass, String> characterClassIcons = EnumHashBiMap.create(CharacterClass.class);
	private final BiMap<CharacterSpec, String> characterSpecIcons = EnumHashBiMap.create(CharacterSpec.class);
	private final BiMap<RaidSubscriptionResponse, String> raidSubscriptionResponseIcons = EnumHashBiMap
			.create(RaidSubscriptionResponse.class);

	@Autowired
	public DiscordEmoteService(Environment env) {

		for (CharacterClass klass : CharacterClass.values()) {
			String emoteId = env.getProperty("jda.discord.icons.classes." + klass.name().toLowerCase());
			if (emoteId != null && !emoteId.isEmpty()) {
				characterClassIcons.put(klass, emoteId);
			}
		}

		for (CharacterSpec spec : CharacterSpec.values()) {
			String emoteId = env.getProperty("jda.discord.icons.specs." + spec.name().toLowerCase());
			if (emoteId != null && !emoteId.isEmpty()) {
				characterSpecIcons.put(spec, emoteId);
			}
		}

		for (RaidSubscriptionResponse response : RaidSubscriptionResponse.values()) {
			String emoteId = env.getProperty("jda.discord.icons.responses." + response.name().toLowerCase());
			if (emoteId != null && !emoteId.isEmpty()) {
				raidSubscriptionResponseIcons.put(response, emoteId);
			}
		}

	}

	public CharacterClass getCharacterClass(String emoteId) {
		return characterClassIcons.inverse().get(emoteId);
	}

	public RaidSubscriptionResponse getRaidSubscriptionResponse(String emoteId) {
		return raidSubscriptionResponseIcons.inverse().get(emoteId);
	}

	public DiscordEmote getEmote(CharacterClass characterClass) {
		String emoteId = characterClassIcons.get(characterClass);
		if (emoteId == null) {
			return MISSING_EMOTE;
		}
		return new DiscordCustomEmote(characterClass.name().toLowerCase(), emoteId);
	}

	public DiscordEmote getEmote(CharacterSpec characterSpec) {
		String emoteId = characterSpecIcons.get(characterSpec);
		if (emoteId == null) {
			return MISSING_EMOTE;
		}
		return new DiscordCustomEmote(characterSpec.name().toLowerCase(), emoteId);
	}

	public DiscordEmote getEmote(RaidSubscriptionResponse response) {
		String emoteId = raidSubscriptionResponseIcons.get(response);
		if (emoteId == null) {
			return MISSING_EMOTE;
		}
		return new DiscordCustomEmote(response.name().toLowerCase(), emoteId);
	}

	public DiscordEmote getEmote(CharacterRole role) {
		return CHARACTER_ROLE_EMOTES.get(role);
	}

	public DiscordEmote getParticipantsEmote() {
		return PARTICIPANTS_EMOTE;
	}

	public interface DiscordEmote {
		String forReaction();

		String forMessage();
	}

	@RequiredArgsConstructor
	private static class DiscordCustomEmote implements DiscordEmote {
		private final String emoteName;
		private final String emoteId;

		@Override
		public String forReaction() {
			return String.format(":%s:%s", emoteName, emoteId);
		}

		@Override
		public String forMessage() {
			return String.format("<:%s:%s>", emoteName, emoteId);
		}

	}

	@RequiredArgsConstructor
	private static class DiscordEmoji implements DiscordEmote {
		private final String emoji;

		@Override
		public String forReaction() {
			return emoji;
		}

		@Override
		public String forMessage() {
			return emoji;
		}

	}

}
