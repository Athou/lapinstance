package be.hehehe.lapinstance;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import be.hehehe.lapinstance.model.User;
import be.hehehe.lapinstance.repository.RaidRepository;
import be.hehehe.lapinstance.repository.RaidSubscriptionRepository;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import be.hehehe.lapinstance.service.URLService;
import be.hehehe.lapinstance.service.UserService;
import be.hehehe.lapinstance.service.discord.DiscordEmbedService;
import be.hehehe.lapinstance.service.discord.DiscordEmoteService;
import be.hehehe.lapinstance.service.discord.DiscordService;
import be.hehehe.lapinstance.service.discord.DiscordServiceImpl;
import be.hehehe.lapinstance.service.discord.DiscordServiceStub;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String DISCORD_BOT_USER_AGENT = "Lapinstance (https://github.com/Athou/lapinstance)";
	private static final String ATTRIBUTE_KEY_USER_ID = "user_id";

	private final UserService userService;
	private final DiscordService discordService;
	private final boolean discordIntegrationEnabled;

	@Autowired
	public SecurityConfiguration(UserService userService, DiscordService discordService, Environment env) {
		this.userService = userService;
		this.discordService = discordService;
		this.discordIntegrationEnabled = !Strings.isNullOrEmpty(env.getProperty("jda.discord.token"));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.logout().logoutSuccessUrl("/");

		if (discordIntegrationEnabled) {
			http.authorizeRequests()
					.anyRequest()
					.hasAuthority(UserRole.USER.name())
					.and()
					.oauth2Login()
					.tokenEndpoint()
					.accessTokenResponseClient(accessTokenResponseClient())
					.and()
					.userInfoEndpoint()
					.userService(userService());
		}

	}

	private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
		DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();

		// discord requires a custom user agent
		client.setRequestEntityConverter(new OAuth2AuthorizationCodeGrantRequestEntityConverter() {
			@Override
			public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest oauth2Request) {
				return withUserAgent(super.convert(oauth2Request));
			}
		});

		return client;
	}

	private OAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
		DefaultOAuth2UserService service = new DefaultOAuth2UserService() {
			@Override
			public OAuth2User loadUser(OAuth2UserRequest userRequest) {
				OAuth2User oAuth2User = super.loadUser(userRequest);

				String discordId = oAuth2User.getAttribute("id").toString();
				String userName = discordService.getUserNickname(discordId)
						.orElseThrow(
								() -> new RuntimeException(String.format("unauthorized user: %s (%s)", oAuth2User.getName(), discordId)));

				User user = userService.saveOrUpdate(discordId, userName);
				Set<UserRole> roles = discordService.getUserRoles(discordId);

				if (user.isDisabled() && !roles.contains(UserRole.ADMIN)) {
					throw new RuntimeException("user is disabled: " + oAuth2User.getName());
				}

				Set<GrantedAuthority> authorities = roles.stream()
						.map(r -> new SimpleGrantedAuthority(r.name()))
						.collect(Collectors.toSet());

				Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
				attributes.put(ATTRIBUTE_KEY_USER_ID, user.getId());

				log.info("user {} ({}) logged in with roles {}", userName, discordId, roles);
				return new SimpleOAuth2User(userName, authorities, attributes);
			}
		};

		// discord requires a custom user agent
		service.setRequestEntityConverter(new OAuth2UserRequestEntityConverter() {
			@Override
			public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
				return withUserAgent(super.convert(userRequest));
			}
		});

		return service;
	}

	private static RequestEntity<?> withUserAgent(RequestEntity<?> request) {
		HttpHeaders headers = new HttpHeaders();
		headers.putAll(request.getHeaders());
		headers.add(HttpHeaders.USER_AGENT, DISCORD_BOT_USER_AGENT);

		return new RequestEntity<>(request.getBody(), headers, request.getMethod(), request.getUrl());
	}

	@Bean
	@SessionScope
	public SecurityContext securityContext() {
		if (!discordIntegrationEnabled) {
			User user = userService.getDefaultUser();
			return new SecurityContext(user.getId(), user.getName(), Sets.newHashSet(UserRole.ADMIN, UserRole.USER));
		}

		OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		OAuth2User oAuth2User = authentication.getPrincipal();
		Long userId = oAuth2User.getAttribute(ATTRIBUTE_KEY_USER_ID);
		String userName = oAuth2User.getName();
		Set<UserRole> roles = oAuth2User.getAuthorities().stream().map(a -> UserRole.valueOf(a.getAuthority())).collect(Collectors.toSet());
		return new SecurityContext(userId, userName, roles);
	}

	@Bean
	@ApplicationScope
	public DiscordService discordService(RaidRepository raidRepository, RaidSubscriptionRepository raidSubscriptionRepository,
			UserCharacterRepository userCharacterRepository, URLService urlService, DiscordEmbedService discordEmbedService,
			DiscordEmoteService discordEmoteService, Environment env) {
		if (!discordIntegrationEnabled) {
			return new DiscordServiceStub();
		}

		return new DiscordServiceImpl(raidRepository, raidSubscriptionRepository, userCharacterRepository, urlService, discordEmbedService,
				discordEmoteService, env);
	}

	@Getter
	@RequiredArgsConstructor
	public static class SecurityContext implements Serializable {
		private static final long serialVersionUID = 1L;

		private final long userId;
		private final String userName;
		private final Set<UserRole> roles;

		public boolean isAdmin() {
			return roles.contains(UserRole.ADMIN);
		}

		public boolean isUser(long userId) {
			return this.userId == userId;
		}
	}

	@RequiredArgsConstructor
	@Getter
	private static final class SimpleOAuth2User implements OAuth2User, Serializable {
		private static final long serialVersionUID = 1L;

		private final String name;
		private final Collection<? extends GrantedAuthority> authorities;
		private final Map<String, Object> attributes;
	}

}