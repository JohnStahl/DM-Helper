package edu.temple.dmhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

public class WarhornActivity extends AppCompatActivity {
    public static final String TAG = "Warhorn Activity";
    AuthState authState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warhorn);

        authState = new AuthState();

        Intent intent = getIntent();
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
        AuthorizationService authService = new AuthorizationService(this);
        authService.performTokenRequest(
                response.createTokenExchangeRequest(),
                new AuthorizationService.TokenResponseCallback() {
                    @Override public void onTokenRequestCompleted(TokenResponse token, AuthorizationException ex) {
                        if (token != null) {
                            // token obtained
                            authState.update(token, ex);
                            Log.d(TAG, "Obtained following access token: " + authState.getAccessToken());
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