package edu.temple.dmhelper.Warhorn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.temple.dmhelper.R;
import edu.temple.dmhelper.SessionsQuery;

public class EventInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Event Fragment";
    private static final String EVENTS_KEY = "events";
    private static final String MESSAGE_URL_KEY = "URL";

    Handler PictureLoadingHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(sessionViews.size() < msg.arg1)
                return false;
            Bitmap picture = (Bitmap) msg.obj;
            View sessionView = sessionViews.get(msg.arg1);
            ImageView coverArt = sessionView.findViewById(R.id.CoverArt);
            coverArt.setImageBitmap(picture);
            return false;
        }
    });

    GraphQLListener mListener;

    ArrayList<String> events;
    Spinner currentEvent;
    SpinnerAdapter spinnerAdapter;
    LinearLayout sessionList;
    ArrayList<View> sessionViews;
    List<SessionsQuery.Node> currentSessions;
    String currentEventName;

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
        view.findViewById(R.id.RemoveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.removeEvent(currentEventName);
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
        //Set up/clean up our global variables before we start adding new sessions
        sessionList.removeAllViews();
        ArrayList<String> urls = new ArrayList<>();
        sessionViews = new ArrayList<>();
        currentSessions = sessions;

        Session session;
        String coverArtUrl;
        View sessionView;
        for(int i = 0; i < sessions.size(); i++){
            //Log.d(TAG, "Adding view");
            session = new Session(sessions.get(i));
            //Create View for each session
            sessionView = createSessionView(session);
            //Add image view and url to our map, which will be used to download picture in seperate thread
            coverArtUrl = session.pictureURL;
            if(coverArtUrl != null && coverArtUrl.length() > 0) {
                urls.add(coverArtUrl);
                sessionViews.add(sessionView);
            }else{
                ((ImageView)sessionView.findViewById(R.id.CoverArt)).setImageResource(R.drawable.ddlogo);
            }
            //Add created view to our linear layout in the scroll view
            sessionList.addView(sessionView);
        }
        //Thread that will go through and add all of the cover art pictures to our sessions
        new ImageDownloadThread(urls, PictureLoadingHandler).start();
    }

    private View createSessionView(Session session){

        //Create View from layout file
        ConstraintLayout sessionView = (ConstraintLayout) getActivity().getLayoutInflater().inflate(R.layout.session, sessionList, false);

        //Set text to display information about the session
        ((TextView)sessionView.findViewById(R.id.CampaignName)).setText(session.campaign);
        ((TextView)sessionView.findViewById(R.id.ScenarioName)).setText(session.scenario);
        ((TextView)sessionView.findViewById(R.id.PlayerSeats)).setText(session.playerSeats);
        ((TextView)sessionView.findViewById(R.id.GMSeats)).setText(session.gmSeats);
        ((TextView)sessionView.findViewById(R.id.Time)).setText(session.time);

        //Add on click listener to details button
        sessionView.findViewById(R.id.viewSession).setOnClickListener(new SessionClickedListener(session));

        //Return the newly made view
        return sessionView;
    }

    //Method that gets called when item is selected in spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, events.get(i) + " was selected");
        currentEventName = events.get(i);
        mListener.querySessions(currentEventName);
    }

    public void removeEvent(String eventName){
        events.remove(eventName);
        spinnerAdapter.notifyDataSetChanged();
        currentEvent.setSelection(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    public interface GraphQLListener{
        void startEventDialogue();
        void querySessions(String eventName);
        void viewSessionDetails(Session session);
        void removeEvent(String eventName);
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
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) getView(position, convertView, parent);
            view.setTextSize(30);
            return view;
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
            toReturn.setTextSize(20);
            return toReturn;
        }
    }

    private class SessionClickedListener implements View.OnClickListener {
        Session session;

        public SessionClickedListener(Session session){
            this.session = session;
        }
        @Override
        public void onClick(View view) {
            mListener.viewSessionDetails(session);
        }
    }
}