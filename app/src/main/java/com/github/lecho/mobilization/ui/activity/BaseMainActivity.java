package com.github.lecho.mobilization.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.lecho.mobilization.async.JsonDataVersion;
import com.github.lecho.mobilization.ui.dialog.JsonUpdateDialogFragment;
import com.github.lecho.mobilization.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Leszek on 08.10.2016.
 */

public class BaseMainActivity extends AppCompatActivity {

    private static final String TAG = BaseMainActivity.class.getSimpleName();
    private DatabaseReference jsonDataRef;
    private JsonDataChangeListener jsonDataChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        jsonDataRef = firebaseDatabase.getReference().child(JsonDataVersion.JSON_NODE);
        jsonDataChangeListener = new JsonDataChangeListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        jsonDataRef.addValueEventListener(jsonDataChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        jsonDataRef.removeEventListener(jsonDataChangeListener);
    }

    private class JsonDataChangeListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            JsonDataVersion jsonDataVersion = dataSnapshot.getValue(JsonDataVersion.class);
            if (jsonDataVersion == null) {
                Log.w(TAG, "JsonDataVersion may have changed but it is now null");
                return;
            }
            if (Utils.checkIfEventShouldBeSkipped(getApplicationContext(), jsonDataVersion.version)) {
                return;
            }
            Utils.saveNextJsonDataVersion(getApplicationContext(), jsonDataVersion.version);
            if (Utils.checkIfJsonUpdateNeeded(getApplicationContext())) {
                Log.d(TAG, "JsonDataVersion changed to version: " + jsonDataVersion.version);
                JsonUpdateDialogFragment.show(BaseMainActivity.this);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "Error when updating JsonDataVersion" + databaseError.getMessage());
        }
    }
}
