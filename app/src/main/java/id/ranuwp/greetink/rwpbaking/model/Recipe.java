package id.ranuwp.greetink.rwpbaking.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ranuwp on 7/2/2017.
 */

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private int servings;
    private String image;

    public Recipe(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        name = jsonObject.getString("name");
        JSONArray ingredients = jsonObject.getJSONArray("ingredients");
        this.ingredients = new ArrayList<>();
        for(int i = 0;i<ingredients.length();i++){
            Ingredient ingredient = new Ingredient(ingredients.getJSONObject(i));
            this.ingredients.add(ingredient);
        }
        JSONArray steps = jsonObject.getJSONArray("steps");
        this.steps = new ArrayList<>();
        for (int i = 0; i < steps.length(); i++) {
            Step step = new Step(steps.getJSONObject(i));
            this.steps.add(step);
        }
        servings = jsonObject.getInt("servings");
        image = jsonObject.getString("image");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Parcelable

    public Recipe(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        ingredients = new ArrayList<>();
        parcel.readList(ingredients,getClass().getClassLoader());
        steps = new ArrayList<>();
        parcel.readList(steps,getClass().getClassLoader());
        servings = parcel.readInt();
        image = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[0];
        }
    };
}
