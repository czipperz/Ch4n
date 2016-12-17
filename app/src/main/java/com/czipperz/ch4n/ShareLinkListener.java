package com.czipperz.ch4n;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by czipperz on 12/3/16.
 */
public class ShareLinkListener implements View.OnLongClickListener {
    private Activity activity;
    private String chan;
    private int post;
    private int reply;

    public ShareLinkListener(Activity activity, String chan, int post, int reply) {
        this.activity = activity;
        this.chan = chan;
        this.post = post;
        this.reply = reply;
    }

    @Override
    public boolean onLongClick(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String subject = chan + post;
        if (reply != 0) {
            subject += " reply " + reply;
        }
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        String link = "http://boards.4chan.org" + chan + "thread/" + post;
        if (reply != 0) {
            link += "#p" + reply;
        }
        sharingIntent.putExtra(Intent.EXTRA_TEXT, link);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        return true;
    }
}
