package id.ranuwp.greetink.rwpbaking.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import id.ranuwp.greetink.rwpbaking.R;
import id.ranuwp.greetink.rwpbaking.SelectRecipeActivity;
import id.ranuwp.greetink.rwpbaking.adapter.SelectRecipeAdapter;
import id.ranuwp.greetink.rwpbaking.databinding.RecipeDetailWidgetConfigureBinding;
import id.ranuwp.greetink.rwpbaking.helper.BakingApiHelper;
import id.ranuwp.greetink.rwpbaking.model.Ingredient;
import id.ranuwp.greetink.rwpbaking.model.Recipe;

/**
 * The configuration screen for the {@link RecipeDetailWidget RecipeDetailWidget} AppWidget.
 */
public class RecipeDetailWidgetConfigureActivity extends Activity implements SelectRecipeAdapter.OnRecipeClickListener {

    private static final String PREFS_NAME = "id.ranuwp.greetink.rwpbaking.widget.RecipeDetailWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_RECIPE = "recipe";
    private static final String PREF_INGREDIENTS = "ingredients";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public RecipeDetailWidgetConfigureActivity() {
        super();
    }

    //RWP
    private RecipeDetailWidgetConfigureBinding recipeDetailWidgetConfigureBinding;
    private RequestQueue requestQueue;
    public ArrayList<Recipe> recipes;
    public SelectRecipeAdapter selectRecipeAdapter;
    //-

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        recipeDetailWidgetConfigureBinding = DataBindingUtil.setContentView(this,R.layout.recipe_detail_widget_configure);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        recipes = new ArrayList<>();
        selectRecipeAdapter = new SelectRecipeAdapter(this,recipes);
        selectRecipeAdapter.setOnRecipeClickListener(this);
        recipeDetailWidgetConfigureBinding.recipeRecyclerview.setAdapter(selectRecipeAdapter);
        if(recipes.isEmpty()){
            recipeRequest();
        }

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
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
                        Toast.makeText(RecipeDetailWidgetConfigureActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(Recipe recipe) {
        Context context = getApplicationContext();
        saveRecipePref(context,mAppWidgetId,recipe);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeDetailWidget.updateAppWidget(context,appWidgetManager,mAppWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
        setResult(RESULT_OK,resultValue);
        finish();
    }

    public static void saveRecipePref(Context context, int appWidgetId, Recipe recipe){
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit();
        prefs.putString(PREF_PREFIX_KEY+appWidgetId+PREF_RECIPE,recipe.getName());
        String ingredientsText = "";
        for(Ingredient ingredient : recipe.getIngredients()){
            String ingredientText = ingredient.getIngredient()+" "+ingredient.getQuantity()+" "+ingredient.getMeasure();
            ingredientsText += ingredientText+";";
        }
        prefs.putString(PREF_PREFIX_KEY+appWidgetId+PREF_INGREDIENTS,ingredientsText);
        prefs.apply();
    }

    public static Recipe loadRecipePrefs(Context context, int appWidgetId){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        Recipe recipe = new Recipe();
        recipe.setName(prefs.getString(PREF_PREFIX_KEY+appWidgetId+PREF_RECIPE,""));
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String[] prefsIngredients = prefs.getString(PREF_PREFIX_KEY+appWidgetId+PREF_INGREDIENTS,"").split(";");
        for(String prefsIngredient : prefsIngredients){
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredient(prefsIngredient);
            ingredients.add(ingredient);
        }
        recipe.setIngredients(ingredients);
        return recipe;
    }

    public static void deleteRecipe(Context context, int appWidgetId){
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit();
        prefs.remove(PREF_PREFIX_KEY+appWidgetId+PREF_RECIPE);
        prefs.remove(PREF_PREFIX_KEY+appWidgetId+PREF_INGREDIENTS);
        prefs.apply();
    }

}

