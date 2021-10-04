package com.recipes.controller;

import com.recipes.model.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecipeController {
    private static int recipeId = 0;
    private final List<Recipe> recipes;

    public RecipeController(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable int id) {
        try {
            return recipes.get(--id);
        } catch (IndexOutOfBoundsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No recipe with id: %s", ++id));
        }
    }

    @PostMapping("/recipe/new")
    public Map<String, Integer> saveRecipe(@RequestBody Recipe recipe) {
        recipes.add(recipe);
        return Map.of("id", ++recipeId);
    }
}
