package id.ranuwp.greetink.rwpbaking.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.ranuwp.greetink.rwpbaking.R;
import id.ranuwp.greetink.rwpbaking.adapter.MenuListAdapter;
import id.ranuwp.greetink.rwpbaking.databinding.FragmentMenuListBinding;
import id.ranuwp.greetink.rwpbaking.model.Recipe;
import id.ranuwp.greetink.rwpbaking.model.Step;


public class MenuListFragment extends Fragment implements MenuListAdapter.OnItemClickListener {


    private static final String LAYOUT_MANAGER_TAG = "layout_manager";
    public interface OnMenuListClickListener {
        void onClick(Step step);
    }

    private FragmentMenuListBinding fragmentMenuListBinding;
    private Recipe recipe;
    private MenuListAdapter menuListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private OnMenuListClickListener onMenuListClickListener;

    public MenuListFragment() {
        // Required empty public constructor
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setOnMenuListClickListener(OnMenuListClickListener onMenuListClickListener) {
        this.onMenuListClickListener = onMenuListClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);
        fragmentMenuListBinding = FragmentMenuListBinding.bind(view);
        layoutManager = fragmentMenuListBinding.menuListRecyclerview.getLayoutManager();
        if(savedInstanceState != null){
            recipe = savedInstanceState.getParcelable(Recipe.class.getName());
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_TAG));
        }
        if(recipe!=null){
            menuListAdapter = new MenuListAdapter(getContext(), recipe, this);
            fragmentMenuListBinding.menuListRecyclerview.setAdapter(menuListAdapter);
        }
        return fragmentMenuListBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onMenuListClickListener = (OnMenuListClickListener) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Recipe.class.getName(), recipe);
        outState.putParcelable(LAYOUT_MANAGER_TAG,layoutManager.onSaveInstanceState());
    }

    @Override
    public void onItemClick(Step step) {
        if(onMenuListClickListener == null) return;
        onMenuListClickListener.onClick(step);
    }

    public void setSelectedItem(Step step){
        int selectedPosition = -1;
        if(step != null){
            selectedPosition = recipe.getSteps().indexOf(step);
        }
        menuListAdapter.setSelectedPosition(selectedPosition);
        menuListAdapter.notifyDataSetChanged();
    }
}
