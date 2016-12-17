package com.czipperz.ch4n;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayChanActivity extends AppCompatActivity implements Callback<JSONObject> /*, View.OnClickListener*/ {
    private String chan;
    private boolean inPopup = false;
    private PopupWindow popup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_chan);

        chan = getIntent().getStringExtra(MainActivity.CHAN);

        getSupportActionBar().setTitle(chan);

        new ReadWebpageTask(this).execute("http://a.4cdn.org" + chan + "1.json");
    }

    @Override
    public void useInput(JSONObject doc) {
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_chan);
        layout.removeAllViews();

        try {
            JSONArray array = doc.getJSONArray("threads");
            for (int i = 0; i != array.length(); ++i) {
                JSONObject obj = array.getJSONObject(i).getJSONArray("posts").getJSONObject(0);
                ChanPost post = parsePost(obj, chan);
                LinearLayout postView = layoutPost(this, post);
                postView.setOnClickListener(new DisplayPostListener(this, post));
                postView.setOnLongClickListener(new ShareLinkListener(this, post.chan, post.content.num, 0));
                layout.addView(postView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: add message
            return;
        }
    }

    private ChanPost parsePost(JSONObject thread, String chan) throws JSONException {
        return new ChanPost(chan, Html.fromHtml(thread.optString("sub", "")).toString(),
                new ChanReply(thread.getInt("no"), thread.optString("com", ""),
                        "http://i.4cdn.org" + chan + thread.getString("tim") + thread.getString("ext")));
    }

    public static LinearLayout layoutPost(Activity a, ChanPost post) {
        LinearLayout layout = new LinearLayout(a);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.color.colorPrimary);
        layout.setPadding(10, 0, 10, 0);

        TextView numView = new TextView(a);
        numView.setTextSize(20);
        numView.setTextColor(a.getResources().getColor(R.color.colorNum));
        numView.setText(Integer.toString(post.content.num));
        layout.addView(numView);

        ImageView imageView = new ImageView(a);
        new DownloadImageTask(imageView).execute(post.content.image);
//        imageView.setOnClickListener((View.OnClickListener) a);
        layout.addView(imageView);

        if (!post.title.isEmpty()) {
            TextView titleView = new TextView(a);
            titleView.setTextColor(a.getResources().getColor(R.color.colorAccent));
            titleView.setTextSize(20);
            titleView.setText(post.title);
            layout.addView(titleView);
        }

        ArrayList<TextView> postViews = Helper.layoutPost(a,
                post.content.content, reply.content, "http://boards.4chan.org" + post.chan,
                a.getResources().getColor(R.color.colorText),
                a.getResources().getColor(R.color.colorNameQuote),
                a.getResources().getColor(R.color.colorNameMention));
        for (TextView postView : postViews) {
            layout.addView(postView);
        }

        LinearLayout wrapper = new LinearLayout(a);
        wrapper.addView(layout);
        wrapper.setPadding(0, 20, 0, 20);
        return wrapper;
    }
//
//    @Override
//    public void onBackPressed() {
//        if (inPopup) {
//            popup.dismiss();
//            inPopup = false;
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        popup = Helper.displayImageFullscreen(this, (ImageView) view);
//        inPopup = true;
//    }
}
