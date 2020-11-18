package edu.temple.dmhelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddEventDialogue extends DialogFragment {
    EventAdder myActivity;

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
