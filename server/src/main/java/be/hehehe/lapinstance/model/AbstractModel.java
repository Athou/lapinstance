package be.hehehe.lapinstance.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@JsonProperty(required = true)
	private long id;

}
