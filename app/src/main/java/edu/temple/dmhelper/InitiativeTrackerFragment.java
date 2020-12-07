package edu.temple.dmhelper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitiativeTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitiativeTrackerFragment extends Fragment implements CharacterListFragment.CharacterListInterface{


    private CharacterList CHARACTER_LIST;
    private static final String CHARACTER_LIST_KEY = "characterlist";
    EditText nameEditText;
    EditText initiativeEditText;

    private ActionInterface actionInterface;


    public InitiativeTrackerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ActionInterface) {
            actionInterface = (ActionInterface) context;
            if (actionInterface.isDm())
                actionInterface.setDiscoverable(true);
        } else {
            throw new RuntimeException(context.getClass().getName() + " must implement ActionInterface.");
        }
    }

    public static InitiativeTrackerFragment newInstance(CharacterList characterList) {
        InitiativeTrackerFragment fragment = new InitiativeTrackerFragment();
        Bundle args = new Bundle();
        args.putSerializable(CHARACTER_LIST_KEY, characterList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        if (actionInterface.isDm())
            v.findViewById(R.id.dmControls).setVisibility(View.VISIBLE);

        v.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().length() == 0 || initiativeEditText.getText().toString().length() == 0) return;

                Character character = new Character(nameEditText.getText().toString(), Integer.parseInt(initiativeEditText.getText().toString()));
                actionInterface.addCharacter(character);
                CHARACTER_LIST.add(character);
                updateCharacterList();
            }
        });

        v.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextRound();
            }
        });

        v.findViewById(R.id.endGameButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionInterface.endGame();
            }
        });

        return  v;
    }

    public void handleNextRound() {
        CHARACTER_LIST.nextTurn();
        if (actionInterface.isDm())
            actionInterface.sendNextRound();
        this.updateCharacterList();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.updateCharacterList();
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateCharacterList() {
        Fragment characterListFragment = CharacterListFragment.newInstance(CHARACTER_LIST);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.characterListContainer, characterListFragment).commit();
    }

    @Override
    public void removeCharacter(int index) {
        Character character = CHARACTER_LIST.remove(index);
        if (actionInterface.isDm())
            actionInterface.removeCharacter(character);
        this.updateCharacterList();
    }

    public void removeCharacter(Character character) {
        CHARACTER_LIST.remove(character);
        this.updateCharacterList();
    }

    public void addCharacter(Character player) {
        CHARACTER_LIST.add(player);
        this.updateCharacterList();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.initiative_tracker_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.diceRollerMenuItem) {
            actionInterface.showDiceRoller();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}