package com.kjeldsen.player.domain.exceptions;

public class IllegalAttendanceException extends IllegalArgumentException {

    public IllegalAttendanceException() {
        super("Attendance count cannot be negative!");
    }
}
