package com.emrej.flightmeal.model;

import com.emrej.flightmeal.dao.MealDao;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.emrej.flightmeal.model.MealType.*;

@ToString
@Getter
@EqualsAndHashCode
public class Meals {

    @JsonProperty("meals")
    private List<Meal> meals;

    public List<MealDao> from() {
        List<MealDao> mealsMapped = new ArrayList<>();
        this.meals.forEach(meal -> {
            MealClass mealClass = MealClass.getByTypeName(meal.getMealClass());
            mealsMapped.add(new MealDao(mealClass, BREAKFAST, meal.getBreakfast()));
            mealsMapped.add(new MealDao(mealClass, LIGHT_SNACK, meal.getLightSnack()));
            mealsMapped.add(new MealDao(mealClass, LUNCH, meal.getLunch()));
            mealsMapped.add(new MealDao(mealClass, DINNER, meal.getDinner()));
        });
        return mealsMapped;
    }

    protected Meals() {}

    public Meals(List<Meal> meals) {
        this.meals = meals;
    }
}
