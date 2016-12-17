package com.czipperz.ch4n;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by czipperz on 12/3/16.
 */
public class HideReplyListener implements View.OnClickListener {
    private boolean hidden = false;

    @Override
    public void onClick(View view) {
        LinearLayout layout = (LinearLayout) ((LinearLayout) view).getChildAt(0);
        if (hidden) {
            for (int i = 1; i < layout.getChildCount(); ++i) {
                layout.getChildAt(i).setVisibility(View.VISIBLE);
            }
        } else {
            for (int i = 1; i < layout.getChildCount(); ++i) {
                layout.getChildAt(i).setVisibility(View.GONE);
            }
        }
        hidden = !hidden;
    }
}
