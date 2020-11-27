package edu.temple.dmhelper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitiativeTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitiativeTrackerFragment extends Fragment implements CharacterListFragment.CharacterListInterface{


    private CharacterList CHARACTER_LIST = new CharacterList();
    private static final String CHARACTER_LIST_KEY = "characterlist";
    EditText nameEditText;
    EditText initiativeEditText;


    public InitiativeTrackerFragment() {
        // Required empty public constructor
    }


    public static InitiativeTrackerFragment newInstance() {
        InitiativeTrackerFragment fragment = new InitiativeTrackerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CHARACTER_LIST = (CharacterList) getArguments().getSerializable(CHARACTER_LIST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_initiative_tracker, container, false);

        nameEditText = v.findViewById(R.id.nameEditText);
        initiativeEditText = v.findViewById(R.id.initiativeEditText);

        v.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHARACTER_LIST.add(new Character(nameEditText.getText().toString(), Integer.parseInt(initiativeEditText.getText().toString())));
                Fragment characterListFragment = CharacterListFragment.newInstance(CHARACTER_LIST);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.characterListContainer, characterListFragment).commit();
            }
        });

        v.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHARACTER_LIST.nextTurn();
                Fragment characterListFragment = CharacterListFragment.newInstance(CHARACTER_LIST);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.characterListContainer, characterListFragment).commit();
            }
        });

        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Fragment characterListFragment = new CharacterListFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.characterListContainer, characterListFragment).commit();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void removeCharacter(int index) {
        CHARACTER_LIST.remove(index);
        Fragment characterListFragment = CharacterListFragment.newInstance(CHARACTER_LIST);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.characterListContainer, characterListFragment).commit();
    }
}