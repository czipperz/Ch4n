package com.czipperz.ch4n;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayPostActivity extends AppCompatActivity implements Callback<JSONObject> /*, View.OnClickListener*/ {
    private ChanPost post;
    private boolean inPopup = false;
    private PopupWindow popup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

        post = (ChanPost) getIntent().getSerializableExtra(MainActivity.CHAN);

        {
            String title = post.title;
            if (!title.isEmpty()) {
                title += " -- ";
            }
            title += post.content.num;
            getSupportActionBar().setTitle(title);
        }

        {
            LinearLayout layout = (LinearLayout) findViewById(R.id.activity_display_post);
            layout.removeAllViews();

            LinearLayout postView = DisplayChanActivity.layoutPost(this, post);
            postView.setOnLongClickListener(new ShareLinkListener(this, post.chan, post.content.num, 0));
            layout.addView(postView);
        }

        new ReadWebpageTask(this).execute("http://a.4cdn.org" + post.chan + "thread/" +
                post.content.num + ".json");
    }

    @Override
    public void useInput(JSONObject doc) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_display_post);
        try {
            JSONArray array = doc.getJSONArray("posts");
            for (int i = 1; i != array.length(); ++i) {
                JSONObject obj = array.getJSONObject(i);
                ChanReply reply = parseComment(obj, post.chan);
                LinearLayout replyView = layoutComment(reply);
                replyView.setOnClickListener(new HideReplyListener());
                replyView.setOnLongClickListener(
                        new ShareLinkListener(this, post.chan, post.content.num, reply.num));
                layout.addView(replyView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: add message
            return;
        }
    }

    private ChanReply parseComment(JSONObject comment, String chan) throws JSONException {
        String image = comment.optString("tim");
        if (image != null) {
            image = "http://i.4cdn.org" + chan + image + comment.optString("ext");
        }
        return new ChanReply(comment.getInt("no"), comment.optString("com", ""), image);
    }

    private LinearLayout layoutComment(ChanReply reply) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.color.colorPrimary);
        layout.setPadding(10, 0, 10, 0);

        TextView numView = new TextView(this);
        numView.setTextSize(20);
        numView.setTextColor(getResources().getColor(R.color.colorNum));
        numView.setText(Integer.toString(reply.num));
        layout.addView(numView);

        if (reply.image != null) {
            ImageView imageView = new ImageView(this);
            new DownloadImageTask(imageView).execute(reply.image);
//            imageView.setOnClickListener(this);
            layout.addView(imageView);
        }

        ArrayList<TextView> postViews = Helper.layoutPost(this,
                post, reply.content, "http://boards.4chan.org" + post.chan,
                getResources().getColor(R.color.colorText),
                getResources().getColor(R.color.colorNameQuote),
                getResources().getColor(R.color.colorNameMention));
        for (TextView postView : postViews) {
            layout.addView(postView);
        }

        LinearLayout wrapper = new LinearLayout(this);
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
