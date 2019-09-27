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

@ToString
@Getter
@EqualsAndHashCode
public class Meals {

    @JsonProperty("meals")
    private List<Meal> mealList;

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
}
