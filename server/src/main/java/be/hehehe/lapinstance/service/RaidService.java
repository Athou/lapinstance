package be.hehehe.lapinstance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.service.discord.DiscordService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class RaidService {

	private final RaidRepository raidRepository;
	private final RaidSubscriptionRepository raidSubscriptionRepository;
	private final DiscordService discordService;

	public Raid save(Raid raid) {
		Raid updatedRaid = raidRepository.save(raid);
		discordService.sendOrUpdateEmbed(updatedRaid.getId());
		return updatedRaid;
	}

	public void deleteById(long raidId) {
		discordService.removeEmbed(raidId);

		raidSubscriptionRepository.deleteByRaidId(raidId);
		raidRepository.deleteById(raidId);
	}
}
