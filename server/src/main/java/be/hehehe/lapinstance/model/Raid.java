package be.hehehe.lapinstance.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * An actual run of a {@link Raid}.
 */
@Entity
@Table(name = "RAIDS")
@Getter
@Setter
public class Raid extends AbstractModel {

	// The starting time of the raid
	@NotNull
	@JsonProperty(required = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String comment;

	@NotNull
	@JsonProperty(required = true)
	@Enumerated(EnumType.STRING)
	private RaidType raidType;

	private String discordMessageId;

	@NotNull
	@JsonProperty(required = true)
	private String discordTextChannelId;

	public final String getFormattedDate() {
		return new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", Locale.FRENCH).format(getDate());
	}

}
