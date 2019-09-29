package com.emrej.flightmeal.model;

import com.emrej.flightmeal.dao.FlightDao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import static com.emrej.flightmeal.util.DateConverter.PATTERN;
import static com.emrej.flightmeal.util.DateConverter.cleanUpTime;

@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Flight {

    @JsonProperty("flightDepartureDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN, timezone = "UTC")
    private Date flightDepartureDate;

    @JsonProperty("flightNumber")
    private String flightNumber;

    @JsonProperty("meals")
    private List<Meal> meals;

    public Flight() {}

    public Flight(Date flightDepartureDate, String flightNumber) {
        this.flightDepartureDate = cleanUpTime(flightDepartureDate);
        this.flightNumber = flightNumber;
    }

    public static Flight to(FlightDao flightDao) {
        Flight flight = new Flight(flightDao.getFlightDepartureDate(), flightDao.getFlightNumber());
        flight.setMeals(Meals.to(flightDao.getMeals()));
        return flight;
    }

    public Date getFlightDepartureDate() {
        return cleanUpTime(flightDepartureDate);
    }

    public void setFlightDepartureDate(Date flightDepartureDate) {
        this.flightDepartureDate = cleanUpTime(flightDepartureDate);
    }
}
