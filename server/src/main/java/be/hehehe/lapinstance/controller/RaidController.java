package be.hehehe.lapinstance.controller;

import java.util.List;

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

import com.google.common.collect.Lists;

import be.hehehe.lapinstance.SecurityConfiguration.SecurityContext;
import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.model.RaidSubscription;
import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.service.RaidService;
import be.hehehe.lapinstance.service.RaidSubscriptionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("raids")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class RaidController {

	private final RaidRepository raidRepository;
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
	public Raid saveRaid(@RequestBody Raid raid) {
		if (raid.getId() == 0) {
			return raidService.save(raid);
		} else {
			Raid existing = raidRepository.findById(raid.getId()).orElseThrow(ResourceNotFoundException::new);
			existing.setComment(raid.getComment());
			existing.setDate(raid.getDate());
			existing.setRaidType(raid.getRaidType());
			return raidService.save(existing);
		}
	}

	@DeleteMapping("{id}")
	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	public void deleteRaid(@PathVariable("id") long id) {
		raidService.deleteById(id);
	}

	@GetMapping
	public List<Raid> findAllRaids() {
		return Lists.newArrayList(raidRepository.findAll());
	}

	@GetMapping("{raidId}/subscriptions")
	public List<RaidSubscription> findRaidSubscriptions(@PathVariable("raidId") long raidId) {
		return raidSubscriptionRepository.findByRaidId(raidId);
	}

	@PostMapping("{raidId}/subscriptions")
	public RaidSubscription saveRaidSubscription(@PathVariable("raidId") long raidId, @RequestBody RaidSubscription raidSubscription) {
		if (!securityContext.isAdmin() && !securityContext.isUser(raidSubscription.getUser().getId())) {
			throw new AccessDeniedException("");
		}

		Raid raid = raidRepository.findById(raidId).orElseThrow(ResourceNotFoundException::new);
		raidSubscription.setRaid(raid);

		return raidSubscriptionService.save(raidSubscription);
	}

	@GetMapping("{raidId}/missingSubscriptions")
	public List<User> findMissingRaidSubscriptions(@PathVariable("raidId") long raidId) {
		return raidSubscriptionService.findMissingSubscriptions(raidId);
	}

	@PostMapping("{raidId}/missingSubscriptions/notify")
	public void notifyMissingRaidSubscriptions(@PathVariable("raidId") long raidId, @RequestBody List<User> users) {
		raidSubscriptionService.notifyMissingSubscriptions(raidId, users);
	}

}
