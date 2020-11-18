package edu.temple.dmhelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationService;

import org.json.JSONException;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import edu.temple.dmhelper.bluetooth.BluetoothService;
import edu.temple.dmhelper.bluetooth.DiscoveryActivity;
import edu.temple.dmhelper.bluetooth.JoinGameActivity;
import edu.temple.dmhelper.bluetooth.message.BluetoothMessage;
import edu.temple.dmhelper.bluetooth.message.GameStartMessage;
import edu.temple.dmhelper.bluetooth.message.PlayerJoinMessage;

public class MainActivity extends AppCompatActivity implements ActionInterface {
    public static final String TAG = "MainActivity";

    public static final int REQUEST_DISCOVER_DEVICE = 1001;
    public static final int REQUEST_JOIN_GAME = 1002;

    private LobbyFragment lobbyFragment;

    private BluetoothService.BluetoothBinder btBinder = null;
    private boolean btServiceBound = false;
    private final ServiceConnection btServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            btBinder = (BluetoothService.BluetoothBinder) service;
            btServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            btServiceBound = false;
        }
    };
    private final Handler messageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            BluetoothMessage message = (BluetoothMessage) msg.obj;

            return true;
        }
    });

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

        Intent bluetoothServiceIntent = new Intent(this, BluetoothService.class);
        startService(bluetoothServiceIntent);
        bindService(bluetoothServiceIntent, btServiceConn, Context.BIND_AUTO_CREATE);
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
                    if (connectToServer(device, character))
                        showLobby();
                }
            } else {
                Log.d(TAG, "Join game cancelled");
                showMainMenu();
            }
        }
    }

    private boolean connectToServer(BluetoothDevice device, Character character) {
        if (!btServiceBound) return false;

        try {
            btBinder.connectToServer(device, messageHandler);
            btBinder.sendMessage(new PlayerJoinMessage(character));
        } catch (IOException e) {
            Log.e(TAG, "Unable to connect to the server", e);
            showMainMenu();
            Toast.makeText(this, getString(R.string.bluetooth_failed_to_connect, device.getName()), Toast.LENGTH_LONG).show();
        }

        return true;
    }

    public void showLobby() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, lobbyFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void joinGame() {
        startDiscoveryActivity();
    }

    @Override
    public void createGame() {
        if (btServiceBound) {
            try {
                btBinder.startServer(messageHandler);
                showLobby();
            } catch (IOException e) {
                Log.e(TAG, "Unable to start the server", e);
                Toast.makeText(this, R.string.bluetooth_failed_to_start, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void startGame(List<Character> characters) {
        if (btServiceBound) {
            btBinder.sendMessage(new GameStartMessage());
            // TODO: Show Initiative tracker
        }
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