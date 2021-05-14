package com.gsrathoreniks.facefilter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.backBtn_profile_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Utils utils = new Utils();
//
        String name = getStoredString1(ProfileActivity.this, "original_name");
        String email = utils.getStoredString(ProfileActivity.this, "original_email");
        String favTeacherStr = getStoredString1(ProfileActivity.this, "user_answer");

        TextView name1 = findViewById(R.id.name_name);
        TextView name2 = findViewById(R.id.name_name_name);
        TextView email1 = findViewById(R.id.email_email);
        TextView email2 = findViewById(R.id.email_email_email);
        TextView favTeacher = findViewById(R.id.favouriteTeacherTv);

        name1.setText(name);
        name2.setText(name);

        email1.setText(email);
        email2.setText(email);

        favTeacher.setText(favTeacherStr);
    }

    private String getStoredString1(Context context, String name) {
        String PACKAGE_NAME = "dev.moutamid.facefilter.forgot";
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, "Error");
    }
}








