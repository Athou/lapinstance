package be.hehehe.lapinstance.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import be.hehehe.lapinstance.model.RaidSubscription;

public interface RaidSubscriptionRepository extends JpaRepository<RaidSubscription, Long> {

	List<RaidSubscription> findByRaidId(long raidId);

	List<RaidSubscription> findByUserId(long userId);

	Optional<RaidSubscription> findByRaidIdAndUserId(long raidId, long userId);

	@Transactional
	void deleteByRaidId(Long raidId);

}
