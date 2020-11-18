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
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import edu.temple.dmhelper.bluetooth.DiscoveryActivity;
import edu.temple.dmhelper.bluetooth.JoinGameActivity;

public class MainActivity extends AppCompatActivity implements ActionInterface {
    public static final String TAG = "MainActivity";

    public static final int REQUEST_DISCOVER_DEVICE = 1001;
    public static final int REQUEST_JOIN_GAME = 1002;

    private LobbyFragment lobbyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lobbyFragment = LobbyFragment.newInstance();

        startDiscoveryActivity();
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

        if (requestCode == REQUEST_DISCOVER_DEVICE) { // DiscoveryActivity finished
            if (resultCode == RESULT_OK && data != null) {
                BluetoothDevice device = data.getParcelableExtra(DiscoveryActivity.EXTRA_DEVICE);
                if (device != null) {
                    Log.d(TAG, "Device selected: " + device.toString());

                    Intent intent = new Intent(this, JoinGameActivity.class);
                    intent.putExtra(JoinGameActivity.EXTRA_DEVICE, device);
                    startActivityForResult(intent, REQUEST_JOIN_GAME);
                }
            } else {
                Log.d(TAG, "Discovery cancelled");
                showMainMenu();
            }
        } else if (requestCode == REQUEST_JOIN_GAME) { // JoinGameActivity finished
            if (resultCode == RESULT_OK && data != null) {
                Character character = data.getParcelableExtra(JoinGameActivity.EXTRA_CHARACTER);
                BluetoothDevice device = data.getParcelableExtra(JoinGameActivity.EXTRA_DEVICE);

                if (device != null && character != null) {
                    Log.d(TAG, "Character created: " + character.toString());
                    // TODO: Handle connect to server
                }
            } else {
                Log.d(TAG, "Join game cancelled");
                showMainMenu();
            }
        }
    }

    public void showLobby() {
        
    }

    @Override
    public void joinGame() {
        startDiscoveryActivity();
    }

    @Override
    public void createGame() {

    }

    @Override
    public void startGame(List<Character> characters) {

    }

    @Override
    public void showDiceRoller() {

    }

    @Override
    public void showWarhorn() {

    }

    @Override
    public void showMainMenu() {

    }
}