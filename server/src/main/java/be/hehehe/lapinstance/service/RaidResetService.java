package be.hehehe.lapinstance.service;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Map;

import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.model.RaidType;

@Service
public class RaidResetService {

	private final Map<RaidType, Date> resetTimes = new EnumMap<>(RaidType.class);

	public RaidResetService() {
		resetTimes.put(RaidType.ONYXIA, new GregorianCalendar(2020, Calendar.FEBRUARY, 28, 9, 0).getTime());
	}

	public Date nextReset(RaidType raidType) {
		Calendar cal = Calendar.getInstance();
		Date resetTime = resetTimes.get(raidType);
		cal.setTime(resetTime);

		while (cal.getTimeInMillis() < System.currentTimeMillis()) {
			cal.add(raidType.getResetTimer(), Calendar.DATE);
		}

		return cal.getTime();
	}

}
