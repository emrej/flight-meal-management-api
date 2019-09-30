package com.emrej.flightmeal.model;

import com.emrej.flightmeal.dao.MealDao;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.emrej.flightmeal.model.MealType.values;

/**
 * Model class representing the 'meals list of a flight' for transferring information between FrontEnd and other APIs
 * Responsible for converting data between DAOs and integration models
 */
@ToString
@Getter
@EqualsAndHashCode
public class Meals {

    @JsonProperty("meals")
    private List<Meal> mealList;

    /**
     * Converts the object model (JSON) structure of meals into flat DB models
     * @return List<MealDao>
     */
    public List<MealDao> from() {
        List<MealDao> mealDaoList = new ArrayList<>();
        this.mealList.forEach(meal -> {
            MealClass mealClass = MealClass.getByTypeName(meal.getMealClass());
            for (MealType mealType : values()) {
                mealDaoList.add(new MealDao(mealClass, mealType, meal.getNumberOfMeal(mealType)));
            }
        });
        return mealDaoList;
    }

    /**
     * Converts the flat DB model structure of meals into object models (JSON structure)
     * @param mealDaos
     * @return List<Meal>
     */
    public static List<Meal> to(Set<MealDao> mealDaos) {
        List<Meal> mealList = new ArrayList<>();
        if (mealDaos == null) {
            return mealList;
        }

        Map<MealClass, List<MealDao>> groupByMealClassMap = mealDaos.stream().collect(Collectors.groupingBy(MealDao::getMealClass));

        groupByMealClassMap.keySet().forEach((mealClass) -> {
            List<MealDao> listMealClass = groupByMealClassMap.get(mealClass);
            Meal meal = new Meal();
            meal.setMealClass(mealClass.getTypeName());
            Map<MealType, List<MealDao>> groupByMealTypeMap = listMealClass.stream().collect(Collectors.groupingBy(MealDao::getMealType));

            groupByMealTypeMap.keySet().forEach((mealType) -> {
                List<MealDao> listMealType = groupByMealTypeMap.get(mealType);
                int numberOfMeals = listMealType.stream().mapToInt(MealDao::getNumberOfMeals).sum();
                meal.setNumberOfMeal(mealType, numberOfMeals);
            });

            mealList.add(meal);
        });

        return mealList;
    }

    Meals() {
        this.mealList = new ArrayList<>();
    }

    public Meals(List<Meal> mealList) {
        this();
        if (mealList != null) {
            this.mealList = mealList;
        }
    }
}
