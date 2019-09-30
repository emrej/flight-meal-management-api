package com.emrej.flightmeal.dao;

import com.emrej.flightmeal.model.MealClass;
import com.emrej.flightmeal.model.MealType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * DAO class representing the DB entity of a Meal
 */
@Data
@EqualsAndHashCode(exclude = "flight")
@ToString(exclude = "flight")
@Entity(name = "meal")
public class MealDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    private FlightDao flight;

    @Enumerated(EnumType.STRING)
    private MealClass mealClass;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Min(0)
    @Max(1000)
    private int numberOfMeals;

    protected MealDao() {}

    public MealDao(MealClass mealClass, MealType mealType, int numberOfMeals) {
        this.mealClass = mealClass;
        this.mealType = mealType;
        this.numberOfMeals = numberOfMeals;
    }

}
