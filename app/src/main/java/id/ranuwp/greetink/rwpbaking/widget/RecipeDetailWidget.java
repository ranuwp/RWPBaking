package id.ranuwp.greetink.rwpbaking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import id.ranuwp.greetink.rwpbaking.R;
import id.ranuwp.greetink.rwpbaking.SelectRecipeActivity;
import id.ranuwp.greetink.rwpbaking.SelectRecipeDetailActivity;
import id.ranuwp.greetink.rwpbaking.model.Recipe;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeDetailWidgetConfigureActivity RecipeDetailWidgetConfigureActivity}
 */
public class RecipeDetailWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Recipe recipe = RecipeDetailWidgetConfigureActivity.loadRecipePrefs(context,appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_detail_widget);
        views.setTextViewText(R.id.recipeTitleTextView, recipe.getName());
        //Set ListView
        Intent intentIngredients = new Intent(context,ListIngredientsService.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Recipe.class.getName(),recipe);
        intentIngredients.putExtra("bundle",extras);
        views.setRemoteAdapter(R.id.ingredientsListView, intentIngredients);
        Intent intent = new Intent(context, SelectRecipeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_layout,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeDetailWidgetConfigureActivity.deleteRecipe(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

