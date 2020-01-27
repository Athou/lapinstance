package be.hehehe.lapinstance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.hehehe.lapinstance.model.Raid;

public interface RaidRepository extends JpaRepository<Raid, Long> {

	Raid findByDiscordMessageId(String discordMessageId);

}
