package be.hehehe.lapinstance.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import be.hehehe.lapinstance.model.RaidType;
import be.hehehe.lapinstance.model.RosterMember;

public interface RosterMemberRepository extends JpaRepository<RosterMember, Long> {

	Optional<RosterMember> findByRaidTypeAndUserCharacterId(RaidType raidType, long userCharacterId);

	@Query("select rm from RosterMember rm join rm.userCharacter uc where uc.user.id = ?1")
	List<RosterMember> findByUserId(long userId);

	@Transactional
	void deleteByRaidTypeAndUserCharacterId(RaidType raidType, long userCharacterId);

}
