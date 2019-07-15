package com.kcolis.android.kcolis.connInscript;

import android.content.Intent;
import android.os.Bundle;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.appviews.Home;
import com.kcolis.android.kcolis.model.Database.SessionManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(this);
        Thread background = new Thread() {
            public void run() {

                try {

                    sleep(3*1000);
                    finish();
                    if(!session.IsLoggedIn()) {
                        Intent i = new Intent(getApplicationContext(), Connexion.class);
                        startActivity(i);
                        finish();
                    }else
                    {

                        Intent i = new Intent(getApplicationContext(), Home.class);
                        startActivity(i);
                        finish();
                    }


                } catch (Exception e) {

                }
            }
        };

        background.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
