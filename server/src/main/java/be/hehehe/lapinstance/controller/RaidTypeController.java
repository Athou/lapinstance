package be.hehehe.lapinstance.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.hehehe.lapinstance.model.RaidType;
import be.hehehe.lapinstance.service.RaidResetService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("raidTypes")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class RaidTypeController {

	private final RaidResetService raidResetService;

	@GetMapping("{raidType}/nextReset")
	public Date nextReset(@PathVariable("raidType") RaidType raidType) {
		return raidResetService.nextReset(raidType);
	}

}
