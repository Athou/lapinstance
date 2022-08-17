package be.hehehe.lapinstance.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCharacterService {

	private final UserCharacterRepository userCharacterRepository;

	public UserCharacter saveUserCharacter(UserCharacter character) {
		List<UserCharacter> userCharacters = userCharacterRepository.findByUserId(character.getUser().getId());

		// allow only one main character
		if (character.isMain()) {
			if (userCharacters.stream().anyMatch(c -> c.isMain() && c.getId() != character.getId())) {
				throw new TooManyMainCharactersException();
			}
		}

		return userCharacterRepository.save(character);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "You can have only one main character")
	public static class TooManyMainCharactersException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

}
