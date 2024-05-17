package it.unisalento.pasproject.memberservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class Availability {
    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}