package edu.temple.dmhelper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LobbyFragment extends Fragment {
    private final ArrayList<Character> characters;

    public LobbyFragment() {
        this.characters = new ArrayList<>();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_lobby, container, false);

        return root;
    }

    public void addCharacter(Character character) {
        if (!characters.contains(character))
            this.characters.add(character);
    }

    public void removeCharacter(Character character) {
        this.characters.remove(character);
    }

    public void addCharacters(ArrayList<Character> characters) {
        for (Character character : characters) addCharacter(character);
    }

    public ArrayList<Character> getCharacters() {
        return this.characters;
    }
}