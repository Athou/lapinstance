package be.hehehe.lapinstance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class UserCharacterService {

	private final UserCharacterRepository userCharacterRepository;

	public UserCharacter saveUserCharacter(UserCharacter character) {
		if (character.isMain()) {
			List<UserCharacter> userCharacters = userCharacterRepository.findByUserId(character.getUser().getId());
			if (userCharacters.stream().anyMatch(c -> c.isMain() && c.getId() != character.getId())) {
				throw new TooManyMainCharactersException();
			}
		}

		return userCharacterRepository.save(character);
	}

	public static class TooManyMainCharactersException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

}
