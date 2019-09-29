# flight-meal-management-api
To start the Spring-Boot application:
mvn spring-boot:run 

When application is up, go to:
http://localhost:8080/api/flight/
to see the list of flights.

Use Postman or start the front-end application to send requests to the 
following endpoints:

http://localhost:8080/api/flight/ | POST | Add Flight
http://localhost:8080/api/flight/{flightNumber}/{flightDepartureDate}/ | GET | Get Flight
http://localhost:8080/api/flight/{flightNumber}/{flightDepartureDate}/meals/ | POST | Add Meals
http://localhost:8080/api/flight/{flightNumber}/{flightDepartureDate}/ | DELETE | Delete Flight
http://localhost:8080/api/flight/ | GET | Get Flights


There is a Swagger UI endpoint to check the Swagger-generated API documentation:

http://localhost:8080/swagger-ui.html#/flight-controller 


Notes: All tests could not be completed, Rest Assured E2E tests are to be implemented..
