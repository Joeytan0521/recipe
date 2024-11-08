package com.example.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class cardViewAdapter extends RecyclerView.Adapter<cardViewAdapter.ViewHolder>{

    private List<RecipeData> recipeDataList;
    Context context;

    public cardViewAdapter(List<RecipeData> recipeDataList, Context context) {
        this.recipeDataList = recipeDataList;
        this.context = context;

    }

    @NonNull
    @Override
    public cardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewAdapter.ViewHolder holder, int position) {
        holder.recipe_name.setText(recipeDataList.get(position).getRecipeName());
//        holder.recipe_desc.setText(recipeDataList.get(position).getRecipeDesc());

        Glide.with(context).load(recipeDataList.get(position).getRecipeImage()).into(holder.recipe_image);
    }

    @Override
    public int getItemCount() {
        return recipeDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView recipe_image;
        public TextView recipe_name;
//        public TextView recipe_desc;
        public Button favourite_button;

        public ViewHolder(View itemView) {
            super(itemView);
            recipe_image = itemView.findViewById(R.id.recipe_image);
            recipe_name = itemView.findViewById(R.id.recipe_name);
//            recipe_desc = itemView.findViewById(R.id.recipe_desc);
            favourite_button = itemView.findViewById(R.id.favourite_button);
        }
    }
}
