package edu.temple.dmhelper.Warhorn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.exception.ApolloException;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.temple.dmhelper.AuthManager;
import edu.temple.dmhelper.EventTitleQuery;
import edu.temple.dmhelper.R;
import edu.temple.dmhelper.SessionsQuery;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


public class WarhornActivity extends AppCompatActivity implements EventInfoFragment.GraphQLListener,
        AddEventDialogue.EventAdder {
    Handler EventTitleHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String contents[] = (String[]) msg.obj;
            if(!myEvents.containsKey(contents[0])) {
                myEvents.put(contents[0], contents[1]);
            }
            ((EventInfoFragment)EventInfo).updateEventSpinner(contents[0]);
            //Log.d(TAG, myEvents.toString());
            return false;
        }
    });

    Handler DisplaySessionsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            List<SessionsQuery.Node> sessions = (List<SessionsQuery.Node>)message.getData().get("Sessions");
            ((EventInfoFragment)EventInfo).displaySessions(sessions);
            return false;
        }
    });

    Handler AddEventAgainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            showEventDialogue(true);
            return false;
        }
    });

    Map<String, String> myEvents;

    public static final String TAG = "Warhorn Activity";
    AuthState authState;
    AuthorizationService authService;
    ApolloClient apolloClient;

    Fragment EventInfo;
    Fragment UserInfo;
    AlertDialog RemoveEventDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warhorn);

        EventInfo = getSupportFragmentManager().findFragmentById(R.id.Event_Info);
        if(!(EventInfo instanceof EventInfoFragment)){
            getMyEvents();
            String[] eventNames = getEventNames();
            EventInfo = EventInfoFragment.newInstance(eventNames);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.Event_Info ,EventInfo)
                    .commit();
        }
    }

    private String[] getEventNames(){
        Object[] objectEventNames = myEvents.keySet().toArray();
        String[] stringEventNames = new String[objectEventNames.length];
        for(int i = 0; i < objectEventNames.length; i++){
            stringEventNames[i] = objectEventNames[i].toString();
        }
        return stringEventNames;
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            authState = AuthManager.readAuthState(this);
        } catch (JSONException e) {
            Log.d(TAG, "Auth State formatted incorrectly");
        }

        if(authState == null){
            handleIntent(getIntent());
        }else{
            //Log.d(TAG, "Using predefined authorization");
            authService = new AuthorizationService(this);
            initializeApolloClient();
            if(!(UserInfo instanceof UserInfoFragment)){
                getUserInfo();
            }
        }
        if(myEvents == null)
            getMyEvents();
    }

    public void getMyEvents() {
        File file = new File(getFilesDir(), getString(R.string.EventsFile));
        if(file.exists() && file.length() > 0){
            try {
                String path = file.getPath();
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
                myEvents = (Map<String, String>) inputStream.readObject();
                inputStream.close();
            } catch (IOException e){
                file.delete();
            } catch (ClassNotFoundException e){
                Log.e(TAG, "Unknown class in Events file");
            }
        }else{
            myEvents = new HashMap<>();
        }
    }

    public void writeMyEvents(){
        File file = new File(getFilesDir(), getString(R.string.EventsFile));
        try {
            String path = file.getPath();
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
            outputStream.writeObject(myEvents);
            outputStream.close();
        } catch (IOException e){
            file.delete();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AuthManager.writeAuthState(this, authState);
        authState = null;
        authService.dispose();
        writeMyEvents();
    }

    //Extracts Authorization response from intent and sends to getUserToken()
    public void handleIntent(Intent intent){
        if(intent.getAction().equals((Intent.ACTION_VIEW))){
            authState = new AuthState();
            authService = new AuthorizationService(this);
            //Log.d(TAG, intent.getData().toString());
            AuthorizationResponse.Builder builder = new AuthorizationResponse.Builder(AuthManager.generateRequest(this));
            AuthorizationResponse response = builder.fromUri(intent.getData()).build();
            if(response == null){
                AuthorizationException exception = AuthorizationException.fromOAuthRedirect(intent.getData());
                if(exception == null)
                    Log.d(TAG, "Response from server is null");
                else
                    Log.e(TAG, "Registration failed with following error: " + exception.toString());
            }else{
                authState.update(response, null);
                getUserToken(response);
            }
        }
    }

    //Grabs user token and stores it in authState from successful authorization response
    //Then calls getUserInfo() to use acquired token to obtain relevant user information
    public void getUserToken(AuthorizationResponse response){
        authService.performTokenRequest(
                response.createTokenExchangeRequest(),
                new AuthorizationService.TokenResponseCallback() {
                    @Override public void onTokenRequestCompleted(TokenResponse token, AuthorizationException ex) {
                        if (token != null) {
                            // token obtained
                            //Log.d(TAG, token.jsonSerializeString());
                            authState.update(token, ex);
                            //Log.d(TAG, "Obtained following access token: " + authState.getAccessToken());
                            //Log.d(TAG, "Obtained id token: " + authState.getIdToken());
                            initializeApolloClient(); //Create apollo client now that we can authenticate
                            getUserInfo(); //Grab User Info now that we can authenticate
                        } else {
                            // authorization failed
                            Log.e(TAG, "Token exchanged failed with following error: " + ex.toString());
                        }
                    }
                });
    }

    //Using obtained access token requests user info from warhorn and updates UI with response
    public void getUserInfo(){
        authState.performActionWithFreshTokens(authService, new AuthState.AuthStateAction() {
            @Override
            public void execute(@Nullable final String accessToken, @Nullable final String idToken,
                                @Nullable AuthorizationException ex) {
                String url = getString(R.string.user_info_end_point);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Successful request and we should update UI with newly obtained user info
                        //Log.d(TAG, "Got response from warhorn");
                        updateUI(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Unsuccessful request, so we log the error
                        Log.e(TAG, "Failed to get user info because: " + error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        //Adds access token to request
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "bearer " + accessToken);
                        //Log.d(TAG, headers.toString());
                        return headers;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(WarhornActivity.this);
                queue.add(request);
            }
        });
    }

    //First pulls relevant information from user info response
    //Then creates new user info fragment with relevant information
    private void updateUI(JSONObject userInfo){
        try {
            //Creates User Info Fragment
            String name = userInfo.getString("name");
            String email = userInfo.getString("email");
            String pictureURL = userInfo.getString("picture");
            UserInfo = UserInfoFragment.newInstance(name, email, pictureURL);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.Profile_Info, UserInfo)
                    .commit();
        } catch (JSONException e) {
            Log.e(TAG, "Obtained user info object is invalid");
        }
    }

    //Creates ApolloClient for graphql queries and mutations
    private void initializeApolloClient(){
        //Log.d(TAG, "Making apollo client");
        //Adds authorization interceptor to apollo client
        OkHttpClient.Builder authBuilder = new OkHttpClient.Builder();
        OkHttpClient authClient = authBuilder
                .addInterceptor(new AuthorizationInterceptor(authState.getAccessToken()))
                .build();

        //Initalizes the apollo client with warhorn's graphql endpoint
        apolloClient = ApolloClient.builder()
                .serverUrl(getString(R.string.graphql_end_point))
                .okHttpClient(authClient)
                .build();
        //Log.d(TAG, "Made apollo client");
    }

    @Override
    public void startEventDialogue() {
        showEventDialogue(false);
    }

    private void showEventDialogue(boolean error){
        DialogFragment eventDialogue = AddEventDialogue.newInstance(error);
        eventDialogue.show(getSupportFragmentManager(), "add event");
    }

    @Override
    public void querySessions(String eventName){
        if(apolloClient == null){
            Log.d("Apollo", "Unable to query at this time");
            return;
        }
        String slug = myEvents.get(eventName);
        //Get the current date to only show sessions that have not happened yet
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); // Quoted "Z" to indicate UTC, no timezone offset
        String now = df.format(new Date());

        Log.d("Apollo", "Attempting to query with this slug: " + slug);
        apolloClient.query(new SessionsQuery(slug, now))
                .enqueue(new ApolloCall.Callback<SessionsQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull com.apollographql.apollo.api.Response<SessionsQuery.Data> response) {
                        //Log.d("Apollo", response.getData().sessions.nodes.toArray()[0].toString());
                        List<SessionsQuery.Node> sessions = response.getData().sessions().nodes();
                        Message msg = Message.obtain();
                        Bundle data = new Bundle();
                        data.putSerializable("Sessions", (Serializable) sessions);
                        msg.setData(data);
                        DisplaySessionsHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        //Log.e("Apollo", "Error", e);
                        showEventDialogue(true);
                    }
                });
    }

    @Override
    public void viewSessionDetails(Session session) {
        String url = session.signupURL;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void removeEvent(final String eventName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete " + eventName + "?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myEvents.remove(eventName);
                        ((EventInfoFragment)EventInfo).removeEvent(eventName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RemoveEventDialogue.dismiss();
                    }
                });
        RemoveEventDialogue = builder.create();
        RemoveEventDialogue.show();
    }

    private void getEventName(final String slug){
        if(apolloClient == null){
            Log.d(TAG, "Unable to query at this time");
            return;
        }
        apolloClient.query(new EventTitleQuery(slug))
                .enqueue(new ApolloCall.Callback<EventTitleQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull com.apollographql.apollo.api.Response<EventTitleQuery.Data> response) {
                        //Log.d("Apollo", response.getData().toString());
                        Message msg = Message.obtain();
                        String messageContents[] = {response.getData().event().title(), slug};
                        msg.obj = messageContents;
                        EventTitleHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        //Log.e("Apollo", "Error", e);
                        AddEventAgainHandler.sendEmptyMessage(0);
                    }
                });
    }

    @Override
    public void addEvent(final String slug) {
        if(!myEvents.values().contains(slug)) {
            new Thread() {
                @Override
                public void run() {
                    getEventName(slug);
                }
            }.start();
        }
    }

    //Class used to add authorization to each graphql query/mutation
    private class AuthorizationInterceptor implements Interceptor {
        private String accessToken;

        public AuthorizationInterceptor(String accessToken){
            this.accessToken = accessToken;
        }
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            //Adds authentication to our queries/mutations
            okhttp3.Request request = chain.request().newBuilder()
                    .addHeader("Authorization", "bearer " + accessToken)
                    .build();

            return chain.proceed(request);
        }
    }
}