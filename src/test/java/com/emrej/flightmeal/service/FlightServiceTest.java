package com.emrej.flightmeal.service;

import com.emrej.flightmeal.dao.FlightDao;
import com.emrej.flightmeal.dao.MealDao;
import com.emrej.flightmeal.exception.FlightExistsException;
import com.emrej.flightmeal.exception.FlightNotFoundException;
import com.emrej.flightmeal.model.Flight;
import com.emrej.flightmeal.model.Meals;
import com.emrej.flightmeal.repository.FlightRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.emrej.flightmeal.model.MealClass.BUSINESS;
import static com.emrej.flightmeal.model.MealClass.ECONOMY;
import static com.emrej.flightmeal.model.MealType.*;
import static org.junit.Assert.assertEquals;

/**
 * @author emre.ceylan
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FlightServiceTest {

	private Date flightDepartureDate = new Date();
	private String flightNumber = "KLM01";
	private Flight flight1 = new Flight(flightDepartureDate, flightNumber);
	private FlightDao flightDao1 = new FlightDao(flightDepartureDate, flightNumber);
	private FlightDao flightDao2 = new FlightDao(flightDepartureDate, flightNumber + "2");
	private Iterable<FlightDao> flightDaoList = new ArrayList<>(Arrays.asList(flightDao1, flightDao2));
	private Stream<FlightDao> flightDaoStream = StreamSupport.stream(flightDaoList.spliterator(), false);
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
	private Flight existingFlight = new Flight(flightDepartureDate, "EXISTS");

	@Inject
	FlightService flightService;

	@MockBean
	private FlightRepository flightRepository;

	@Before
	public void setUp() {
		Mockito.when(flightRepository.findByFlightDepartureDateAndFlightNumber(flight1.getFlightDepartureDate(), flight1.getFlightNumber()))
				.thenReturn(null);
		Mockito.when(flightRepository.save(flightDao1)).thenReturn(flightDao1);
		Mockito.when(flightRepository.findByFlightDepartureDateAndFlightNumber(existingFlight.getFlightDepartureDate(), existingFlight.getFlightNumber()))
				.thenReturn(flightDao1);
		Mockito.when(flightRepository.findAll()).thenReturn(flightDaoList);
	}
	
	@Test
	public void addFlightReturnsFlightDao() throws FlightExistsException {
		FlightDao flightDao = flightService.addFlight(flight1);

		assertEquals(flightDao, flightDao1);
	}

	@Test(expected = FlightExistsException.class)
	public void addExistingFlightThrowsException() throws FlightExistsException {
		flightService.addFlight(existingFlight);
	}

	@Test
	public void addMealsReturnsFlightDao() throws FlightNotFoundException {
		FlightDao flightDao = flightService.addMeals(existingFlight.getFlightDepartureDate(), existingFlight.getFlightNumber(), meals);

		assertEquals(flightDao, flightDao1);
	}

	@Test(expected = FlightNotFoundException.class)
	public void addMealsToUnknownFlightThrowsException() throws FlightNotFoundException {
		flightService.addMeals(flight1.getFlightDepartureDate(), flight1.getFlightNumber(), meals);
	}

	@Test
	public void getFlightReturnsFlightDao() throws FlightNotFoundException {
		FlightDao flightDao = flightService.getFlight(existingFlight.getFlightDepartureDate(), existingFlight.getFlightNumber());

		assertEquals(flightDao, flightDao1);
	}

	@Test(expected = FlightNotFoundException.class)
	public void getUnknownFlightThrowsException() throws FlightNotFoundException {
		flightService.getFlight(flight1.getFlightDepartureDate(), flight1.getFlightNumber());
	}

	@Test
	public void deleteFlightDeletesFlight() throws FlightNotFoundException {
		flightService.deleteFlight(existingFlight.getFlightDepartureDate(), existingFlight.getFlightNumber());
	}

	@Test(expected = FlightNotFoundException.class)
	public void deleteUnknownFlightThrowsException() throws FlightNotFoundException {
		flightService.deleteFlight(flight1.getFlightDepartureDate(), flight1.getFlightNumber());
	}

	@Test
	public void getAllFlightsReturnsAllFlights() {
		Stream<FlightDao> flightStream = flightService.getAllFlights();

		assertEquals(flightStream.count(), flightDaoStream.count());
	}
}
