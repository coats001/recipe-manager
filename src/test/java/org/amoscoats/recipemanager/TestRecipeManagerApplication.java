package org.amoscoats.recipemanager;

import org.springframework.boot.SpringApplication;

public class TestRecipeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(RecipeManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
