package be.hehehe.lapinstance.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * One of the in-game character of a specific {@link User}.
 */
@Entity
@Table(name = "USER_CHARACTERS")
@Getter
@Setter
public class UserCharacter extends AbstractModel {

	@NotBlank
	@JsonProperty(required = true)
	private String name;

	@NotNull
	@JsonProperty(required = true)
	@Enumerated(EnumType.STRING)
	private CharacterSpec spec;

	@NotNull
	@JsonProperty(required = true)
	private boolean main;

	@NotNull
	@JsonProperty(required = true)
	@ManyToOne
	private User user;
}
