package edu.temple.dmhelper.warhorn;

import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

public class ChromeLogIn {

    //Handles all UI calls for authenticating in google chrome for our application
    public static void login() throws UiObjectNotFoundException {
        boolean firstAuthentication = true;
        String webURL = null;

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        try {
            UiObject url = device.findObject(new UiSelector()
                    .resourceId("com.android.chrome:id/url_bar"));
            webURL = url.getText();
        }catch (UiObjectNotFoundException e){
            firstAuthentication = false;
        }

        if(firstAuthentication) {
            Log.d("Test", webURL);
            if(webURL != null && webURL.equals("warhorn.net")) {
                UiObject email = device.findObject(new UiSelector()
                        .resourceId("session_login"));
                UiObject password = device.findObject(new UiSelector()
                        .resourceId("session_password"));
                UiObject loginButton = device.findObject(new UiSelector()
                        .text("LOG IN")
                        .className("android.widget.Button"));
                try {
                    email.setText("jimmyahurd@gmail.com");
                    password.setText("WarHorn01663");
                    loginButton.click();
                }catch(UiObjectNotFoundException e){}
            }

            UiObject menuButton = device.findObject(new UiSelector()
                    .resourceId("com.android.chrome:id/menu_button"));
            menuButton.click();
            UiObject openInBrowserButton = device.findObject(new UiSelector()
                    .resourceId("com.android.chrome:id/menu_item_text")
                    .text("Open in Chrome"));
            openInBrowserButton.click();

            try {
                UiObject dmHelperIcon = device.findObject(new UiSelector()
                        .text("DM Helper"));
                dmHelperIcon.click();
            }catch (UiObjectNotFoundException e){}

            UiObject openInAppButton = device.findObject(new UiSelector()
                    .resourceId("android:id/button_once"));
            openInAppButton.click();
        }
    }
}
