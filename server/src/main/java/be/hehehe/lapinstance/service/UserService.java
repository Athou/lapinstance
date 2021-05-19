package be.hehehe.lapinstance.service;

import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.controller.ResourceNotFoundException;
import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

	public User getDefaultUser() {
		return userRepository.findById(1L).orElseThrow(ResourceNotFoundException::new);
	}

}
