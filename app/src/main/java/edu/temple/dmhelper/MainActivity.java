package edu.temple.dmhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MainActivity extends AppCompatActivity implements WarhornFragment.WarhornInterface {
    private static final String TAG = "Main Activity";
    private static final int RC_AUTH = 2;

    Fragment warhornFragment;
    AuthState authState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grabs Warhorn fragment and creates new instance if necessary
        warhornFragment = getSupportFragmentManager().findFragmentById(R.id.Container);
        if(!(warhornFragment instanceof WarhornFragment)){
            warhornFragment = WarhornFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.Container, warhornFragment)
                    .commit();
        }

        authState = new AuthState();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    //Handles various intents; mostly used for handling authorization responses
    public void handleIntent(Intent intent){
        if(intent.getAction().equals(Intent.ACTION_MAIN))
            return;

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

    //Called by warhorn fragment to initialize login by user into their warhorn fragment
    @Override
    public void authorize() {
        //Uses service to submit request
        AuthorizationService service = new AuthorizationService(this);
        Intent authIntent = service.getAuthorizationRequestIntent(generateRequest());
        startActivityForResult(authIntent, RC_AUTH);
    }
}