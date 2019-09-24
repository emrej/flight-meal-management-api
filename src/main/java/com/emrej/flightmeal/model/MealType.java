package com.emrej.flightmeal.model;

public enum MealType {
    BREAKFAST("breakfast"),
    LIGHT_SNACK("lightSnack"),
    LUNCH("lunch"),
    DINNER("dinner");

    private String typeName;

    MealType(String typeName) {
        this.typeName = typeName;
    }

    public static MealType getByTypeName(String typeName) {
        for (MealType mealType : values()) {
            if (mealType.typeName.equalsIgnoreCase(typeName)) {
                return mealType;
            }
        }
        throw new IllegalArgumentException("Enum value not found");
    }
}
