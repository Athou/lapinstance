package be.hehehe.lapinstance.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * A participation of a {@link UserCharacter} to a {@link Raid}. This means that the character is in the actual group for that particular
 * raid instance.
 */
@Entity
@Table(name = "RAID_PARTICIPANTS")
@Getter
@Setter
public class RaidParticipant extends AbstractModel {

	@NotNull
	@JsonProperty(required = true)
	@ManyToOne
	private Raid raid;

	@NotNull
	@JsonProperty(required = true)
	@ManyToOne
	private UserCharacter character;
}
