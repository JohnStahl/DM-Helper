package edu.temple.dmhelper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class CharacterListFragment extends Fragment {

    private static final String CHARACTER_LIST_KEY = "characterlist";
    private CharacterList characters;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterListFragment() {}



    public static CharacterListFragment newInstance(CharacterList characters) {
        CharacterListFragment fragment = new CharacterListFragment();
        Bundle args = new Bundle();
        args.putSerializable(CHARACTER_LIST_KEY, characters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            characters = (CharacterList) getArguments().getSerializable(CHARACTER_LIST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listview = (ListView)inflater.inflate(R.layout.fragment_character_list, container, false);

        listview.setAdapter(new CharacterAdapter(getContext(), characters));


        return listview;
    }
}