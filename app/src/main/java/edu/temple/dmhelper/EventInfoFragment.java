package edu.temple.dmhelper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventInfoFragment extends Fragment {
    private static final String TAG = "Event Fragment";
    GraphQLListener mListener;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(!(context instanceof GraphQLListener)){
            throw new RuntimeException(context.toString()
                    + " must implement GraphQLListener");
        }else{
            mListener = (GraphQLListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
        view.findViewById(R.id.AddButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.startEventDialogue();
            }
        });
        return view;
    }

    public interface GraphQLListener{
        void startEventDialogue();
        void query(String slug);
    }
}