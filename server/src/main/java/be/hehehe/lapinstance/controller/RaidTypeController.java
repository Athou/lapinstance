package be.hehehe.lapinstance.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.hehehe.lapinstance.model.RaidResetDuration;
import be.hehehe.lapinstance.service.RaidResetService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("raidTypes")
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class RaidTypeController {

	private final RaidResetService raidResetService;

	@GetMapping("{raidResetDuration}/nextReset")
	public List<Date> nextReset(@PathVariable("raidResetDuration") RaidResetDuration raidResetDuration, @RequestParam long from,
			@RequestParam long until) {
		return raidResetService.getResetTimes(raidResetDuration, new Date(from), new Date(until));
	}

}
