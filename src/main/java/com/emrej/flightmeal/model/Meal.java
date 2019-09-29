package com.emrej.flightmeal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
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

    void setMealClass(String mealClass) {
        this.mealClass = mealClass;
    }

    int getNumberOfMeal(MealType mealType) {
        switch (mealType) {
            case BREAKFAST:
                return breakfast;
            case LIGHT_SNACK:
                return lightSnack;
            case LUNCH:
                return lunch;
            case DINNER:
                return dinner;
            default:
                throw new IllegalArgumentException("Unknown Meal Type!");
        }
    }

    void setNumberOfMeal(MealType mealType, int numberOfMeal) {
        switch (mealType) {
            case BREAKFAST:
                this.breakfast = numberOfMeal;
                break;
            case LIGHT_SNACK:
                this.lightSnack = numberOfMeal;
                break;
            case LUNCH:
                this.lunch = numberOfMeal;
                break;
            case DINNER:
                this.dinner = numberOfMeal;
                break;
            default:
                throw new IllegalArgumentException("Unknown Meal Type!");
        }
    }
}
