package be.hehehe.lapinstance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.hehehe.lapinstance.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByDiscordId(String discordId);
}
