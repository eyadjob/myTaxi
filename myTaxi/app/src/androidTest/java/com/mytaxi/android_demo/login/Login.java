package com.mytaxi.android_demo.login;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.helper.ViewInteractionHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * This is Login automated testing class for myTaxi App
 * @author eyadm@amaozn.com
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class Login {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    /**
     * this Login Login case for :
     * -Open app login_ValidUser page
     * -Set valid user name "crazydog335"
     * -set valid password "venture"
     * -Hit login_ValidUser action
     * -check that search field is present
     *
     */
    @Test
    public void login_ValidUser() {

        ViewInteractionHelper viewInteractionHelper = new ViewInteractionHelper();


        viewInteractionHelper.sleep(2000);

        viewInteractionHelper.clickView("id", "edt_username");
        viewInteractionHelper.replaceText("id", "edt_username", "crazydog335");
        viewInteractionHelper.clickView("id", "edt_password");
        viewInteractionHelper.replaceText("id", "edt_password", "venture");
        viewInteractionHelper.clickView("id", "btn_login");

        viewInteractionHelper.drawerOpen("id", "drawer_layout");
        viewInteractionHelper.clickView("text", "Logout");

    }



}
