package be.hehehe.lapinstance.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import be.hehehe.lapinstance.model.Raid;

public interface RaidRepository extends JpaRepository<Raid, Long> {

	Raid findByDiscordMessageId(String discordMessageId);

	@Query("select r from Raid r where r.discordMessageId is not null and r.date < ?1")
	List<Raid> findByExpiredDiscordMessages(Date date);

}
