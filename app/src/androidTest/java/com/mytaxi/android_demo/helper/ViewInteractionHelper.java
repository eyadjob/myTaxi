package com.mytaxi.android_demo.helper;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.mytaxi.android_demo.R;
import com.mytaxi.android_demo.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


/**
 * This is helper class for espresso view actions and commands, to abstract the complexity of espresso implementation
 * it use java reflection to substitute the passed params in some of it methods
 * @author eyadm@amazon.com
 */

public class ViewInteractionHelper {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    /**
     * This is method that return the child matcher view based on the supplied paren matcher and position id
     * @param parentMatcher the parent position of the child position desired to be retrieved
     * @param position the position index
     * @return  child matcher view
     */
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * ths is wrapper method for getting child position espresso  implementation
     * @param firstIdentifier the id of parent view
     * @param secondIdentifier the id of the child view
     * @param firstPosition the position index for child view
     * @param className of the child position
     * @return view interaction to perform the desired operation on it : click, check... etc
     */
    
    public ViewInteraction getChildAtPosition(String firstIdentifier, String secondIdentifier, int firstPosition, int secondPosition, String className) {

        try {
            Field field = R.id.class.getField(firstIdentifier);
            Field field2 = R.id.class.getField(secondIdentifier);

            if (!className.equals("")) {

                ViewInteraction viewInteractionField = onView(
                        Matchers.allOf(ViewMatchers.withId(field.getInt(null)),
                                childAtPosition(
                                        Matchers.allOf(ViewMatchers.withId(field2.getInt(null)),
                                                childAtPosition(

                                                        ViewMatchers.withClassName(is(className)),
                                                        firstPosition)),
                                        secondPosition),
                                ViewMatchers.isDisplayed()));

                return viewInteractionField;
            } else {

                ViewInteraction floatingActionButton = onView(
                        allOf(withId(field.getInt(null)),
                                childAtPosition(
                                        childAtPosition(
                                                withId(field.getInt(null)),
                                                firstPosition),
                                        secondPosition),
                                isDisplayed()));
                floatingActionButton.perform(click());
            }




        } catch (Exception exception ) {exception.getMessage();}
            return null;

    }

    /**
     * this method type text in specific field based on it child position, when identifying the element only by id is not working
     * @param textToReplace text to type
     * @param firstIdentifier the child position id
     * @param secondIdentifier the parent position id
     * @param firstPosition the position id of the child
     * @param secondPosition the position id of the child
     * @param className the class name of the child position
     */
    
    public void typeText(String textToReplace,
                            String firstIdentifier,String secondIdentifier, int firstPosition,int secondPosition,
                            String className) {

        ViewInteraction viewInteraction =  getChildAtPosition(firstIdentifier,secondIdentifier,firstPosition,secondPosition ,className);
        viewInteraction.perform(ViewActions.typeText(textToReplace));

    }
    
    public void clickView( String firstIdentifier,String secondIdentifier, int firstPosition,int secondPosition,String className) {

        ViewInteraction viewInteraction =  getChildAtPosition(firstIdentifier,secondIdentifier,firstPosition, secondPosition,className);
        viewInteraction.perform(click());

    }


    /**
     * this method replace text in specific field based on it child position, when identifying the element only by id is not working
     * @param textToReplace text to replace
     * @param firstIdentifier the child position id
     * @param secondIdentifier the parent position id
     * @param firstPosition the position id of the child
     * @param className the class name of the child position
     */
    public void replaceText(String textToReplace,
                            String firstIdentifier,String secondIdentifier, int firstPosition,int secondPosition,
                            String className) {
       ViewInteraction viewInteraction =  getChildAtPosition(firstIdentifier,secondIdentifier,firstPosition,secondPosition,className);
       viewInteraction.perform(ViewActions.replaceText(textToReplace));

    }

    /**
     * this method replace text in specific field either based on field id or text
     * @param identifierType the way of identifying field
     * @param identifier the identifier field which will be substituted using java reflection
     * @param textToReplace the text to be replaced
     */
    
    public void replaceText(String identifierType, String identifier, String textToReplace) {


        try {
            Field field = R.id.class.getField(identifier);
            switch (identifierType) {
                case "id":
                    ViewInteraction userName = onView(
                            Matchers.allOf(ViewMatchers.withId(field.getInt(null)), ViewMatchers.isDisplayed()));
                    userName.perform(ViewActions.replaceText(textToReplace), ViewActions.closeSoftKeyboard());

                    break;

                case "text":

                    break;
                case "class":
                    break;
            }
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException exception) {
            exception.getMessage();
        }


    }


    /**
     * this method implement the functionality of opening the drawer text in specific field either based on field id or text
     * @param identifierType the way of identifying field
     * @param identifier the identifier field which will be substituted using java reflection
     */
    
    public void drawerOpen(String identifierType, String identifier) {

        try {
            Field field = R.id.class.getField(identifier);
            
            switch (identifierType) {
                case "id":

                    onView(ViewMatchers.withId(field.getInt(null))).perform(DrawerActions.open());
                    break;

                case "text":
                    onView(withText(identifier)).perform(DrawerActions.open());
                    break;
                case "class":
                    break;
            }
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException exception) {
            exception.getMessage();
        }


    }

    /**
     * this method implement the functionality of checking text in specific field either based on field id or text
     * @param identifierType the way of identifying field
     * @param identifier the identifier field which will be substituted using java reflection
     */
    
    public ViewInteraction checkText(String identifierType, String identifier) {


    switch (identifierType) {
        case "id":


            break;
        case "text":

            return onView(withText(identifier)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        case "class":
            break;
    }


return null;

    }



    /**
     * this method implement the functionality of clicking on field or element either based on field id or text
     * @param identifierType the way of identifying field
     * @param identifier the identifier field which will be substituted using java reflection
     */
    
    public void clickView(String identifierType, String identifier) {

        try {

            switch (identifierType) {
                case "id":
                    Field field = R.id.class.getField(identifier);
                    onView(Matchers.allOf(withId(field.getInt(null)), isDisplayed())).perform(click());
                    break;

                case "text":
                    onView(ViewMatchers.withText(identifier)).perform(ViewActions.click());
                    break;

                case "class":
                    break;

            }
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException exception) {
            exception.getMessage();
        }
    }




    /**
     * this method abstract the implementation of the thread sleep
     * @param period the period which is desired for the thread to stay sleeping
     */
    public void sleep(int period) {
        try {
            Thread.sleep(period);
        } catch (InterruptedException e) {
            e.printStackTrace();


        }
 }

    /**
     * This method to simulate the click on back action on the android device
     */
    public void pressBack(){
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();
    }


}
