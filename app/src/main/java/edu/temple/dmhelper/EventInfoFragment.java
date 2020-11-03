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

import net.openid.appauth.AuthState;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class EventInfoFragment extends Fragment {
    private static final String TAG = "Event Fragment";

    private static final String ACCESS_TOKEN = "token";

    ApolloClient apolloClient; //Used to make GraphQL queries and mutations to warhorn
    String accessToken; //Authentication token for warhorn

    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment NewInstance(String accessToken){
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        args.putString(ACCESS_TOKEN, accessToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Adds authorization interceptor to apollo client
        OkHttpClient.Builder authBuilder = new OkHttpClient.Builder();
        OkHttpClient authClient = authBuilder
                .addInterceptor(new AuthorizationInterceptor())
                .build();

        //Initalizes the apollo client with warhorn's graphql endpoint
        apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.graphql_end_point))
                .okHttpClient(authClient)
                .build();
        if(getArguments() != null){
            accessToken = getArguments().getString(ACCESS_TOKEN);
        }else{
            Log.e(TAG, "Fragment requires Authentication to operate");
        }
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

    //Class used to add authorization to each graphql query/mutation
    private class AuthorizationInterceptor implements Interceptor{
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            //Adds authentication to our queries/mutations
            Request request = chain.request().newBuilder()
                    .addHeader("Authorization", "bearer " + accessToken)
                    .build();

            return chain.proceed(request);
        }
    }
}