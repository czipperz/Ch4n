package com.czipperz.ch4n;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by czipperz on 11/28/16.
 */

public class ChanReply implements Serializable {
    public int num;
    public String content;
    public String image;
    public ArrayList<ChanReply> replies;

    public ChanReply(int num, String content, String image) {
        this.num = num;
        this.content = content;
        this.image = image;
        this.replies = new ArrayList<>();
    }
}
