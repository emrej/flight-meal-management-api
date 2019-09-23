package com.emrej.flightmeal.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@ToString
@Getter
@EqualsAndHashCode
@Entity(name = "flight")
public class FlightDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date flightDepartureDate;

    @NotNull
    @Size(min = 5, max = 6)
    private String flightNumber;

    protected FlightDao() {}

    public FlightDao(Date flightDepartureDate, String flightNumber) {
        this.flightDepartureDate = flightDepartureDate;
        this.flightNumber = flightNumber;
    }

}