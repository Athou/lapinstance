package be.hehehe.lapinstance.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.hehehe.lapinstance.model.RaidTextChannel;
import be.hehehe.lapinstance.service.discord.DiscordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("raidTextChannels")
@RequiredArgsConstructor
public class RaidTextChannelController {

	private final DiscordService discordService;

	@GetMapping
	public List<RaidTextChannel> getAll() {
		return discordService.getRaidTextChannels();
	}

}
