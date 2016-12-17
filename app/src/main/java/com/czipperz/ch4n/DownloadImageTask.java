package com.czipperz.ch4n;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by czipperz on 11/29/16.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView view;

    public DownloadImageTask(ImageView view) { this.view = view; }

    protected Bitmap doInBackground(String... urls) {
        try {
            return BitmapFactory.decodeStream(new URL(urls[0]).openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Bitmap result) {
        view.setImageBitmap(result);
    }
}
