package be.hehehe.lapinstance.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import be.hehehe.lapinstance.model.RaidType;

@Service
public class RaidResetService {

	private final Map<RaidType, Date> resetTimes = new EnumMap<>(RaidType.class);

	public RaidResetService() {
		resetTimes.put(RaidType.ONYXIA, new GregorianCalendar(2020, Calendar.FEBRUARY, 28, 9, 0).getTime());
		resetTimes.put(RaidType.ZUL_GURUB, new GregorianCalendar(2020, Calendar.APRIL, 16, 9, 0).getTime());
	}

	public List<Date> getResetTimes(RaidType raidType, Date from, Date until) {
		Preconditions.checkArgument(from.before(until));

		Date raidResetTime = resetTimes.get(raidType);
		if (raidResetTime.after(until)) {
			return Collections.emptyList();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(raidResetTime);

		List<Date> resets = new ArrayList<>();
		while (cal.getTime().before(until)) {
			if (cal.getTime().after(from)) {
				resets.add(cal.getTime());
			}
			cal.add(raidType.getResetTimer(), Calendar.DATE);
		}

		return resets;
	}

}
