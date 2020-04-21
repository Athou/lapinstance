package be.hehehe.lapinstance.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import be.hehehe.lapinstance.SecurityConfiguration.SecurityContext;
import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.RaidSubscriptionResponse;
import be.hehehe.lapinstance.model.RaidType;
import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import be.hehehe.lapinstance.repository.UserRepository;
import be.hehehe.lapinstance.service.RaidService;
import be.hehehe.lapinstance.service.RaidSubscriptionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("raids")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class RaidController {

	private final RaidRepository raidRepository;
	private final UserRepository userRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final RaidService raidService;
	private final RaidSubscriptionRepository raidSubscriptionRepository;
	private final RaidSubscriptionService raidSubscriptionService;
	private final SecurityContext securityContext;

	@GetMapping("{id}")
	public Raid getRaid(@PathVariable("id") long id) {
		return raidRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	@PostMapping
	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	public Raid saveRaid(@RequestBody SaveRaidRequest req) {
		final Raid raid;
		if (req.getRaidId() == null) {
			raid = new Raid();
		} else {
			raid = raidRepository.findById(req.getRaidId()).orElseThrow(ResourceNotFoundException::new);
		}

		raid.setComment(req.getComment());
		raid.setDate(req.getDate());
		raid.setRaidType(req.getRaidType());
		raid.setDiscordTextChannelId(req.getRaidTextChannelId());
		return raidService.save(raid);
	}

	@DeleteMapping("{id}")
	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	public void deleteRaid(@PathVariable("id") long id) {
		raidService.deleteById(id);
	}

	@GetMapping
	public List<Raid> findAllRaids() {
		return raidRepository.findAll();
	}

	@GetMapping("{raidId}/subscriptions")
	public List<RaidSubscription> findRaidSubscriptions(@PathVariable("raidId") long raidId) {
		return raidSubscriptionRepository.findByRaidId(raidId);
	}

	@PostMapping("{raidId}/subscriptions")
	public RaidSubscription saveRaidSubscription(@PathVariable("raidId") long raidId, @RequestBody SaveRaidSubscriptionRequest req) {
		if (!securityContext.isAdmin() && !securityContext.isUser(req.getUserId())) {
			throw new AccessDeniedException("");
		}

		RaidSubscription sub = new RaidSubscription();
		sub.setRaid(raidRepository.findById(raidId).orElseThrow(ResourceNotFoundException::new));
		sub.setUser(userRepository.findById(req.getUserId()).orElseThrow(ResourceNotFoundException::new));
		sub.setResponse(req.getResponse());

		if (req.getCharacterId() != null) {
			sub.setCharacter(userCharacterRepository.findById(req.getCharacterId()).orElseThrow(ResourceNotFoundException::new));
		}

		return raidSubscriptionService.save(sub);
	}

	@GetMapping("{raidId}/missingSubscriptions")
	public List<User> findMissingRaidSubscriptions(@PathVariable("raidId") long raidId) {
		return raidSubscriptionService.findMissingSubscriptions(raidId);
	}

	@PostMapping("{raidId}/missingSubscriptions/notify")
	public void notifyMissingRaidSubscriptions(@PathVariable("raidId") long raidId, @RequestBody List<Long> userIds) {
		raidSubscriptionService.notifyMissingSubscriptions(raidId, userIds);
	}

	@Data
	public static class SaveRaidRequest {

		private Long raidId;

		private String comment;

		@NotNull
		@JsonProperty(required = true)
		private Date date;

		@NotNull
		@JsonProperty(required = true)
		private RaidType raidType;

		@NotNull
		@JsonProperty(required = true)
		private String raidTextChannelId;
	}

	@Data
	public static class SaveRaidSubscriptionRequest {

		@NotNull
		@JsonProperty(required = true)
		private RaidSubscriptionResponse response;

		@NotNull
		@JsonProperty(required = true)
		private long userId;

		private Long characterId;

	}

}
