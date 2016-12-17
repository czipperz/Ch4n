package com.czipperz.ch4n;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by czipperz on 12/3/16.
 */

public class DisplayPostListener implements View.OnClickListener {
    private Activity activity;
    private ChanPost post;

    public DisplayPostListener(Activity activity, ChanPost post) {
        this.activity = activity;
        this.post = post;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, DisplayPostActivity.class);
        intent.putExtra(MainActivity.CHAN, post);
        activity.startActivity(intent);
    }
}
