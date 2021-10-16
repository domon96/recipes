package com.recipes.controller;

import com.recipes.model.Recipe;
import com.recipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable int id) {

        final Optional<Recipe> recipe = recipeService.findRecipeById(id);
        if (recipe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(recipe.get(), HttpStatus.OK);
    }

    @PostMapping("/recipe/new")
    public Map<String, Integer> saveRecipe(@Valid @RequestBody Recipe recipe) {

        final Recipe savedRecipe = recipeService.save(recipe);
        return Map.of("id", savedRecipe.getId());
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable int id) {

        try {
            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable int id,
                                             @Valid @RequestBody Recipe newRecipe) {

        final Optional<Recipe> oldRecipe = recipeService.findRecipeById(id);
        if (oldRecipe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newRecipe.setId(id);
        newRecipe.updateDate();
        recipeService.save(newRecipe);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/recipe/search")
    public ResponseEntity<List<Recipe>> get(@RequestParam(required = false) String category,
                                            @RequestParam(required = false) String name) {

        if ((category == null && name == null) || (category != null && name != null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (category != null) {
            final List<Recipe> recipesByCategory = recipeService.findRecipesByCategory(category);
            return new ResponseEntity<>(recipesByCategory, HttpStatus.OK);
        } else {
            final List<Recipe> recipesContainingName = recipeService.findRecipesContainingName(name);
            return new ResponseEntity<>(recipesContainingName, HttpStatus.OK);
        }
    }
}
