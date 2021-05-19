package be.hehehe.lapinstance.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.model.Raid;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.service.discord.DiscordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
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

	@Scheduled(fixedRate = 1000 * 60 * 60)
	public void deleteExpiredDiscordMessages() {
		log.info("deleting expired discord messages...");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -6);
		Date threshold = cal.getTime();

		List<Raid> raids = raidRepository.findByExpiredDiscordMessages(threshold);
		for (Raid raid : raids) {
			log.info("deleting expired discord message for raid {}", raid.getId());
			discordService.removeEmbed(raid.getId());
		}

		log.info("done deleting expired discord messages");
	}
}
