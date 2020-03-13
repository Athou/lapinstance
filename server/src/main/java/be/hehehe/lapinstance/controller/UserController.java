package be.hehehe.lapinstance.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import be.hehehe.lapinstance.SecurityConfiguration.SecurityContext;
import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.CharacterSpec;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import be.hehehe.lapinstance.repository.UserRepository;
import be.hehehe.lapinstance.service.UserCharacterService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class UserController {

	private final UserRepository userRepository;
	private final RaidSubscriptionRepository raidSubscriptionRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final UserCharacterService userCharacterService;
	private final SecurityContext securityContext;

	@GetMapping
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("{userId}")
	public User getUser(@PathVariable("userId") long userId) {
		return userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
	}

	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	@PostMapping("{userId}")
	public User saveUser(@PathVariable("userId") long userId, @RequestBody SaveUserRequest req) {
		User existing = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
		existing.setDisabled(req.isDisabled());
		return userRepository.save(existing);
	}

	@GetMapping("{userId}/characters")
	public List<UserCharacter> findAllUserCharacters(@PathVariable("userId") long userId) {
		return userCharacterRepository.findByUserId(userId);
	}

	@PostMapping("{userId}/characters")
	public UserCharacter saveUserCharacter(@PathVariable("userId") long userId, @RequestBody SaveUserCharacterRequest req) {
		if (!securityContext.isAdmin() && !securityContext.isUser(userId)) {
			throw new AccessDeniedException("");
		}

		final UserCharacter character;
		if (req.getCharacterId() == null) {
			character = new UserCharacter();
		} else {
			character = userCharacterRepository.findById(req.getCharacterId()).orElseThrow(ResourceNotFoundException::new);
		}

		character.setName(req.getName());
		character.setSpec(req.getSpec());
		character.setMain(req.isMain());
		character.setUser(userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new));

		return userCharacterService.saveUserCharacter(character);
	}

	@GetMapping("{userId}/subscriptions")
	public List<RaidSubscription> findAllSubscriptions(@PathVariable("userId") long userId) {
		return raidSubscriptionRepository.findByUserId(userId);
	}

	@Data
	public static class SaveUserRequest {
		@JsonProperty(required = true)
		private boolean disabled;
	}

	@Data
	public static class SaveUserCharacterRequest {
		private Long characterId;

		@NotBlank
		@JsonProperty(required = true)
		private String name;

		@NotNull
		@JsonProperty(required = true)
		private CharacterSpec spec;

		@NotNull
		@JsonProperty(required = true)
		private boolean main;
	}
}
