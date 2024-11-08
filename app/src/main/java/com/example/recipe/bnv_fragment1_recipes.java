package com.example.recipe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class bnv_fragment1_recipes extends Fragment {

    private RecyclerView recyclerView;
    private cardViewAdapter cardViewAdapter;
    private List<RecipeData> recipeDataList = new ArrayList<>();
    private ProgressBar recipe_progressBar;
    private TextView recipe_errorText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bnv_fragment1_recipes, container, false);

        recyclerView = view.findViewById(R.id.recipe_recyclerView);
        recipe_progressBar = view.findViewById(R.id.recipe_progressBar);
        recipe_errorText = view.findViewById(R.id.recipe_errorText);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cardViewAdapter = new cardViewAdapter(recipeDataList, getContext());
        recyclerView.setAdapter(cardViewAdapter);

        String myUrl = "https://www.themealdb.com/api/json/v1/1/filter.php?a=Canadian";
        new HttpGetRequest().execute(myUrl);

        return view;
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recipe_progressBar.setVisibility(View.VISIBLE);
            recipe_errorText.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result = null;
            String inputLine;
            try {
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);

                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("API Error", "HTTP error code: " + connection.getResponseCode());
                    return null;
                }

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                Log.e("API Error", "IOException: " + e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            recipe_progressBar.setVisibility(View.GONE);

            if (result == null) {
                recipe_errorText.setVisibility(View.VISIBLE);
                recipe_errorText.setText("Failed to fetch data.");
                Log.e("API Error", "Result is null");
                return;
            }

            Log.d("API Result", result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray resultsArray = jsonObject.getJSONArray("meals");

                recipeDataList.clear();
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject resultObject = resultsArray.getJSONObject(i);
                    String recipe_name = resultObject.getString("strMeal");
                    String recipe_image = resultObject.getString("strMealThumb");

                    recipeDataList.add(new RecipeData(recipe_name, recipe_image));
                }

                cardViewAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
                recipe_errorText.setVisibility(View.VISIBLE);
                recipe_errorText.setText("Error fetching data. Check log for details.");
            }
        }
    }
}
