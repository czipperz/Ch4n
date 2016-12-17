package com.czipperz.ch4n;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Ch4n");
    }

    public final static String EXTRA_MESSAGE = "com.czipperz.first.MESSAGE";
    public final static String CHAN = "com.czipperz.first.CHAN";
    public final static String IMAGE = "com.czipperz.first.IMAGE";

    public void readChan(View view) {
        EditText chanInput = (EditText) findViewById(R.id.chan_input);
        String chan = chanInput.getText().toString();
        if (!chan.isEmpty() && '/' != chan.charAt(0)) {
            chan = '/' + chan;
        }
        if (chan.isEmpty() || '/' != chan.charAt(chan.length() - 1)) {
            chan += '/';
        }
        if (!chan.isEmpty()) {
            Intent displayChan = new Intent(this, DisplayChanActivity.class);
            displayChan.putExtra(CHAN, chan);
            startActivity(displayChan);
        }
    }
}
