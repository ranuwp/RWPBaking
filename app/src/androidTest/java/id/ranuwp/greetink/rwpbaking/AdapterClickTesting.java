package id.ranuwp.greetink.rwpbaking;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AdapterClickTesting {

    @Rule
    public ActivityTestRule<SelectRecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(SelectRecipeActivity.class);

    @Test
    public void toDetailRecipe(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.menu_list)).check(matches(isDisplayed()));
    }
}
