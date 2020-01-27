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
 * A potential participant for a {@link Raid}. This means that this {@link UserCharacter} is eligible to provide a {@link RaidSubscription}
 * for each {@link Raid} of the given {@link RaidType}.
 */
@Entity
@Table(name = "ROSTER_MEMBERS")
@Getter
@Setter
public class RosterMember extends AbstractModel {

	@NotNull
	@JsonProperty(required = true)
	@Enumerated(EnumType.STRING)
	private RaidType raidType;

	@NotNull
	@JsonProperty(required = true)
	@ManyToOne
	private UserCharacter userCharacter;
}
