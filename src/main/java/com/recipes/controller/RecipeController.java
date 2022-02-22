package com.recipes.controller;

import com.recipes.model.Recipe;
import com.recipes.model.User;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
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
    public Map<String, Integer> saveRecipe(Authentication auth, @Valid @RequestBody Recipe recipe) {

        final User author = userService.findUserByEmail(auth.getName());
        recipe.setAuthor(author);

        final Recipe savedRecipe = recipeService.save(recipe);
        return Map.of("id", savedRecipe.getId());
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(Authentication auth, @PathVariable int id) {

        final Optional<Recipe> recipe = recipeService.findRecipeById(id);
        if (recipe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final User loggedUser = userService.findUserByEmail(auth.getName());
        if (loggedUser.equals(recipe.get().getAuthor())) {
            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(Authentication auth, @PathVariable int id, @Valid @RequestBody Recipe newRecipe) {

        final Optional<Recipe> oldRecipe = recipeService.findRecipeById(id);
        if (oldRecipe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final User loggedUser = userService.findUserByEmail(auth.getName());
        if (oldRecipe.get().getAuthor().equals(loggedUser)) {
            newRecipe.setId(id);
            newRecipe.updateDate();
            recipeService.save(newRecipe);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/recipe/search")
    public ResponseEntity<List<Recipe>> get(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {

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

    @GetMapping("/register")
    public String register(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result) {

        if (result.hasErrors()) {
            return "add-user";
        }

        if (userService.findUserByEmail(user.getEmail()) != null) {
            return "add-user";
        }

        userService.save(user);
        return "home";
    }
}
