package com.emrej.flightmeal.repository;

import com.emrej.flightmeal.dao.FlightDao;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends CrudRepository<FlightDao, Long> {

    List<FlightDao> findByFlightNumber(String flightNumber);

    FlightDao findById(long id);

    FlightDao findByFlightDepartureDateAndFlightNumber(Date flightDepartureDate, String flightNumber);

}
