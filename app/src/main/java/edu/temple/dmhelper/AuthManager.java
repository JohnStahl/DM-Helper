package edu.temple.dmhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

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

    //Creates request to warhorn for authorization
    public static AuthorizationRequest generateRequest(Context context){
        //Sets up authorization configuration
        Uri auth_end_point = Uri.parse(context.getString(R.string.auth_end_point));
        Uri token_end_point = Uri.parse(context.getString(R.string.token_end_point));
        AuthorizationServiceConfiguration config =
                new AuthorizationServiceConfiguration(auth_end_point, token_end_point);

        AuthorizationRequest request = new AuthorizationRequest.Builder(
                config,
                context.getString(R.string.client_id),
                ResponseTypeValues.CODE,
                Uri.parse(context.getString(R.string.redirect_url))
        ).build();
        return request;
    }
}
