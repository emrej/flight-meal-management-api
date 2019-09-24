package com.emrej.flightmeal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class Meal {

    @JsonProperty("mealClass")
    private String mealClass;

    @JsonProperty("breakfast")
    private int breakfast;

    @JsonProperty("lightSnack")
    private int lightSnack;

    @JsonProperty("lunch")
    private int lunch;

    @JsonProperty("dinner")
    private int dinner;

    protected Meal() {}

}