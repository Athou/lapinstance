package be.hehehe.lapinstance.service.discord;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.CharacterClass;
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import be.hehehe.lapinstance.model.RaidTextChannel;

public interface DiscordService {

	void reconnect();

	void sendOrUpdateEmbed(long raidId);

	void removeEmbed(long raidId);

	void sendPrivateMessage(String userId, String message);

	Optional<String> getUserNickname(String userId);

	Set<UserRole> getUserRoles(String userId);

	List<RaidTextChannel> getRaidTextChannels();

	void addMessageReactionListener(MessageReactionListener listener);

	public interface MessageReactionListener {
		void userClickedReaction(String discordUserId, String messageId, CharacterClass characterClass);

		void userClickedReaction(String discordUserId, String messageId, RaidSubscriptionResponse response);

	}

}