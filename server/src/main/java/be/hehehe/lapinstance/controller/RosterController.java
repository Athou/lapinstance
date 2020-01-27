package be.hehehe.lapinstance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.RosterMember;
import be.hehehe.lapinstance.repository.RosterMemberRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("roster")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class RosterController {

	private final RosterMemberRepository rosterMemberRepository;

	@GetMapping
	public List<RosterMember> findAllRosterMembers() {
		return Lists.newArrayList(rosterMemberRepository.findAll());
	}

	@PostMapping
	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	public RosterMember addRosterMember(@RequestBody RosterMember member) {
		return rosterMemberRepository.save(member);
	}

	@DeleteMapping
	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	public void removeRosterMember(@RequestBody RosterMember member) {
		rosterMemberRepository.deleteByRaidTypeAndUserCharacterId(member.getRaidType(), member.getUserCharacter().getId());
	}

}
