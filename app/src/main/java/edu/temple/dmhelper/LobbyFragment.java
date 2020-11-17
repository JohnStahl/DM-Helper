package edu.temple.dmhelper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LobbyFragment extends Fragment {
    public static final String ARG_CHARACTERS = "Characters";

    private List<Character> characters;

    public LobbyFragment() {
        this.characters = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LobbyFragment.
     */
    public static LobbyFragment newInstance(ArrayList<Character> characters) {
        LobbyFragment fragment = new LobbyFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CHARACTERS, characters);
        fragment.setArguments(args);
        return fragment;
    }

    public static LobbyFragment newInstance() {
        return newInstance(new ArrayList<Character>());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ArrayList<Character> characters = getArguments().getParcelableArrayList(ARG_CHARACTERS);
            if (characters != null) this.characters.addAll(characters);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lobby, container, false);
    }

    public void addCharacter(Character character) {
        this.characters.add(character);
    }

    public void removeCharacter(Character character) {
        this.characters.remove(character);
    }
}