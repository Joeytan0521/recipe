package com.example.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bnv_fragment1_recipes extends Fragment {

    private RecyclerView recyclerView;
    private cardViewAdapter cardViewAdapter;
    private List<RecipeData> recipeDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bnv_fragment1_recipes, container, false);

        recyclerView = view.findViewById(R.id.recipe_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeDataList = new ArrayList<>();
        loadSampleData();

        cardViewAdapter = new cardViewAdapter(recipeDataList, getContext());
        recyclerView.setAdapter(cardViewAdapter);

        return view;
    }

    private void loadSampleData() {

        recipeDataList.add(new RecipeData("Recipe 1", "Description 1",
                "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2016/6/12/3/FNM070116_Penne-with-Vodka-Sauce-and-Mini-Meatballs-recipe_s4x3.jpg.rend.hgtvcom.1280.1280.suffix/1465939620872.jpeg"));
        recipeDataList.add(new RecipeData("Recipe 2", "Description 2",
                "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/chorizo-mozarella-gnocchi-bake-cropped-9ab73a3.jpg"));
        recipeDataList.add(new RecipeData("Recipe 3", "Description 3",
                "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2016/6/12/3/FNM070116_Penne-with-Vodka-Sauce-and-Mini-Meatballs-recipe_s4x3.jpg.rend.hgtvcom.1280.1280.suffix/1465939620872.jpeg"));
        recipeDataList.add(new RecipeData("Recipe 4", "Description 4",
                "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/chorizo-mozarella-gnocchi-bake-cropped-9ab73a3.jpg"));
        recipeDataList.add(new RecipeData("Recipe 5", "Description 5",
                ""));

    }

}


