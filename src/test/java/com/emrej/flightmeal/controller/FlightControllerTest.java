package com.emrej.flightmeal.controller;

import com.emrej.flightmeal.dao.FlightDao;
import com.emrej.flightmeal.dao.MealDao;
import com.emrej.flightmeal.exception.FlightExistsException;
import com.emrej.flightmeal.exception.FlightNotFoundException;
import com.emrej.flightmeal.model.Flight;
import com.emrej.flightmeal.model.Meal;
import com.emrej.flightmeal.model.Meals;
import com.emrej.flightmeal.service.FlightService;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.emrej.flightmeal.model.MealClass.BUSINESS;
import static com.emrej.flightmeal.model.MealClass.ECONOMY;
import static com.emrej.flightmeal.model.MealType.*;
import static com.emrej.flightmeal.util.DateConverter.cleanUpTime;
import static com.emrej.flightmeal.util.DateConverter.dateToString;
import static org.junit.Assert.*;

/**
 * @author emre.ceylan
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FlightControllerTest {

	private Date flightDepartureDate = new Date();
	private String flightNumber = "KLM01";
	private Flight flight1 = new Flight(flightDepartureDate, flightNumber);
	private FlightDao flightDao1 = new FlightDao(flightDepartureDate, flightNumber);
	private FlightDao flightDao2 = new FlightDao(flightDepartureDate, flightNumber + "2");
	private List<FlightDao> flightDaoList = new ArrayList<>(Arrays.asList(flightDao1, flightDao2));
	private List<Flight> flightList = flightDaoList.stream().map(Flight::to).collect(Collectors.toList());
	private Set<MealDao> mealDaoList = new HashSet<>(Arrays.asList(
			new MealDao(ECONOMY, BREAKFAST, 10),
			new MealDao(ECONOMY, LIGHT_SNACK, 15),
			new MealDao(ECONOMY, LUNCH, 20),
			new MealDao(ECONOMY, DINNER, 12),
			new MealDao(BUSINESS, BREAKFAST, 32),
			new MealDao(BUSINESS, LIGHT_SNACK, 100),
			new MealDao(BUSINESS, LUNCH, 25),
			new MealDao(BUSINESS, DINNER, 50)

	));
	private Meals meals = new Meals(Meals.to(mealDaoList));
	private FlightDao flightDaoWithMeals = new FlightDao(flightDepartureDate, flightNumber + "3");
	private Flight flightWithMeals;

	private Flight existingFlight = new Flight(flightDepartureDate, "EXISTS");
	private Flight badRequestFlight = new Flight(flightDepartureDate, "BADREQUEST");

	@Inject
	FlightController flightController;

	@MockBean
	private FlightService flightService;

	@Before
	public void setUp() throws FlightExistsException, FlightNotFoundException {
		flightDaoWithMeals.addMeals(new ArrayList<MealDao>(mealDaoList));
		flightWithMeals = Flight.to(flightDaoWithMeals);

		Mockito.when(flightService.addFlight(flight1)).thenReturn(flightDao1);
		Mockito.when(flightService.getFlight(cleanUpTime(flightDepartureDate), flightNumber)).thenReturn(flightDao1);
		Mockito.when(flightService.getAllFlights()).thenReturn(flightDaoList.stream());
		Mockito.when(flightService.addMeals(cleanUpTime(flightDepartureDate), flightNumber, meals)).thenReturn(flightDaoWithMeals);

		Mockito.when(flightService.getFlight(cleanUpTime(flightDepartureDate), "NFOUND")).thenThrow(new FlightNotFoundException(""));
		Mockito.when(flightService.addFlight(existingFlight)).thenThrow(new FlightExistsException(""));
		Mockito.when(flightService.addFlight(badRequestFlight)).thenThrow(new ConstraintViolationException(null));
	}
	
	@Test
	public void addFlightReturnsHttpCreatedResponseWithBody() throws FlightExistsException {
		ResponseEntity responseEntity = flightController.addFlight(flight1);

		assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
		assertEquals(responseEntity.getBody(), Flight.to(flightDao1));
	}

	@Test
	public void getFlightInfoReturnsHttpOkResponseWithBody() throws ParseException, FlightNotFoundException {
		ResponseEntity responseEntity = flightController.getFlightInfo(flightNumber, dateToString(flightDepartureDate));

		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		assertEquals(responseEntity.getBody(), Flight.to(flightDao1));
	}

	@Test
	public void getAllFlightsReturnsHttpOkResponseWithBody() throws ParseException, FlightNotFoundException {
		ResponseEntity responseEntity = flightController.getAllFlights();

		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		assertEquals(responseEntity.getBody(), flightList);
	}

	@Test
	public void deleteFlightReturnsHttpNoContentResponseWithoutBody() throws ParseException, FlightNotFoundException {
		ResponseEntity responseEntity = flightController.deleteFlight(flightNumber, dateToString(flightDepartureDate));

		assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
		assertNull(responseEntity.getBody());
	}

	@Test
	public void addMealsReturnsHttpAcceptedResponseWithBody() throws ParseException, FlightNotFoundException {
		ResponseEntity responseEntity = flightController.addMeals(flightNumber, dateToString(flightDepartureDate), meals);

		assertEquals(responseEntity.getStatusCode(), HttpStatus.ACCEPTED);
		assertEquals(responseEntity.getBody(), flightWithMeals);
	}

	@Test
	public void addMealsConvertsMealsDaoToModelWhenReturningFlight() throws ParseException, FlightNotFoundException {
		ResponseEntity responseEntity = flightController.addMeals(flightNumber, dateToString(flightDepartureDate), meals);

		Flight flight = (Flight) responseEntity.getBody();
		assertNotNull(flight);

		for (Meal meal : flight.getMeals()) {
			assertTrue(meal.getMealClass().equals(ECONOMY.getTypeName()) || meal.getMealClass().equals(BUSINESS.getTypeName()));

			if (meal.getMealClass().equals(ECONOMY.getTypeName())) {
				assertEquals(meal.getBreakfast(), 10);
				assertEquals(meal.getLightSnack(), 15);
				assertEquals(meal.getLunch(), 20);
				assertEquals(meal.getDinner(), 12);
			}
			if (meal.getMealClass().equals(BUSINESS.getTypeName())) {
				assertEquals(meal.getBreakfast(), 32);
				assertEquals(meal.getLightSnack(), 100);
				assertEquals(meal.getLunch(), 25);
				assertEquals(meal.getDinner(), 50);
			}
		}

	}

	@Test(expected = FlightExistsException.class)
	public void addExistingFlightThrowsFlightExistsException() throws FlightExistsException {
		ResponseEntity responseEntity = flightController.addFlight(existingFlight);
	}

	@Test(expected = FlightNotFoundException.class)
	public void getUnknownFlightThrowsFlightNotFoundException() throws ParseException, FlightNotFoundException {
		ResponseEntity responseEntity = flightController.getFlightInfo("NFOUND", dateToString(flightDepartureDate));
	}

	@Test(expected = ConstraintViolationException.class)
	public void addBadRequestFlightThrowsConstraintViolationException() throws FlightExistsException {
		ResponseEntity responseEntity = flightController.addFlight(badRequestFlight);
	}
}
