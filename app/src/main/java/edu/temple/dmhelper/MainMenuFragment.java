package edu.temple.dmhelper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {
    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    private ActionInterface actionInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActionInterface) {
            actionInterface = (ActionInterface) context;
        } else {
            throw new RuntimeException(context.getClass().getName() + " must implement ActionInterface.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_menu, container, false);

        root.findViewById(R.id.createGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInterface.createGame();
            }
        });

        root.findViewById(R.id.joinGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInterface.joinGame();
            }
        });

        root.findViewById(R.id.diceRollerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInterface.showDiceRoller();
            }
        });

        root.findViewById(R.id.warhornButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInterface.showWarhorn();
            }
        });

        return root;
    }
}