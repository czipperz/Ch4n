package com.czipperz.ch4n;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by czipperz on 11/28/16.
 */

public final class Helper {
    private Helper() {}

    public static PopupWindow displayImageFullscreen(AppCompatActivity context, ImageView view) {
        PopupWindow popup = new PopupWindow(context);
        popup.setWidth(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        popup.setHeight(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout layout = new LinearLayout(context);
        ImageView image = new ImageView(context);
        image.setImageDrawable(view.getDrawable());
        layout.addView(image);

        popup.setContentView(layout);
        context.setContentView(layout);
        return popup;
    }

    public static String decodeHtml(String html, String uri) {
        String result = "";
        Element doc = Jsoup.parseBodyFragment(html, uri).body();
        for (Node node : doc.childNodes()) {
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.tagName().equals("br")) {
                    result += '\n';
                } else if (element.tagName().equals("a")) {
                    result += element.text();
                } else if (element.tagName().equals("span")) {
                    result += element.text();
                }
            } else if (node instanceof TextNode) {
                TextNode text = (TextNode) node;
                result += text.getWholeText();
            }
        }
        return result;
    }

    public static ArrayList<TextView> layoutPost(Context parent, ChanPost post, String text,
                                                 String uri, int colorText, int colorTextQuote,
                                                 int colorTextMention) {
        text = decodeHtml(text, uri);
        ArrayList<TextView> views = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(text));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                int color = colorText;
                if (!line.isEmpty() && line.charAt(0) == '>') {
                    color = colorTextQuote;
                    if (line.length() >= 2 && line.charAt(1) == '>') {
                        color = colorTextMention;
                    }
                }

                TextView postView = new TextView(parent);
                postView.setMovementMethod(new ReplyLinkClicker(post));
                postView.setTextColor(color);
                postView.setTextSize(20);
                postView.setText(line);
                views.add(postView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return views;
    }
}
