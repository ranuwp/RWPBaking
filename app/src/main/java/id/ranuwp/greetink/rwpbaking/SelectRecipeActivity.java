package id.ranuwp.greetink.rwpbaking;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.ranuwp.greetink.rwpbaking.adapter.SelectRecipeAdapter;
import id.ranuwp.greetink.rwpbaking.databinding.SelectRecipeBinding;
import id.ranuwp.greetink.rwpbaking.helper.BakingApiHelper;
import id.ranuwp.greetink.rwpbaking.model.Recipe;

public class SelectRecipeActivity extends AppCompatActivity {

    private SelectRecipeBinding selectRecipeBinding;
    private SelectRecipeAdapter selectRecipeAdapter;
    private ArrayList<Recipe> recipes;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectRecipeBinding = DataBindingUtil.setContentView(this, R.layout.select_recipe);
        recipes = new ArrayList<>();
        if(savedInstanceState != null){
            ArrayList<Recipe> temp = savedInstanceState.getParcelableArrayList(Recipe.class.getName());
            recipes.addAll(temp);
        }
        selectRecipeAdapter = new SelectRecipeAdapter(this,recipes);
        selectRecipeBinding.recipeRecyclerview.setAdapter(selectRecipeAdapter);
        requestQueue = Volley.newRequestQueue(this);
        if(recipes.size() == 0){
            recipeRequest();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Recipe.class.getName(),recipes);
    }

    private void recipeRequest(){
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                BakingApiHelper.URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            recipes.clear();
                            for(int i = 0; i<response.length();i++){
                                Recipe recipe = new Recipe(response.getJSONObject(i));
                                recipes.add(recipe);
                            }
                            selectRecipeAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SelectRecipeActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
