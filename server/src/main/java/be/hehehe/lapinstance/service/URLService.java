package be.hehehe.lapinstance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class URLService {

	private final String baseUrl;

	@Autowired
	public URLService(@Value("${server.base-url}") String baseUrl) {
		this.baseUrl = baseUrl.endsWith("/") ? baseUrl : (baseUrl + "/");
	}

	public String getProfileUrl() {
		return baseUrl + "#/profile";
	}

	public String getRaidUrl(long raidId) {
		return baseUrl + "#/raid/show/" + raidId;
	}

}
