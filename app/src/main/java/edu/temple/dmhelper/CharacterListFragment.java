package edu.temple.dmhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class CharacterListFragment extends Fragment {

    private static final String CHARACTER_LIST_KEY = "characterlist";
    private CharacterList characters;
    CharacterListInterface parentFragment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof CharacterListInterface) {
            parentFragment = (CharacterListInterface) getParentFragment();
        } else {
            throw new RuntimeException("Please implement the required interface(s)");
        }
    }

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
        } else {
            characters = new CharacterList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listview = (ListView)inflater.inflate(R.layout.fragment_character_list, container, false);

        listview.setAdapter(new CharacterAdapter(getContext(), characters));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                parentFragment.removeCharacter(position);
            }
        });

        return listview;
    }

    interface CharacterListInterface{
        void removeCharacter(int index);
    }
}