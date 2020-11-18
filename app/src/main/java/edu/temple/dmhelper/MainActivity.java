package edu.temple.dmhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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