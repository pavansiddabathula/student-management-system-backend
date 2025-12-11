package com.techcode.studentmgmt.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;


@Component
public class TimeUtil {
	
	
	public String getTimeAgo(LocalDateTime dateTime) {

	    LocalDateTime now = LocalDateTime.now();

	    long years = ChronoUnit.YEARS.between(dateTime, now);
	    if (years > 0) return years + " year" + (years > 1 ? "s ago" : " ago");

	    long months = ChronoUnit.MONTHS.between(dateTime, now);
	    if (months > 0) return months + " month" + (months > 1 ? "s ago" : " ago");

	    long days = ChronoUnit.DAYS.between(dateTime, now);
	    if (days > 0) return days + " day" + (days > 1 ? "s ago" : " ago");

	    long hours = ChronoUnit.HOURS.between(dateTime, now);
	    if (hours > 0) return hours + " hour" + (hours > 1 ? "s ago" : " ago");

	    long minutes = ChronoUnit.MINUTES.between(dateTime, now);
	    if (minutes > 0) return minutes + " minute" + (minutes > 1 ? "s ago" : " ago");

	    return "Just now";
	}


}
