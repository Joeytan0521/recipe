package com.example.recipe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

public class bnv_fragment2_favourite extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ArrayList<FavouriteRecipeData> favouriteRecipeData = new ArrayList<>();
    private FavAdapter favAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bnv_fragment2_favourite, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.favourite_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData();
        return view;
    }

    public void loadData() {
        if(favouriteRecipeData != null){
            favouriteRecipeData.clear();
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = databaseHelper.select_all_favorite_list();
        try {
            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAV_TITLE));
                String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAV_ID));
                int imageResource = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAV_IMAGE_URI));

                FavouriteRecipeData favouriteRecipeData1 = new FavouriteRecipeData(id, title, imageResource);
                favouriteRecipeData.add(favouriteRecipeData1);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        favAdapter = new FavAdapter(getActivity(), favouriteRecipeData);
        recyclerView.setAdapter(favAdapter);
    }


}
