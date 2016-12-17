package com.czipperz.ch4n;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by czipperz on 11/28/16.
 */

public class ChanPost implements Serializable {
    public String chan;
    public String title;
    public ChanReply content;
    public ArrayList<ChanReply> replies;

    public ChanPost(String chan, String title, ChanReply content) {
        this.chan = chan;
        this.title = title;
        this.content = content;
        this.replies = new ArrayList<>();
    }
}
