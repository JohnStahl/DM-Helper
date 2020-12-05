package edu.temple.dmhelper.Warhorn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.temple.dmhelper.R;

public class AddEventDialogue extends DialogFragment {
    public static final String ERROR_KEY = "error";
    EventAdder myActivity;
    boolean error;

    public static AddEventDialogue newInstance(boolean error){
        AddEventDialogue dialogue = new AddEventDialogue();
        Bundle args = new Bundle();
        args.putBoolean(ERROR_KEY, error);
        dialogue.setArguments(args);
        return dialogue;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            error = getArguments().getBoolean(ERROR_KEY);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof EventAdder){
            myActivity = (EventAdder) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement EventAdder");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogue = inflater.inflate(R.layout.event_dialogue, null);
        //Log.d("Event Dialogue", error + "");
        if(error) {
            //Present error message to user
            dialogue.findViewById(R.id.errorMessage).setVisibility(View.VISIBLE);
        }
        final TextView slugDescription = dialogue.findViewById(R.id.SlugInfo);
        dialogue.findViewById(R.id.informationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slugDescription.getVisibility() == View.GONE)
                    slugDescription.setVisibility(View.VISIBLE);
                else
                    slugDescription.setVisibility(View.GONE);
            }
        });
        final EditText slug = dialogue.findViewById(R.id.slug);
        builder.setView(dialogue)
                .setPositiveButton(R.string.AddEvent, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myActivity.addEvent(slug.getText().toString());
                    }
                })
                .setNegativeButton(R.string.CancelEvent, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddEventDialogue.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface EventAdder{
        void addEvent(String slug);
    }
}
