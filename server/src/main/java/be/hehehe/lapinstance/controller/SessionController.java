package be.hehehe.lapinstance.controller;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import be.hehehe.lapinstance.SecurityConfiguration.SecurityContext;
import be.hehehe.lapinstance.UserRole;
import be.hehehe.lapinstance.model.User;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RestController
@RequestMapping("session")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class SessionController {

	private final SecurityContext securityContext;

	@GetMapping("user")
	public Session getCurrentUser() {
		User user = new User();
		user.setId(securityContext.getUserId());
		user.setName(securityContext.getUserName());
		return new Session(user, securityContext.getRoles());
	}

	@Value
	public static class Session {

		@NotNull
		@JsonProperty(required = true)
		private User user;

		@NotNull
		@JsonProperty(required = true)
		private Set<UserRole> roles;

	}

}
