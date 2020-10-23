package edu.temple.dmhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class WarhornActivity extends AppCompatActivity {
    public static final String TAG = "Warhorn Activity";
    AuthState authState;
    AuthorizationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warhorn);

        authService = new AuthorizationService(this);
        authState = new AuthState();

        handleIntent(getIntent());
    }

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
                        Log.d(TAG, "Got response from warhorn");
                        updateUI(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to get user info because: " + error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
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

    private void updateUI(JSONObject userInfo){
        Log.d(TAG, userInfo.toString());
    }

    //Extracts Authorization response from intent and sends to getUserToken()
    public void handleIntent(Intent intent){
        if(intent.getAction().equals((Intent.ACTION_VIEW))){
            Log.d(TAG, intent.getData().toString());
            AuthorizationResponse.Builder builder = new AuthorizationResponse.Builder(generateRequest());
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
        }
    }

    //Grabs user token and stores it in authState from successful authorization response
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

    //Creates request to warhorn for authorization
    public AuthorizationRequest generateRequest(){
        //Sets up authorization configuration
        Uri auth_end_point = Uri.parse(getString(R.string.auth_end_point));
        Uri token_end_point = Uri.parse(getString(R.string.token_end_point));
        AuthorizationServiceConfiguration config =
                new AuthorizationServiceConfiguration(auth_end_point, token_end_point);

        AuthorizationRequest request = new AuthorizationRequest.Builder(
                config,
                getString(R.string.client_id),
                ResponseTypeValues.CODE,
                Uri.parse(getString(R.string.redirect_url))
        ).build();
        return request;
    }
}