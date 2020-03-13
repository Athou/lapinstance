package be.hehehe.lapinstance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.hehehe.lapinstance.model.UserCharacter;
import be.hehehe.lapinstance.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("userCharacters")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class UserCharacterController {

	private final UserCharacterRepository userCharacterRepository;

	@GetMapping
	public List<UserCharacter> findAllUserCharacters() {
		return userCharacterRepository.findAll();
	}

}
