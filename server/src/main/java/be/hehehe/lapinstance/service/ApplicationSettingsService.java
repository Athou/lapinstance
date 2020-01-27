package be.hehehe.lapinstance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSettingsService {

	@Value("${settings.roster.enabled}")
	private boolean rosterEnabled;

	public boolean isRosterEnabled() {
		return rosterEnabled;
	}

}
