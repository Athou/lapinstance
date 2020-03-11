package be.hehehe.lapinstance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import be.hehehe.lapinstance.SecurityConfiguration.SecurityContext;
import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.RosterMember;
import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.repository.RosterMemberRepository;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import be.hehehe.lapinstance.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class UserController {

	private final UserRepository userRepository;
	private final RaidSubscriptionRepository raidSubscriptionRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final RosterMemberRepository rosterMemberRepository;
	private final SecurityContext securityContext;

	@GetMapping
	public List<User> findAllUsers() {
		return Lists.newArrayList(userRepository.findAll());
	}

	@GetMapping("{userId}")
	public User getUser(@PathVariable("userId") long userId) {
		return userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
	}

	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	@PostMapping("{userId}")
	public User saveUser(@PathVariable("userId") long userId, @RequestBody User user) {
		User existing = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
		existing.setDisabled(user.isDisabled());
		return userRepository.save(existing);
	}

	@GetMapping("{userId}/characters")
	public List<UserCharacter> findAllUserCharacters(@PathVariable("userId") long userId) {
		return userCharacterRepository.findByUserId(userId);
	}

	@PostMapping("{userId}/characters")
	public UserCharacter saveUserCharacter(@PathVariable("userId") long userId, @RequestBody UserCharacter character) {
		if (!securityContext.isAdmin() && !securityContext.isUser(character.getUser().getId())) {
			throw new AccessDeniedException("");
		}

		User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
		character.setUser(user);
		return userCharacterRepository.save(character);
	}

	@GetMapping("{userId}/rosterMemberships")
	public List<RosterMember> findAllRosterMemberships(@PathVariable("userId") long userId) {
		return rosterMemberRepository.findByUserId(userId);
	}

	@GetMapping("{userId}/subscriptions")
	public List<RaidSubscription> findAllSubscriptions(@PathVariable("userId") long userId) {
		return raidSubscriptionRepository.findByUserId(userId);
	}
}
