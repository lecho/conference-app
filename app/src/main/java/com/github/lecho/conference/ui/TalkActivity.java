package com.github.lecho.conference.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.lecho.conference.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TalkActivity extends AppCompatActivity {

    private static final String ARG_TALK_KEY = "talk-key";
    private String talkKey;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //@Bind(R.id.button_add_to_my_agenda)
    //FloatingActionButton addToMyAgenda;

    public static void startActivity(@NonNull Context context, @NonNull String talkKey){
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(ARG_TALK_KEY, talkKey);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        talkKey = getIntent().getStringExtra(ARG_TALK_KEY);

        if (null == savedInstanceState) {
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.content_container, TalkFragment.newInstance()).commit();
        }


//        addToMyAgenda.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v instanceof FloatingActionButton) {
//                    FloatingActionButton bt = (FloatingActionButton) v;
//                    bt.setLineMorphingState((bt.getLineMorphingState() + 1) % 2, true);
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
