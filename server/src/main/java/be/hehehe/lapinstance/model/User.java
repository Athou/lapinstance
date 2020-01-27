package be.hehehe.lapinstance.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * A member of the guild. A {@link User} represents a physical person and thus multiple {@link UserCharacter}.
 */
@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User extends AbstractModel {

	@NotBlank
	@JsonProperty(required = true)
	private String name;

	private String discordId;

}
