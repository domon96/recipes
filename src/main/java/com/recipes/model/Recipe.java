package com.recipes.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Objects;

public class Recipe {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String description;
    @Size(min = 1)
    private final String[] ingredients;
    @Size(min = 1)
    private final String[] directions;

    public Recipe(String name, String description, String[] ingredients, String[] directions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String[] getDirections() {
        return directions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(name, recipe.name) && Objects.equals(description, recipe.description) && Arrays.equals(ingredients, recipe.ingredients) && Arrays.equals(directions, recipe.directions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description);
        result = 31 * result + Arrays.hashCode(ingredients);
        result = 31 * result + Arrays.hashCode(directions);
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", directions=" + Arrays.toString(directions) +
                '}';
    }
}
