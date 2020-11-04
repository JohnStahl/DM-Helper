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

import org.json.JSONException;

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
                try {
                    authorize();
                } catch (JSONException e) {
                    Log.d(TAG, "JSON error during authorization");
                }
            }
        });
    }

    //Called by warhorn fragment to initialize login by user into their warhorn fragment
    public void authorize() throws JSONException {
        //Uses service to submit request
        AuthState authState = AuthManager.readAuthState(this);
        if(authState == null) {
            AuthorizationService service = new AuthorizationService(this);
            Intent intent = new Intent(this, WarhornActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
            service.performAuthorizationRequest(AuthManager.generateRequest(this), pi);
            service.dispose();
        }else{
            Intent intent = new Intent(this, WarhornActivity.class);
            intent.setAction(getString(R.string.already_authenticated));
            startActivity(intent);
        }
    }
}