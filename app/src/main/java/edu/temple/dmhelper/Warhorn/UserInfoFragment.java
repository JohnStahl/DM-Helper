package edu.temple.dmhelper.Warhorn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.temple.dmhelper.R;

public class UserInfoFragment extends Fragment {

    Handler PictureLoadingHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bitmap picture = (Bitmap) msg.obj;
            profilePicture.setImageBitmap(picture);
            return false;
        }
    });

    private ImageView profilePicture;

    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String PICTURE = "Picture";

    private String name;
    private String email;
    private String pictureURL;

    public UserInfoFragment() {
        // Required empty public constructor
    }


    public static UserInfoFragment newInstance(String name, String email, String pictureURL) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(EMAIL, email);
        args.putString(PICTURE, pictureURL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(NAME);
            email = getArguments().getString(EMAIL);
            pictureURL = getArguments().getString(PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        ((TextView)view.findViewById(R.id.name)).setText(name);
        ((TextView)view.findViewById(R.id.email)).setText(email);

        profilePicture = (ImageView) view.findViewById(R.id.profile_pic);
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(pictureURL);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Message msg = Message.obtain();
                    msg.obj = bmp;
                    PictureLoadingHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    Log.d("User Info", "Picture URL is invalid");
                } catch (IOException e) {
                    Log.d("User Info", "Unable to open stream from picture URL");
                }
            }
        }.start();

        return view;
    }
}