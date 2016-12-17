package com.czipperz.ch4n;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by czipperz on 12/6/16.
 */
public class ReplyLinkClicker extends LinkMovementMethod {
    private ChanPost post;

    public ReplyLinkClicker(ChanPost post) {
        this.post = post;
    }

    @Override
    public boolean onTouchEvent(TextView view, Spannable buffer, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX() - view.getTotalPaddingLeft() + view.getScrollX();
            int y = (int) event.getY() - view.getTotalPaddingTop() + view.getScrollY();

            Layout layout = view.getLayout();
            int line = layout.getLineForVertical(y);
            int offset = layout.getOffsetForHorizontal(line, x);

            Object[] spans = buffer.getSpans(offset, offset, Object.class);
            if (spans.length != 0) {
                Object span = spans[0];
                if (span instanceof URLSpan) {
                    ((URLSpan) span).onClick(view);
                    return true;
                } else if (span instanceof ReplyLink) {
                    int replyNum = ((ReplyLink) span).replyNum;
                    for (ChanReply reply : post.replies) {
                        if (reply.num == replyNum) {
                            System.out.println("Reply " + replyNum + ": " + reply.content);
                            return true;
                        }
                    }
                }
            }
        }

        return super.onTouchEvent(view, buffer, event);
    }
}
