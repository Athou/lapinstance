package be.hehehe.lapinstance.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class RaidTextChannel {

	@NotNull
	@JsonProperty(required = true)
	private String id;

	@NotNull
	@JsonProperty(required = true)
	private String name;
}
