package be.hehehe.lapinstance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class UserService {

	private final UserRepository userRepository;

	public User saveOrUpdate(String discordId, String name) {
		User user = userRepository.findByDiscordId(discordId);
		if (user == null) {
			user = new User();
			user.setDiscordId(discordId);
		}
		user.setName(name);

		return userRepository.save(user);
	}

}
