package edu.temple.dmhelper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiceRollerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiceRollerFragment extends Fragment {

    final int MAX_DICE = 25;

    /*d4, d6, d8, d10, d12, d20, d100 */
    int[] dieCounts = {0,0,0,0,0,0,0};
    TextView[] dieTextViews;
    TextView rollResultsTextView;
    Random rand;

    public DiceRollerFragment() {
        // Required empty public constructor
    }


    public static DiceRollerFragment newInstance() {
        DiceRollerFragment fragment = new DiceRollerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dice_roller, container, false);
        rand = new Random(System.currentTimeMillis()); //Make sure we have a different seed each time

        rollResultsTextView = v.findViewById(R.id.rollResultsTextView);
        rollResultsTextView.setMovementMethod(new ScrollingMovementMethod());
        dieTextViews = new TextView[7];
        dieTextViews[0] = v.findViewById(R.id.d4Count);
        dieTextViews[1] = v.findViewById(R.id.d6Count);
        dieTextViews[2] = v.findViewById(R.id.d8Count);
        dieTextViews[3] = v.findViewById(R.id.d10Count);
        dieTextViews[4] = v.findViewById(R.id.d12Count);
        dieTextViews[5] = v.findViewById(R.id.d20Count);
        dieTextViews[6] = v.findViewById(R.id.d100Count);

        v.findViewById(R.id.rollButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add dice roll sound effect
                String roll = rollDice();

                /*If the textview isn't empty add a new line*/
                if (!rollResultsTextView.getText().toString().equals("")) rollResultsTextView.append("\n");

                rollResultsTextView.append(roll);
            }
        });

        /*Decrement Buttons*/

        v.findViewById(R.id.d4Dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDie(0);
            }
        });

        v.findViewById(R.id.d6Dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDie(1);
            }
        });

        v.findViewById(R.id.d8Dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDie(2);
            }
        });

        v.findViewById(R.id.d10Dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDie(3);
            }
        });

        v.findViewById(R.id.d12Dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDie(4);
            }
        });

        v.findViewById(R.id.d20Dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDie(5);
            }
        });

        v.findViewById(R.id.d100Dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDie(6);
            }
        });

        /*Increment Buttons*/

        v.findViewById(R.id.d4Inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDie(0);
            }
        });

        v.findViewById(R.id.d6Inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDie(1);
            }
        });

        v.findViewById(R.id.d8Inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDie(2);
            }
        });

        v.findViewById(R.id.d10Inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDie(3);
            }
        });

        v.findViewById(R.id.d12Inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDie(4);
            }
        });

        v.findViewById(R.id.d20Inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDie(5);
            }
        });

        v.findViewById(R.id.d100Inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDie(6);
            }
        });


        return v;
    }

    /**
     * Picks random numbers corresponding to the dice specified by the user and returns the results
     * @return A string containing the results of each individual die roll as well as their total
     */
    public String rollDice(){
        StringBuilder result = new StringBuilder();
        int total = 0;

        for(int i = 0; i < dieCounts[0]; i++){ //d4
            int r = rand.nextInt(4) + 1;
            if (total > 0) result.append(" + ");
            result.append(r);
            total += r;
        }

        for(int i = 0; i < dieCounts[1]; i++){ //d6
            int r = rand.nextInt(6) + 1;
            if (total > 0) result.append(" + ");
            result.append(r);
            total += r;
        }

        for(int i = 0; i < dieCounts[2]; i++){ //d8
            int r = rand.nextInt(8) + 1;
            if (total > 0) result.append(" + ");
            result.append(r);
            total += r;
        }

        for(int i = 0; i < dieCounts[3]; i++){ //d10
            int r = rand.nextInt(10) + 1;
            if (total > 0) result.append(" + ");
            result.append(r);
            total += r;
        }

        for(int i = 0; i < dieCounts[4]; i++){ //d12
            int r = rand.nextInt(12) + 1;
            if (total > 0) result.append(" + ");
            result.append(r);
            total += r;
        }

        for(int i = 0; i < dieCounts[5]; i++){ //d20
            int r = rand.nextInt(20) + 1;
            if (total > 0) result.append(" + ");
            result.append(r);
            total += r;
        }

        for(int i = 0; i < dieCounts[6]; i++){ //d100
            int r = rand.nextInt(100) + 1;
            if (total > 0) result.append(" + ");
            result.append(r);
            total += r;
        }

        result.append(" = " + total);
        return result.toString();
    }

    /**
     * Adds one additional die of the corresponding value to the dice being rolled. Checks to ensure
     * the total number of dice being rolled does not exceed MAX_DICE.
     * Array order: d4, d6, d8, d10, d12, d20, d100
     * @param index The index of the dieCounts array that corresponds with the number of dice rolled
     */
    public void incrementDie(int index){
        int sum = 0;
        for(int i : dieCounts){
            sum += i;
            if (sum >= MAX_DICE){
                Toast.makeText(getActivity(),"Reached maximum number of dice",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        dieCounts[index]++;
        dieTextViews[index].setText("" + dieCounts[index]);
    }

    /**
     * Removes one die of the corresponding value from the dice being rolled. Checks to ensure a
     * negative number of dice are not being rolled.
     * Array order: d4, d6, d8, d10, d12, d20, d100
     * @param index The index of the dieCounts array that corresponds with the number of dice rolled
     */
    public void decrementDie(int index){
        if (dieCounts[index] <= 0) return;

        dieCounts[index]--;
        dieTextViews[index].setText("" + dieCounts[index]);
    }
}