package edu.temple.dmhelper.Warhorn;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import edu.temple.dmhelper.R;
import edu.temple.dmhelper.SessionsQuery;

public class SessionDetailsFragment extends Fragment {
    private static final String SESSION_KEY = "session";
    private Session session;

    SessionDetailsListener mListener;

    public SessionDetailsFragment() {
        // Required empty public constructor
    }

    public static SessionDetailsFragment newInstance(Session session) {
        SessionDetailsFragment fragment = new SessionDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(SESSION_KEY, (Serializable) session);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SessionDetailsListener){
            mListener = (SessionDetailsListener) context;
        }else{
            throw new RuntimeException(context.toString() +
                    " must implement SessionDetailsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            session = (Session) getArguments().getSerializable(SESSION_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session_details, container, false);
        //Create all of our text fields as strings
        String campaignName, scenarioName, availablePlayerSeats, availableGMSeats, time, notes, blurb;

        //Set text to display information about the session
        ((TextView)view.findViewById(R.id.CampaignName)).setText(session.campaign);
        ((TextView)view.findViewById(R.id.ScenarioName)).setText(session.scenario);
        ((TextView)view.findViewById(R.id.PlayerSeats)).setText(session.playerSeats);
        ((TextView)view.findViewById(R.id.GMSeats)).setText(session.gmSeats);
        ((TextView)view.findViewById(R.id.Time)).setText(session.time);

        if(session.notes != null){
            ((TextView)view.findViewById(R.id.notes)).setText(session.notes);
        }else{
            //No notes to display, so make textview not visible
            view.findViewById(R.id.notes).setVisibility(View.GONE);
        }

        if(session.blurb != null){
            ((TextView)view.findViewById(R.id.blurb)).setText(session.blurb);
        }else{
            //No blurb to display, so make textview not visible
            view.findViewById(R.id.blurb).setVisibility(View.GONE);
        }

        view.findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.exitSessionDetails();
            }
        });

        return view;
    }

    public interface SessionDetailsListener{
        void exitSessionDetails();
    }
}