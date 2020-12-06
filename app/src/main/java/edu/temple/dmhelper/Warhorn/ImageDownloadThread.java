package edu.temple.dmhelper.Warhorn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ImageDownloadThread extends Thread{
    private static final String TAG = "Image Download Thread";
    ArrayList<String> urls;
    Handler handler;

    public ImageDownloadThread(ArrayList<String> urls, Handler handler){
        this.urls = urls;
        this.handler = handler;
    }

    @Override
    public void run() {
        for(int i = 0; i < urls.size(); i++){
            try {
                URL url = new URL(urls.get(i));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Message msg = Message.obtain();
                msg.obj = bmp;
                msg.arg1 = i;
                handler.sendMessage(msg);
            }catch (MalformedURLException e) {
                Log.d(TAG, "Picture URL is invalid");
            } catch (IOException e) {
                Log.d(TAG, "Unable to open stream from picture URL");
            }
        }
    }
}
