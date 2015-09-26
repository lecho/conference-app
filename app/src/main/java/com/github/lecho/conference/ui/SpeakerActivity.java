package com.github.lecho.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.lecho.conference.R;

public class SpeakerActivity extends AppCompatActivity {

    private static final String TAG = SpeakerActivity.class.getSimpleName();
    private static final String ARG_SPEAKER_KEY = "speaker-key";

    public static void startActivity(Context context, String speakerKey) {
        Intent intent = new Intent(context, SpeakerActivity.class);
        intent.putExtra(ARG_SPEAKER_KEY, speakerKey);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
