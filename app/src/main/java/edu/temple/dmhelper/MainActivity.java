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

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.warhorn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorize();
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
    public void authorize() {
        //Uses service to submit request
        AuthorizationService service = new AuthorizationService(this);
        Intent intent = new Intent(this, WarhornActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        service.performAuthorizationRequest(generateRequest(), pi);
    }
}