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
import id.ranuwp.greetink.rwpbaking.databinding.SingleTextLayoutBinding;
import id.ranuwp.greetink.rwpbaking.model.Ingredient;
import id.ranuwp.greetink.rwpbaking.model.Recipe;
import id.ranuwp.greetink.rwpbaking.model.Step;

/**
 * Created by ranuwp on 7/14/2017.
 */

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder> {

    private Context context;
    private Recipe recipe;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = -1;

    public interface OnItemClickListener{
        void onItemClick(Step step);
    }

    public class MenuListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SingleTextLayoutBinding singleTextLayoutBinding;

        public MenuListViewHolder(SingleTextLayoutBinding singleTextLayoutBinding) {
            super(singleTextLayoutBinding.getRoot());
            this.singleTextLayoutBinding = singleTextLayoutBinding;
        }

        public void bindIngredients(ArrayList<Ingredient> ingredients){
            singleTextLayoutBinding.recipeImageView.setImageDrawable(null);
            String text = "Ingredients :";
            for(Ingredient ingredient : ingredients){
                text += "\n"+ingredient.getIngredient()+" "+ingredient.getQuantity()+" "+ingredient.getMeasure();
            }
            singleTextLayoutBinding.recipeNameTextview.setText(text);
        }

        public void bindSteps(Step step){
            singleTextLayoutBinding.recipeNameTextview.setText(step.getShortDescription());
            singleTextLayoutBinding.getRoot().setTag(step);
            singleTextLayoutBinding.getRoot().setOnClickListener(this);
            String url = "";
            if (!step.getVideoURL().equals("")) {
                url = step.getVideoURL();
            } else {
                url = step.getThumbnailURL();
            }
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.encodeQuality(10);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.placeholder(R.drawable.ic_image);
            requestOptions.frame(1);
            singleTextLayoutBinding.recipeImageView.setImageDrawable(null);
            Glide.with(context).asBitmap().load(url).apply(requestOptions).into(singleTextLayoutBinding.recipeImageView);
        }

        public void setSelected(boolean isSelected){
            if(isSelected){
                singleTextLayoutBinding.getRoot().setBackgroundResource(R.color.selectedItem);
            }else{
                singleTextLayoutBinding.getRoot().setBackgroundResource(android.R.color.transparent);
            }
        }

        @Override
        public void onClick(View view) {
            selectedPosition = getAdapterPosition();
            onItemClickListener.onItemClick((Step) view.getTag());
            notifyDataSetChanged();
        }
    }

    public MenuListAdapter(Context context, Recipe recipe, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.recipe = recipe;
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectedPosition(int selectedPotition) {
        this.selectedPosition = selectedPotition;
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    @Override
    public MenuListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleTextLayoutBinding singleTextLayoutBinding =
                SingleTextLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MenuListViewHolder(singleTextLayoutBinding);
    }

    @Override
    public void onBindViewHolder(MenuListViewHolder holder, int position) {
        if(position == 0){
            holder.bindIngredients(recipe.getIngredients());
        }else{
            holder.bindSteps(recipe.getSteps().get(position-1));
            if(selectedPosition == position){
                holder.setSelected(true);
            }else{
                holder.setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size()+1;
    }
}
