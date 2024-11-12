package com.example.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private ArrayList<FavouriteRecipeData> favouriteRecipeData;
    private Context context;
    private DatabaseHelper databaseHelper;

    public FavAdapter(Context context, ArrayList<FavouriteRecipeData> favouriteRecipeData) {
        this.favouriteRecipeData = favouriteRecipeData;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_cardview2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouriteRecipeData data = favouriteRecipeData.get(position);
        holder.recipe_name.setText(data.getTitle());

        Glide.with(context)
                .load(data.getImageResource())
                .into(holder.recipe_image);
    }

    @Override
    public int getItemCount() {
        return favouriteRecipeData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recipe_image;
        TextView recipe_name;
        Button favourite_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipe_image = itemView.findViewById(R.id.recipe_image);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            favourite_button = itemView.findViewById(R.id.favourite_button);

            favourite_button.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    FavouriteRecipeData item = favouriteRecipeData.get(position);

                    if (item.getFavStatus().equals("0")) {
                        item.setFavStatus("1");
                        databaseHelper.insertIntoTheDatabase(item.getTitle(), item.getImageResource(),
                                item.getFav_id(), item.getFavStatus());
                        favourite_button.setBackgroundResource(R.drawable.baseline_favorite_24);
                    } else {
                        item.setFavStatus("0");
                        databaseHelper.remove_fav(item.getFav_id());
                        favourite_button.setBackgroundResource(R.drawable.baseline_favorite_empty_24);
                    }

                    notifyItemChanged(position);

                    if (context instanceof FragmentActivity) {
                        FragmentActivity activity = (FragmentActivity) context;
                        bnv_fragment2_favourite fragment = (bnv_fragment2_favourite) activity.getSupportFragmentManager().findFragmentByTag("bnv_fragment2_favourite");
                        if (fragment != null) {
                            fragment.loadData();
                        }
                    }
                }
            });

        }
    }

    private void removeItem(int position) {
        favouriteRecipeData.remove(position);
        notifyItemRemoved(position);
    }
}
