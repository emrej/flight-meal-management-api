package com.emrej.flightmeal.service;

import com.emrej.flightmeal.dao.FlightDao;
import com.emrej.flightmeal.exception.FlightExistsException;
import com.emrej.flightmeal.model.Flight;
import com.emrej.flightmeal.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    private FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public FlightDao addFlight(Flight newFlight) throws FlightExistsException {
        LOGGER.info("Add flight called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(newFlight.getFlightDepartureDate(), newFlight.getFlightNumber());
        if (null == flight) {
            flight = flightRepository.save(new FlightDao(newFlight.getFlightDepartureDate(), newFlight.getFlightNumber()));
            LOGGER.info("Flight saved");
            return flight;
        } else {
            LOGGER.warn("Flight for this date already exists.");
            throw new FlightExistsException("Flight for this date already exists.");
        }
    }
}
