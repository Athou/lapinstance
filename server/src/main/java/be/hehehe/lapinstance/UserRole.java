package be.hehehe.lapinstance;

import lombok.experimental.UtilityClass;

public enum UserRole {
	ADMIN, USER;

	@UtilityClass
	public static class PreAuthorizeStrings {
		public static final String ADMIN = "hasAuthority('ADMIN')";
	}

}
