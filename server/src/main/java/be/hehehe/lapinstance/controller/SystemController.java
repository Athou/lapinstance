package be.hehehe.lapinstance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.service.discord.DiscordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("system")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class SystemController {

	private final DiscordService discordService;

	@PostMapping("reconnect")
	@PreAuthorize(UserRole.PreAuthorizeStrings.ADMIN)
	public void reconnect() {
		discordService.reconnect();
	}

}
