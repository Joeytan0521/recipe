package com.example.recipe;

public class FavouriteRecipeData {

    private int imageResource;
    private String title;
    private String fav_id;
    private String favStatus;

    public FavouriteRecipeData(String fav_id, String title, int imageResource) {
        this.fav_id = fav_id;
        this.title = title;
        this.imageResource = imageResource;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getFav_id() {
        return fav_id;
    }

    public void setFav_id(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}


