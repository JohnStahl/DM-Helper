package edu.temple.dmhelper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Event Fragment";
    private static final String EVENTS_KEY = "events";
    GraphQLListener mListener;

    ArrayList<String> events;
    Spinner currentEvent;
    SpinnerAdapter spinnerAdapter;
    LinearLayout sessionList;

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
        currentEvent.setOnItemSelectedListener(this);
        sessionList = view.findViewById(R.id.Sessions).findViewById(R.id.SessionList);
        return view;
    }

    public void updateEventSpinner(String eventName){
        spinnerAdapter.addEvent(eventName);
        spinnerAdapter.notifyDataSetChanged();
    }

    public void displaySessions(List<SessionsQuery.Node> sessions){
        for(SessionsQuery.Node session : sessions){
            //Log.d(TAG, "Adding view");
            sessionList.addView(createSessionView(session));
        }
        Log.d(TAG, "Added all sessions");
    }

    private View createSessionView(SessionsQuery.Node session){
        //Create all of our text fields as strings
        String campaignName, scenarioName, availablePlayerSeats, availableGMSeats, time;

        //Following fields may be null; need to be checked before assignment
        if(session.scenarioOffering != null && session.scenarioOffering.scenario != null &&
                session.scenarioOffering.scenario.campaign != null &&
                session.scenarioOffering.scenario.campaign.name != null) {
            campaignName = "Campaign: " + session.scenarioOffering.scenario.campaign.name;
        }else {
            campaignName = "No campaign name given";
        }
        if(session.scenarioOffering.scenario.name != null)
            scenarioName = "Scenario: " + session.scenarioOffering.scenario.name;
        else
            scenarioName = "No Scenario name given";
        if(session.slot != null)
            time = session.slot.startsAt.toString() + "-" + session.slot.endsAt.toString();
        else
            time = "No time given";

        //Following fields will never be null
        availablePlayerSeats = "Player seats available: " + session.availablePlayerSeats;
        availableGMSeats = "GM seats available: " + session.availableGmSeats;

        //Create View from layout file
        ConstraintLayout sessionView = (ConstraintLayout) getActivity().getLayoutInflater().inflate(R.layout.session, sessionList, false);

        //Set text to display information about the session
        ((TextView)sessionView.findViewById(R.id.CampaignName)).setText(campaignName);
        ((TextView)sessionView.findViewById(R.id.ScenarioName)).setText(scenarioName);
        ((TextView)sessionView.findViewById(R.id.PlayerSeats)).setText(availablePlayerSeats);
        ((TextView)sessionView.findViewById(R.id.GMSeats)).setText(availableGMSeats);
        ((TextView)sessionView.findViewById(R.id.Time)).setText(time);

        //Return the newly made view
        return sessionView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, events.get(i) + " was selected");
        mListener.querySessions(events.get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    public interface GraphQLListener{
        void startEventDialogue();
        void querySessions(String eventName);
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