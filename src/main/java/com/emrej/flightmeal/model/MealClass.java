package com.emrej.flightmeal.model;

/**
 * The class which the meal belongs to
 */
public enum MealClass {
    ECONOMY("economyClass"),
    BUSINESS("businessClass");

    private String typeName;

    MealClass(String typeName) {
        this.typeName = typeName;
    }

    public static MealClass getByTypeName(String typeName) {
        for (MealClass mealClass : values()) {
            if (mealClass.typeName.equalsIgnoreCase(typeName)) {
                return mealClass;
            }
        }
        throw new IllegalArgumentException("Enum value not found");
    }

    public String getTypeName() {
        return typeName;
    }
}
