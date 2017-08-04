package id.ranuwp.greetink.rwpbaking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import id.ranuwp.greetink.rwpbaking.R;
import id.ranuwp.greetink.rwpbaking.databinding.SingleRecipeLayoutBinding;
import id.ranuwp.greetink.rwpbaking.databinding.SingleTextLayoutBinding;
import id.ranuwp.greetink.rwpbaking.model.Recipe;
import id.ranuwp.greetink.rwpbaking.model.Step;

/**
 * Created by ranuwp on 7/14/2017.
 */

public class SelectRecipeAdapter extends RecyclerView.Adapter<SelectRecipeAdapter.SelectRecipeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;
    private OnRecipeClickListener onRecipeClickListener;

    public interface OnRecipeClickListener {
        void onClick(Recipe recipe);
    }

    public void setOnRecipeClickListener(OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }

    public class SelectRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SingleRecipeLayoutBinding singleRecipeLayoutBinding;

        public SelectRecipeViewHolder(SingleRecipeLayoutBinding singleRecipeLayoutBinding) {
            super(singleRecipeLayoutBinding.getRoot());
            this.singleRecipeLayoutBinding = singleRecipeLayoutBinding;
        }

        public void bind(Recipe recipe) {
            singleRecipeLayoutBinding.recipeNameTextview.setText(recipe.getName());
            singleRecipeLayoutBinding.parent.setTag(recipe);
            singleRecipeLayoutBinding.parent.setOnClickListener(this);
            String imageUrl = recipe.getImage();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.encodeQuality(10);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.placeholder(R.drawable.ic_image);
            requestOptions.frame(1);
            singleRecipeLayoutBinding.recipeImageView.setImageDrawable(null);
            Glide.with(context).asBitmap().load(imageUrl).apply(requestOptions).into(singleRecipeLayoutBinding.recipeImageView);
            singleRecipeLayoutBinding.servingsTextview.setText(String.valueOf(recipe.getServings()));
        }

        @Override
        public void onClick(View view) {
            if (onRecipeClickListener == null) return;
            onRecipeClickListener.onClick((Recipe) view.getTag());
        }
    }

    public SelectRecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public SelectRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleRecipeLayoutBinding singleRecipeLayoutBinding =
                SingleRecipeLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SelectRecipeViewHolder(singleRecipeLayoutBinding);
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
