package com.recipes.controller;

import com.recipes.model.Recipe;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RecipeController {
    private Recipe recipe;

    @GetMapping("/recipe")
    public Recipe getRecipe() {
        return recipe;
    }

    @PostMapping("/recipe")
    public void saveRecipe(@RequestBody Recipe recipe) {
        this.recipe = recipe;
    }
}
