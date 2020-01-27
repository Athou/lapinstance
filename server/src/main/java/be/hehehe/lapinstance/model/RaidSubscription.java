package be.hehehe.lapinstance.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * A subscription to a {@link Raid}. This is the RSVP of a {@link UserCharacter} to a specific {@link Raid}.
 */
@Entity
@Table(name = "RAID_SUBSCRIPTIONS")
@Getter
@Setter
public class RaidSubscription extends AbstractModel {

	@NotNull
	@JsonProperty(required = true)
	@ManyToOne
	private Raid raid;

	@NotNull
	@JsonProperty(required = true)
	@Enumerated(EnumType.STRING)
	private RaidSubscriptionResponse response;

	@ManyToOne
	private UserCharacter character;

	@NotNull
	@JsonProperty(required = true)
	@ManyToOne
	private User user;

}
