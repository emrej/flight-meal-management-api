package com.emrej.flightmeal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

import static com.emrej.flightmeal.util.DateConverter.PATTERN;

@ToString
@Getter
@EqualsAndHashCode
public class Flight {

    @JsonProperty("flightDepartureDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN, timezone = "UTC")
    private Date flightDepartureDate;

    @JsonProperty("flightNumber")
    private String flightNumber;

    protected Flight() {}

    public Flight(Date flightDepartureDate, String flightNumber) {
        this.flightDepartureDate = flightDepartureDate;
        this.flightNumber = flightNumber;
    }

}