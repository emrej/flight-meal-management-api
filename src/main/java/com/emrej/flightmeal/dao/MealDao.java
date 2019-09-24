package com.emrej.flightmeal.dao;

import com.emrej.flightmeal.model.MealClass;
import com.emrej.flightmeal.model.MealType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity(name = "meal")
public class MealDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
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