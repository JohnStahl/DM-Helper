package edu.temple.dmhelper.Warhorn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.temple.dmhelper.R;

public class SessionDetailsFragment extends Fragment {

    public SessionDetailsFragment() {
        // Required empty public constructor
    }

    public static SessionDetailsFragment newInstance() {
        SessionDetailsFragment fragment = new SessionDetailsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_details, container, false);
    }
}