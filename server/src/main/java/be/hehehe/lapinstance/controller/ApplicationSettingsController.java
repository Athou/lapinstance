package be.hehehe.lapinstance.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import be.hehehe.lapinstance.service.ApplicationSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RestController
@RequestMapping("applicationSettings")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class ApplicationSettingsController {

	private final ApplicationSettingsService applicationSettingsService;

	@GetMapping
	public ApplicationSettings getApplicationSettings() {
		return new ApplicationSettings(applicationSettingsService.isRosterEnabled());
	}

	@Value
	public static class ApplicationSettings {
		@NotNull
		@JsonProperty(required = true)
		private boolean roasterEnabled;
	}

}
