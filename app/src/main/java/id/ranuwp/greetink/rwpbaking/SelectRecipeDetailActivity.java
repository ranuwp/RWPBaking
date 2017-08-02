package id.ranuwp.greetink.rwpbaking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import id.ranuwp.greetink.rwpbaking.databinding.SelectRecipeDetailBinding;
import id.ranuwp.greetink.rwpbaking.fragment.MenuListFragment;
import id.ranuwp.greetink.rwpbaking.fragment.StepDetailFragment;
import id.ranuwp.greetink.rwpbaking.model.Recipe;
import id.ranuwp.greetink.rwpbaking.model.Step;

public class SelectRecipeDetailActivity extends AppCompatActivity implements MenuListFragment.OnMenuListClickListener, StepDetailFragment.OnClickListener {

    private static final String RECIPE_ID = Recipe.class.getSimpleName();
    private Recipe recipe;
    private SelectRecipeDetailBinding selectRecipeDetailBinding;
    private Step currentStep;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectRecipeDetailBinding = DataBindingUtil.setContentView(this, R.layout.select_recipe_detail);
        if (getIntent() == null || !getIntent().hasExtra(RECIPE_ID) || getSupportActionBar() == null) {
            Toast.makeText(this, "Recipe is not initialized", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        fragmentManager = getSupportFragmentManager();
        recipe = getIntent().getParcelableExtra(RECIPE_ID);
        getSupportActionBar().setTitle(recipe.getName());
        if(savedInstanceState == null){
            if(getResources().getBoolean(R.bool.multiPane)){
                MenuListFragment menuListFragment = new MenuListFragment();
                menuListFragment.setRecipe(recipe);
                menuListFragment.setOnMenuListClickListener(this);
                fragmentManager.beginTransaction()
                        .replace(R.id.menu_list,menuListFragment,Recipe.class.getName())
                        .commit();
            }else{
                MenuListFragment menuListFragment = new MenuListFragment();
                menuListFragment.setRecipe(recipe);
                fragmentManager.beginTransaction()
                        .replace(R.id.menu_list,menuListFragment,Recipe.class.getName())
                        .commit();
            }
        }else{
            currentStep = savedInstanceState.getParcelable(Step.class.getName());
            if(fragmentManager.getBackStackEntryCount() > 0){
                fragmentManager.popBackStack();
            }
            if(getResources().getBoolean(R.bool.multiPane)){
                MenuListFragment menuListFragment = new MenuListFragment();
                menuListFragment.setRecipe(recipe);
                menuListFragment.setOnMenuListClickListener(this);
                fragmentManager.beginTransaction()
                        .replace(R.id.menu_list,menuListFragment,Recipe.class.getName())
                        .commit();
                int val =fragmentManager.getBackStackEntryCount();
                Log.d("RWP",String.valueOf(val));
                if(currentStep != null){
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    stepDetailFragment.setStep(currentStep,true);
                    stepDetailFragment.setOnClickListener(this);
                    fragmentManager.beginTransaction()
                            .replace(R.id.detail_step,stepDetailFragment,Step.class.getName())
                            .commit();
                }
            }else{
                MenuListFragment menuListFragment = new MenuListFragment();
                menuListFragment.setRecipe(recipe);
                fragmentManager.beginTransaction()
                        .replace(R.id.menu_list,menuListFragment,Recipe.class.getName())
                        .commit();
                if(currentStep != null){
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    stepDetailFragment.setStep(currentStep,isLastStep(currentStep));
                    stepDetailFragment.setOnClickListener(this);
                    fragmentManager.beginTransaction()
                            .replace(R.id.menu_list,stepDetailFragment,Step.class.getName())
                            .addToBackStack(null)
                            .commit();
                }
            }
        }
//        if (savedInstanceState != null) {
//            currentStep = savedInstanceState.getParcelable(Step.class.getName());
//        } else {
//            MenuListFragment menuListFragment = new MenuListFragment();
//            menuListFragment.setOnMenuListClickListener(this);
//            menuListFragment.setRecipe(recipe);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.menu_list, menuListFragment, MenuListFragment.class.getName())
//                    .commit();
//        }
//        if (selectRecipeDetailBinding.detailStep != null) {
//            twoPane = true;
//            if (currentStep == null) {
//                currentStep = recipe.getSteps().get(0);
//            }
//            MenuListFragment menuListFragment = new MenuListFragment();
//            menuListFragment.setOnMenuListClickListener(this);
//            menuListFragment.setRecipe(recipe);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.menu_list, menuListFragment, MenuListFragment.class.getName())
//                    .commit();
//            StepDetailFragment stepDetailFragment = new StepDetailFragment();
//            stepDetailFragment.setStep(currentStep, true);
//            getSupportFragmentManager().beginTransaction().
//                    replace(R.id.detail_step, stepDetailFragment, StepDetailFragment.class.getName())
//                    .commit();
//        } else if (currentStep != null) {
//            twoPane = false;
//            StepDetailFragment stepDetailFragment = new StepDetailFragment();
//            boolean isLast = recipe.getSteps().get(recipe.getSteps().size() - 1).equals(currentStep);
//            stepDetailFragment.setStep(currentStep, isLast);
//            stepDetailFragment.setOnClickListener(this);
//            getSupportFragmentManager().beginTransaction()
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .replace(R.id.menu_list, stepDetailFragment, StepDetailFragment.class.getName())
//                    .addToBackStack(null)
//                    .commit();
//        }
//        Log.d("RWP", recipe.getName());
    }

    public static void toActivity(Context context, Recipe recipe) {
        Intent intent = new Intent(context, SelectRecipeDetailActivity.class);
        intent.putExtra(RECIPE_ID, recipe);
        context.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Recipe.class.getName(),recipe);
        outState.putParcelable(Step.class.getName(),currentStep);
    }

    @Override
    public void onClick(Step step) {
        currentStep = step;
        if (getResources().getBoolean(R.bool.multiPane)) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStep(step,true);
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_step,stepDetailFragment,Step.class.getName())
                    .commit();
        } else {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setOnClickListener(this);
            boolean isLast = recipe.getSteps().get(recipe.getSteps().size() - 1).equals(currentStep);
            stepDetailFragment.setStep(step,isLast);
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.menu_list, stepDetailFragment,Step.class.getName())
                    .addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public void onNextButtonClick(Step step) {
        currentStep = step;
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        boolean isLast = isLastStep(currentStep);
        Step nextStep = getNextStep(currentStep);
        stepDetailFragment.setStep(nextStep, isLast);
        stepDetailFragment.setOnClickListener(this);
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.menu_list, stepDetailFragment,Step.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private boolean isLastStep(Step step){
        return recipe.getSteps().get(recipe.getSteps().size() - 1).equals(currentStep);
    }

    private Step getNextStep(Step step){
        return recipe.getSteps().get(recipe.getSteps().indexOf(currentStep) + 1);
    }
}