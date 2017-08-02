package id.ranuwp.greetink.rwpbaking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.ranuwp.greetink.rwpbaking.SelectRecipeDetailActivity;
import id.ranuwp.greetink.rwpbaking.databinding.SingleTextLayoutBinding;
import id.ranuwp.greetink.rwpbaking.model.Recipe;

/**
 * Created by ranuwp on 7/14/2017.
 */

public class SelectRecipeAdapter extends RecyclerView.Adapter<SelectRecipeAdapter.SelectRecipeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public class SelectRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SingleTextLayoutBinding singleTextLayoutBinding;

        public SelectRecipeViewHolder(SingleTextLayoutBinding singleTextLayoutBinding) {
            super(singleTextLayoutBinding.getRoot());
            this.singleTextLayoutBinding = singleTextLayoutBinding;
        }

        public void bind (Recipe recipe){
            singleTextLayoutBinding.recipeNameTextview.setText(recipe.getName());
            singleTextLayoutBinding.parent.setTag(recipe);
            singleTextLayoutBinding.parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SelectRecipeDetailActivity.toActivity(view.getContext(),(Recipe) view.getTag());
        }
    }

    public SelectRecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public SelectRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleTextLayoutBinding singleTextLayoutBinding =
                SingleTextLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
        return new SelectRecipeViewHolder(singleTextLayoutBinding);
    }


    @Override
    public void onBindViewHolder(SelectRecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
