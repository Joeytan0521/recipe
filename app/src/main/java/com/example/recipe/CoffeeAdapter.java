package com.example.recipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.ViewHolder> {

    private ArrayList<CoffeeItem> coffeeItem;
    private Context context;
    private DatabaseHelper databaseHelper;

    public CoffeeAdapter(ArrayList<CoffeeItem> coffeeItem, Context context) {
        this.coffeeItem = coffeeItem;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_cardview,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeAdapter.ViewHolder holder, int position) {
        CoffeeItem item = coffeeItem.get(position);

        readCursorData(item, holder);
        holder.recipe_image.setImageResource(item.getImageResource());
        holder.recipe_name.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return coffeeItem.size();
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
                CoffeeItem item = coffeeItem.get(position);

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
            });
        }
    }

    private void createTableOnFirstStart() {
        databaseHelper.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorData(CoffeeItem item, ViewHolder viewHolder) {
        try (Cursor cursor = databaseHelper.read_all_data(item.getFav_id())) {
            if (cursor != null && cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAV_STATUS));
                item.setFavStatus(item_fav_status);

                if ("1".equals(item_fav_status)) {
                    viewHolder.favourite_button.setBackgroundResource(R.drawable.baseline_favorite_24);
                } else {
                    viewHolder.favourite_button.setBackgroundResource(R.drawable.baseline_favorite_empty_24);
                }
            }
        } catch (Exception e) {
            Log.e("CoffeeAdapter", "Error reading data from cursor", e);
        }
    }

}
