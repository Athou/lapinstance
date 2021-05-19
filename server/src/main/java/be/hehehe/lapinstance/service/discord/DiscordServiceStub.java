package be.hehehe.lapinstance.service.discord;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.RaidTextChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscordServiceStub implements DiscordService {

	@Override
	public void sendOrUpdateEmbed(long raidId) {
		log.info("called sendOrUpdateEmbed for raidId {}", raidId);
	}

	@Override
	public void removeEmbed(long raidId) {
		log.info("called removeEmbed for raidId {}", raidId);
	}

	@Override
	public void sendPrivateMessage(String userId, String message) {
		log.info("called sendPrivateMessage for userId {} and message {}", userId, message);
	}

	@Override
	public Optional<String> getUserNickname(String userId) {
		log.info("called getUserNickname for userId {}", userId);
		return Optional.empty();
	}

	@Override
	public Set<UserRole> getUserRoles(String userId) {
		log.info("called getUserRoles for userId {}", userId);
		return Collections.emptySet();
	}

	@Override
	public List<RaidTextChannel> getRaidTextChannels() {
		log.info("called getRaidTextChannels");
		return Collections.emptyList();
	}

	@Override
	public void reconnect() {
		log.info("called reconnect");
	}

	@Override
	public void addMessageReactionListener(MessageReactionListener listener) {

	}

}
