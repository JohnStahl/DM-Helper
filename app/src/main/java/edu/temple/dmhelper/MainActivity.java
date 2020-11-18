package edu.temple.dmhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import edu.temple.dmhelper.bluetooth.DiscoveryActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public static final int REQUEST_DISCOVER_DEVICE = 1001;

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

    private void startDiscoveryActivity() {
        Log.d(TAG, "Launching Discovery Activity");
        Intent intent = new Intent(this, DiscoveryActivity.class);
        startActivityForResult(intent, REQUEST_DISCOVER_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DISCOVER_DEVICE) {
            if (resultCode == RESULT_OK && data != null) {
                BluetoothDevice device = (BluetoothDevice) data.getParcelableExtra(DiscoveryActivity.EXTRA_DEVICE);
                if (device != null) {
                    Log.d(TAG, "Device selected: " + device.toString());
                    // TODO: Handle selected device
                }
            } else {
                Log.d(TAG, "Discovery cancelled");
                // TODO: Handle discovery cancelled
            }
        }
    }
}