package com.emrej.flightmeal.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "meals")
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

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private Set<MealDao> meals;

    public void addMeals(List<MealDao> meals) {
        if (this.meals == null) {
            this.meals = new HashSet<>(meals);
        } else {
            this.meals.addAll(meals);
        }
        this.meals.forEach(x -> x.setFlight(this));
    }

    protected FlightDao() {}

    public FlightDao(Date flightDepartureDate, String flightNumber) {
        this.flightDepartureDate = flightDepartureDate;
        this.flightNumber = flightNumber;
    }

}