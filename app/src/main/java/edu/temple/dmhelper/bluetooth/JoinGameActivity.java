package edu.temple.dmhelper.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import edu.temple.dmhelper.Character;
import edu.temple.dmhelper.R;

public class JoinGameActivity extends AppCompatActivity {
    private static final String TAG = "JoinGameActivity";

    public static final String ACTION_CHARACTER_CREATED = "edu.temple.dmhelper.join.action.CHARACTER_CREATED";
    public static final String EXTRA_DEVICE = "edu.temple.dmhelper.join.extra.DEVICE";
    public static final String EXTRA_CHARACTER = "edu.temple.dmhelper.join.extra.CHARACTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        setResult(RESULT_CANCELED);

        final BluetoothDevice device = getIntent().getParcelableExtra(EXTRA_DEVICE);
        final EditText nameField = findViewById(R.id.characterName);
        final Spinner initiativeSpin = findViewById(R.id.characterInitiative);

        if (device == null) {
            Log.e(TAG, "EXTRA_DEVICE extra must not be null");
            finish();
            return;
        }

        ArrayAdapter<CharSequence> initiativeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.initiatives,
                android.R.layout.simple_spinner_item
        );
        initiativeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initiativeSpin.setAdapter(initiativeAdapter);

        ((TextView) findViewById(R.id.device)).setText(device.getName());

        findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID id = UUID.randomUUID();
                String name = nameField.getText().toString();
                int initiative = initiativeSpin.getSelectedItemPosition();
                Character character = new Character(name, initiative, id);

                Intent intent = new Intent(ACTION_CHARACTER_CREATED);
                intent.putExtra(EXTRA_DEVICE, device);
                intent.putExtra(EXTRA_CHARACTER, character);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.cancelJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}