package com.edu.EvaluationWeb.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;

import com.edu.EvaluationWeb.entity.Test;

public class ClosestTestComparator implements Comparator<Test> {

	private static final Integer GREATER = 1;
	private static final Integer SMALLER = -1;

	@Override
	public int compare(Test left, Test right) {
		LocalDateTime leftTestStartTime = left.getStartTime();
		LocalDateTime rightTestStartTime = right.getStartTime();
		return compareDates(leftTestStartTime, rightTestStartTime);
	}

	private int compareDates(LocalDateTime left, LocalDateTime right) {
		LocalDateTime now = LocalDateTime.now();
		if(left.isBefore(now)) {
			return SMALLER;
		}
		if(right.isBefore(now)) {
			return GREATER;
		}
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		ZoneOffset zoneOffset = zonedDateTime.getOffset();
		return (int) (right.toEpochSecond(zoneOffset) - left.toEpochSecond(zoneOffset));
	}

}
