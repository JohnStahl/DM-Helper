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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class EventInfoFragment extends Fragment {
    private static final String TAG = "Event Fragment";
    private static final String EVENTS_KEY = "events";
    GraphQLListener mListener;

    ArrayList<String> events;
    Spinner currentEvent;
    SpinnerAdapter spinnerAdapter;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment newInstance(String[] events){
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(EVENTS_KEY, new ArrayList<String>(Arrays.asList(events)));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            events = getArguments().getStringArrayList(EVENTS_KEY);
        }
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
        currentEvent = (Spinner) view.findViewById(R.id.CurrentEvent);
        spinnerAdapter = new SpinnerAdapter(getContext(), events);
        currentEvent.setAdapter(spinnerAdapter);
        return view;
    }

    public void updateEventSpinner(String eventName){
        spinnerAdapter.addEvent(eventName);
        spinnerAdapter.notifyDataSetChanged();
    }

    public interface GraphQLListener{
        void startEventDialogue();
        void query(String slug);
    }

    public class SpinnerAdapter extends BaseAdapter {
        ArrayList<String> events;
        Context context;

        public SpinnerAdapter(Context context, ArrayList<String> events){
            this.context = context;
            this.events = events;
        }

        public void addEvent(String eventName){
            events.add(eventName);
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int i) {
            return events.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView toReturn;
            if(!(view instanceof TextView)) {
                toReturn = new TextView(context);
            }else{
                toReturn = (TextView)view;
            }
            toReturn.setText(events.get(i));
            return toReturn;
        }
    }
}