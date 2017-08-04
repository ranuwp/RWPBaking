package id.ranuwp.greetink.rwpbaking.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import id.ranuwp.greetink.rwpbaking.R;
import id.ranuwp.greetink.rwpbaking.SelectRecipeActivity;
import id.ranuwp.greetink.rwpbaking.model.Ingredient;
import id.ranuwp.greetink.rwpbaking.model.Recipe;

/**
 * Created by ranuwp on 8/4/2017.
 */

public class ListIngredientsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        Recipe recipe = bundle.getParcelable(Recipe.class.getName());
        return new ListRemoteViewsFactory(this.getApplicationContext(),recipe);
    }

}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context context;
    private Recipe recipe;

    public ListRemoteViewsFactory(Context context, Recipe recipe){
        this.context = context;
        this.recipe = recipe;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(recipe.getIngredients() != null){
            return recipe.getIngredients().size();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Ingredient ingredient = recipe.getIngredients().get(i);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_text_widget_ingredient);
        views.setTextViewText(R.id.ingredient_name_textview,ingredient.getIngredient());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
