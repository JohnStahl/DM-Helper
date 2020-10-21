package edu.temple.dmhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationServiceConfiguration;

public class WarhornFragment extends Fragment {
    private static final String TAG = "WarhornFragment";

    WarhornInterface listener;

    public WarhornFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof WarhornInterface){
            listener = (WarhornInterface)context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement WarhornInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static WarhornFragment newInstance() {
        WarhornFragment fragment = new WarhornFragment();
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
        View view = inflater.inflate(R.layout.fragment_warhorn, container, false);
        // Adds listener to login button that starts login process when button is clicked
        view.findViewById(R.id.warhorn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.authorize();
            }
        });

        return view;
    }

    public interface WarhornInterface{
        void authorize();
    }
}