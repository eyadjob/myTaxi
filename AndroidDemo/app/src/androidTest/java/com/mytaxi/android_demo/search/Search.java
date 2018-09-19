package com.mytaxi.android_demo.search;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mytaxi.android_demo.activities.MainActivity;

import com.mytaxi.android_demo.helper.ViewInteractionHelper;
import com.mytaxi.android_demo.search.login.Login;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

/**
 * This is search automated test cases for myTaxi app
 * @author eyadm@amazon.com
 */
@android.support.test.filters.LargeTest
@RunWith(AndroidJUnit4.class)
public class Search {



    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * This test case for search for an existing driver name :
     * -enter valid driver name
     * -click on the driver name
     * -verify the driver information is valid and present
     * -click on call action
     * -click back action on the android device to move back
     * -click back action again on the android device to move back to the myTaxi app to the driver profile page
     * -click back action on the app main activity to move to the search page
     * -click on burgerMenu action
     * -click on logout link
     */
    @Test
    public void search_ExistingDriver() {
        ViewInteractionHelper viewInteractionHelper = new ViewInteractionHelper();


        Login login = new Login();
        login.login_ValidUser();

        viewInteractionHelper.sleep(3000);
        viewInteractionHelper.typeText("wow", "textSearch", "searchContainer", 1,0, "android.support.design.widget.CoordinatorLayout");
        viewInteractionHelper.replaceText("sa", "textSearch", "searchContainer", 1,0, "android.support.design.widget.CoordinatorLayout");

        viewInteractionHelper.sleep(2000);
        
        onView(ViewMatchers.withText("Sarah Scott")).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click());
        viewInteractionHelper.checkText("text", "Sarah Scott");
        viewInteractionHelper.checkText("text", "Driver Profile");
        viewInteractionHelper.checkText("text", "6834 charles st");
        viewInteractionHelper.checkText("text", "2002-10-18");

        viewInteractionHelper.clickView("id", "fab");

        viewInteractionHelper.pressBack();
        viewInteractionHelper.pressBack();
        Espresso.pressBack();

        viewInteractionHelper.drawerOpen("id", "drawer_layout");
        viewInteractionHelper.clickView("text", "Logout");

    }



}
