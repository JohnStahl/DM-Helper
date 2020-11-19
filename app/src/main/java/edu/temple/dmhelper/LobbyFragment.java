package edu.temple.dmhelper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LobbyFragment extends Fragment {
    private CharacterAdapter characterAdapter;

    public LobbyFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LobbyFragment.
     */
    public static LobbyFragment newInstance() {
        return new LobbyFragment();
    }

    private ActionInterface actionInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.characterAdapter = new CharacterAdapter(context);
        if (context instanceof ActionInterface) {
            actionInterface = (ActionInterface) context;
        } else {
            throw new RuntimeException(context.getClass().getName() + " must implement ActionInterface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_lobby, container, false);

        ListView charactersList = root.findViewById(R.id.characters);
        charactersList.setAdapter(characterAdapter);

        Button startGameButton = root.findViewById(R.id.startGameButton);
        Button endGameButton = root.findViewById(R.id.endGameButton);


        int visibility = actionInterface.isDm() ? View.VISIBLE : View.GONE;
        startGameButton.setVisibility(visibility);
        endGameButton.setVisibility(visibility);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInterface.startGame(characterAdapter.getAll());
            }
        });

        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInterface.endGame();
                actionInterface.showMainMenu();
            }
        });

        return root;
    }

    public void addCharacter(Character character) {
        characterAdapter.addItem(character);
    }

    public void removeCharacter(Character character) {
        characterAdapter.removeItem(character);
    }

    public void addCharacters(ArrayList<Character> characters) {
        characterAdapter.addItems(characters);
    }

    public ArrayList<Character> getCharacters() {
        return this.characterAdapter.getAll();
    }
}