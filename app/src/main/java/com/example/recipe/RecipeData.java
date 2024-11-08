package com.example.recipe;

public class RecipeData {

    private String recipe_name;
//    private String recipe_desc;
    private String recipe_image;


    public RecipeData(String recipe_name, String recipe_image) {
        this.recipe_name = recipe_name;
//        this.recipe_desc = recipe_desc;
        this.recipe_image = recipe_image;
    }

    public String getRecipeName() {
        return recipe_name;
    }

//    public String getRecipeDesc() {
//        return recipe_desc;
//    }

    public String getRecipeImage() {
        return recipe_image;
    }

}
