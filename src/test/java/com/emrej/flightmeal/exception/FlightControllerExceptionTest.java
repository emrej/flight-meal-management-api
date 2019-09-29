package com.emrej.flightmeal.exception;

import com.emrej.flightmeal.model.Flight;
import com.emrej.flightmeal.service.FlightService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import java.util.Date;

import static com.emrej.flightmeal.util.DateConverter.cleanUpTime;
import static com.emrej.flightmeal.util.DateConverter.dateToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author emre.ceylan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class FlightControllerExceptionTest {

    private Date flightDepartureDate = new Date();
    private String flightDepartureDateStr = dateToString(flightDepartureDate);

    private Flight existingFlight = new Flight(flightDepartureDate, "EXISTS");
    private Flight badRequestFlight = new Flight(flightDepartureDate, "BADREQUEST");

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Before
    public void setUp() throws FlightExistsException, FlightNotFoundException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        Mockito.when(flightService.getFlight(cleanUpTime(flightDepartureDate), "NFOUND")).thenThrow(new FlightNotFoundException(""));
        Mockito.when(flightService.addFlight(existingFlight)).thenThrow(new FlightExistsException(""));
        Mockito.when(flightService.addFlight(badRequestFlight)).thenThrow(new ConstraintViolationException(null));
    }

    @Test
    public void getUnknownFlightReturnsHttpNotFoundResponse() throws Exception {
        this.mockMvc.perform(
                get("/api/flight/NFOUND/" + flightDepartureDateStr + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

	@Test
	public void addExistingFlightReturnsHttpConflictResponse() throws Exception {
		this.mockMvc.perform(
				post("/api/flight/")
						.content("{\n" +
								"  \"flightNumber\": \"EXISTS\",\n" +
								"  \"flightDepartureDate\": \"" + flightDepartureDateStr + "\"\n" +
								"}")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andExpect(status().isConflict());
	}

	@Test
	public void addBadRequestFlightReturnsHttpBadRequestResponse() throws Exception {
		this.mockMvc.perform(
				post("/api/flight/")
						.content("{\n" +
								"  \"flightNumber\": \"BADREQUEST\",\n" +
								"  \"flightDepartureDate\": \"" + flightDepartureDateStr + "\"\n" +
								"}")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andExpect(status().isBadRequest());
	}
}
