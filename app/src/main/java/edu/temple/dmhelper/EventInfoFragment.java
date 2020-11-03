package edu.temple.dmhelper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

public class EventInfoFragment extends Fragment {
    private static final String TAG = "Event Fragment";

    ApolloClient apolloClient;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.graphql_end_point))
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
        view.findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query("indy-dnd");
            }
        });
        return view;
    }

    private void query(String slug){
        apolloClient.query(new EventTitleQuery(slug))
                .enqueue(new ApolloCall.Callback<EventTitleQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<EventTitleQuery.Data> response) {
                        Log.d("Apollo", response.getData().toString());
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("Apollo", "Error", e);
                    }
                });
    }
}