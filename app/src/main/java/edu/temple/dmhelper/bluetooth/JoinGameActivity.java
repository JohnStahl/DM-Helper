package edu.temple.dmhelper.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputFilter;
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
import edu.temple.dmhelper.utilities.InputFilterMinMax;

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
        final EditText initiativeField = findViewById(R.id.characterInitiative);

        int min = getResources().getInteger(R.integer.initiative_min);
        int max = getResources().getInteger(R.integer.initiative_max);
        initiativeField.setFilters(new InputFilter[]{ new InputFilterMinMax(min, max)});

        if (device == null) {
            Log.e(TAG, "EXTRA_DEVICE extra must not be null");
            finish();
            return;
        }

        ((TextView) findViewById(R.id.device)).setText(device.getName());

        findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID id = UUID.randomUUID();
                String name = nameField.getText().toString();
                int initiative = Integer.parseInt(initiativeField.getText().toString());
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