package edu.temple.dmhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ClientAuthentication;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class WarhornActivity extends AppCompatActivity {
    public static final String TAG = "Warhorn Activity";
    AuthState authState;
    AuthorizationService authService;

    Fragment EventInfo;
    Fragment UserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warhorn);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventInfo = getSupportFragmentManager().findFragmentById(R.id.Event_Info);
        UserInfo = getSupportFragmentManager().findFragmentById(R.id.Profile_Info);

        if(!(UserInfo instanceof UserInfoFragment)){
            handleIntent(getIntent());
        }
        if(!(EventInfo instanceof EventInfoFragment)) {
            EventInfo = EventInfoFragment.NewInstance(authState.getAccessToken());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.Event_Info, EventInfo)
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AuthManager.writeAuthState(this, authState);
        authState = null;
        authService.dispose();
    }

    //Using obtained access token requests user info from warhorn and updates UI with response
    public void getUserInfo(){
        authState.performActionWithFreshTokens(authService, new AuthState.AuthStateAction() {
            @Override
            public void execute(@Nullable final String accessToken, @Nullable final String idToken,
                                @Nullable AuthorizationException ex) {
                String url = getString(R.string.user_info_end_point);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Successful request and we should update UI with newly obtained user info
                        Log.d(TAG, "Got response from warhorn");
                        updateUI(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Unsuccessful request, so we log the error
                        Log.e(TAG, "Failed to get user info because: " + error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        //Adds access token to request
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "bearer " + accessToken);
                        Log.d(TAG, headers.toString());
                        return headers;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(WarhornActivity.this);
                queue.add(request);
            }
        });
    }

    //First pulls relevant information from user info response
    //Then creates new user info fragment with relevant information
    private void updateUI(JSONObject userInfo){
        try {
            String name = userInfo.getString("name");
            String email = userInfo.getString("email");
            String pictureURL = userInfo.getString("picture");
            UserInfo = UserInfoFragment.newInstance(name, email, pictureURL);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.Profile_Info, UserInfo)
                    .commit();
        } catch (JSONException e) {
            Log.e(TAG, "Obtained user info object is invalid");
        }
    }

    //Extracts Authorization response from intent and sends to getUserToken()
    public void handleIntent(Intent intent){
        if(intent.getAction().equals((Intent.ACTION_VIEW))){
            authState = new AuthState();
            authService = new AuthorizationService(this);
            Log.d(TAG, intent.getData().toString());
            AuthorizationResponse.Builder builder = new AuthorizationResponse.Builder(AuthManager.generateRequest(this));
            AuthorizationResponse response = builder.fromUri(intent.getData()).build();
            if(response == null){
                AuthorizationException exception = AuthorizationException.fromOAuthRedirect(intent.getData());
                if(exception == null)
                    Log.d(TAG, "Response from server is null");
                else
                    Log.e(TAG, "Registration failed with following error: " + exception.toString());
            }else{
                authState.update(response, null);
                getUserToken(response);
            }
        }else{
            Log.d(TAG, "Getting pre-existing authorization");
            try {
                authState = AuthManager.readAuthState(this);
            } catch (JSONException e) {
                Log.e(TAG, "Authstate not properly formatted");
                authState = new AuthState();
            }
            authService = new AuthorizationService(this);
            getUserInfo();
        }
    }

    //Grabs user token and stores it in authState from successful authorization response
    //Then calls getUserInfo() to use acquired token to obtain relevant user information
    public void getUserToken(AuthorizationResponse response){
        authService.performTokenRequest(
                response.createTokenExchangeRequest(),
                new AuthorizationService.TokenResponseCallback() {
                    @Override public void onTokenRequestCompleted(TokenResponse token, AuthorizationException ex) {
                        if (token != null) {
                            // token obtained
                            //Log.d(TAG, token.jsonSerializeString());
                            authState.update(token, ex);
                            Log.d(TAG, "Obtained following access token: " + authState.getAccessToken());
                            Log.d(TAG, "Obtained id token: " + authState.getIdToken());
                            getUserInfo();
                        } else {
                            // authorization failed
                            Log.e(TAG, "Token exchanged failed with following error: " + ex.toString());
                        }
                    }
                });
    }
}