package edu.temple.dmhelper;

import android.content.Context;
import android.content.SharedPreferences;

import net.openid.appauth.AuthState;

import org.json.JSONException;

public class AuthManager {

    //Gets Authentication state from private shared preferences
    public static AuthState readAuthState(Context context) throws JSONException {
        SharedPreferences authPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String stateJson = authPrefs.getString("stateJson", null);
        if (stateJson != null) {
            return AuthState.jsonDeserialize(stateJson);
        } else {
            return null;
        }
    }

    //Writes Authentication state to private shared preferences
    public static void writeAuthState(Context context, AuthState authState) {
        SharedPreferences authPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        authPrefs.edit()
                .putString("stateJson", authState.jsonSerializeString())
                .apply();
    }
}
