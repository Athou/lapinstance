package be.hehehe.lapinstance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.hehehe.lapinstance.model.UserCharacter;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {

	List<UserCharacter> findByUserId(long userId);

}
