package com.github.lecho.mobilization.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.apimodel.AgendaItem;
import com.github.lecho.mobilization.apimodel.ApiData;
import com.github.lecho.mobilization.apimodel.ApiFacade;
import com.github.lecho.mobilization.apimodel.BreakApiDto;
import com.github.lecho.mobilization.apimodel.SlotApiDto;
import com.github.lecho.mobilization.apimodel.TalkApiDto;
import com.github.lecho.mobilization.realmmodel.RealmFacade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Menu navigationViewMenu = navigationView.getMenu();

        if (null == savedInstanceState) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_container, MyAgendaFragment.newInstance()).commit();
        }

        ApiData apiData = ApiFacade.parseJsonsFromAssets(this, "test-data");

        RealmFacade facade = new RealmFacade(this);
        facade.saveApiData(apiData);

    }

//    public static String readFileFromAsstes(String fileName, Context context) {
//        StringBuilder returnString = new StringBuilder();
//        InputStream fIn = null;
//        InputStreamReader isr = null;
//        BufferedReader input = null;
//        try {
//            fIn = context.getAssets().open(fileName, Context.MODE_PRIVATE);
//            isr = new InputStreamReader(fIn);
//            input = new BufferedReader(isr);
//            String line = "";
//            while ((line = input.readLine()) != null) {
//                returnString.append(line);
//            }
//        } catch (Exception e) {
//            e.getMessage();
//        } finally {
//            try {
//                if (isr != null)
//                    isr.close();
//                if (fIn != null)
//                    fIn.close();
//                if (input != null)
//                    input.close();
//            } catch (Exception e2) {
//                e2.getMessage();
//            }
//        }
//        return returnString.toString();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, TalkActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings2) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_container, VenueAgendaFragment.newInstance())
                    .commit();
            return true;
        } else if (id == R.id.action_settings3) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_container, SponsorsFragment.newInstance()).commit();
            return true;
        } else if (id == R.id.action_settings4) {
            Intent intent = new Intent(this, SpeakerActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
