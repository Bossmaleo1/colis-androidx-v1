package com.kcolis.android.kcolis.appviews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.connInscript.MainActivity;
import com.kcolis.android.kcolis.fragments.AddAnnonce;
import com.kcolis.android.kcolis.fragments.Notifications;
import com.kcolis.android.kcolis.fragments.Recherche;
import com.kcolis.android.kcolis.model.Config;
import com.kcolis.android.kcolis.model.Const;
import com.kcolis.android.kcolis.model.Database.SessionManager;
import com.kcolis.android.kcolis.model.dao.DatabaseHandler;
import com.kcolis.android.kcolis.model.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class Home extends AppCompatActivity {

    private DatabaseHandler database;
    private BottomNavigationView navigation;
    private SessionManager session;
    private User user;
    private static final String TAG = Home.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private Resources res;
    private Menu menu;
    private SpannableString annonce_title_text;
    private SpannableString recherche_title_text;
    private SpannableString notification_title_text;
    private Drawable Icon_notification;
    private Drawable Icon_recherche;
    private Drawable Icon_annonce;
    private JSONObject reponse;
    private static int mCartItemCount = 0;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String Keypush = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        toolbar = findViewById(R.id.toolbar);
        navigation =  findViewById(R.id.bottom_navigation);
        res = getResources();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(res.getString(R.string.app_name));
        database = new DatabaseHandler(this);
        session = new SessionManager(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        menu = navigation.getMenu();
        Icon_notification = res.getDrawable(R.drawable.ic_notifications_black_24dp);
        Icon_recherche = res.getDrawable(R.drawable.baseline_search_black_24);
        Icon_annonce = res.getDrawable(R.drawable.baseline_control_point_black_24);
        annonce_title_text = new SpannableString("Ajouter Annonce");
        recherche_title_text = new SpannableString("Recherche");
        notification_title_text = new SpannableString("Notifications");

        Icon_recherche.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recherche_title_text.setSpan(new ForegroundColorSpan(res.getColor(R.color.colorPrimary)),0,recherche_title_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menu.findItem(R.id.recherche).setIcon(Icon_recherche);
        menu.findItem(R.id.recherche).setTitle(recherche_title_text);
        CountNotification();
        loadFragment(new Recherche());

        /*
        * il s'agit de la methode qui genere le
        * le jeton FCM
        * */
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);
                        Keypush = token;
                        Connexion();
                        // Log and toast
                        //String msg = token;
                        //Keypush = ms
                        //Toast.makeText(Home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    mCartItemCount++;
                    BadgeDrawable badge = navigation.showBadge(R.id.notification);
                    badge.setNumber(mCartItemCount);
                    badge.setBadgeTextColor(Color.WHITE);
            }
        };



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.deco:
                session.logoutUser();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                return true;
            case R.id.notification: {
                // Do something
                return true;
            }
            case R.id.profil:
                Intent intent = new Intent(getApplicationContext(),Profil.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.recherche:
                    Icon_notification = res.getDrawable(R.drawable.ic_notifications_black_24dp);
                    Icon_recherche = res.getDrawable(R.drawable.baseline_search_black_24);
                    Icon_annonce = res.getDrawable(R.drawable.baseline_control_point_black_24);
                    annonce_title_text = new SpannableString("Ajouter Annonce");
                    recherche_title_text = new SpannableString("Recherche");
                    notification_title_text = new SpannableString("Notifications");

                    Icon_recherche.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    recherche_title_text.setSpan(new ForegroundColorSpan(res.getColor(R.color.colorPrimary)),0,recherche_title_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    menu.findItem(R.id.recherche).setIcon(Icon_recherche);
                    menu.findItem(R.id.notification).setIcon(Icon_notification);
                    menu.findItem(R.id.annonce).setIcon(Icon_annonce);

                    menu.findItem(R.id.recherche).setTitle(recherche_title_text);
                    menu.findItem(R.id.notification).setTitle(notification_title_text);
                    menu.findItem(R.id.annonce).setTitle(annonce_title_text);
                    fragment = new Recherche();
                    loadFragment(fragment);
                    return true;
                case R.id.annonce:
                    Icon_notification = res.getDrawable(R.drawable.ic_notifications_black_24dp);
                    Icon_recherche = res.getDrawable(R.drawable.baseline_search_black_24);
                    Icon_annonce = res.getDrawable(R.drawable.baseline_control_point_black_24);
                    annonce_title_text = new SpannableString("Ajouter Annonce");
                    recherche_title_text = new SpannableString("Recherche");
                    notification_title_text = new SpannableString("Notifications");

                    Icon_annonce.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    annonce_title_text.setSpan(new ForegroundColorSpan(res.getColor(R.color.colorPrimary)),0,annonce_title_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    menu.findItem(R.id.annonce).setIcon(Icon_annonce);
                    menu.findItem(R.id.notification).setIcon(Icon_notification);
                    menu.findItem(R.id.recherche).setIcon(Icon_recherche);


                    menu.findItem(R.id.notification).setTitle(notification_title_text);
                    menu.findItem(R.id.recherche).setTitle(recherche_title_text);
                    menu.findItem(R.id.annonce).setTitle(annonce_title_text);
                    fragment = new AddAnnonce();
                    loadFragment(fragment);
                    return true;
                case R.id.notification:
                    Icon_notification = res.getDrawable(R.drawable.ic_notifications_black_24dp);
                    Icon_recherche = res.getDrawable(R.drawable.baseline_search_black_24);
                    Icon_annonce = res.getDrawable(R.drawable.baseline_control_point_black_24);
                    annonce_title_text = new SpannableString("Ajouter Annonce");
                    recherche_title_text = new SpannableString("Recherche");
                    notification_title_text = new SpannableString("Notifications");

                    Icon_notification.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    notification_title_text.setSpan(new ForegroundColorSpan(res.getColor(R.color.colorPrimary)),0,notification_title_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    menu.findItem(R.id.notification).setIcon(Icon_notification);
                    menu.findItem(R.id.recherche).setIcon(Icon_recherche);
                    menu.findItem(R.id.annonce).setIcon(Icon_annonce);

                    menu.findItem(R.id.notification).setTitle(notification_title_text);
                    menu.findItem(R.id.annonce).setTitle(annonce_title_text);
                    menu.findItem(R.id.recherche).setTitle(recherche_title_text);

                    fragment = new Notifications();
                    loadFragment(fragment);
                    navigation.removeBadge(R.id.notification);
                    return true;
            }
            return false;
        }
    };



    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void Connexion()
    {
        String url_sendkey = Const.dns.concat("/colis/ColisApi/public/api/UpdateKeyPush?ID=").concat(String.valueOf(database.getUSER(Integer.valueOf(session.getUserDetail().get(SessionManager.Key_ID))).getID())).concat("&PUSHKEY=").concat(Keypush);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_sendkey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        database.UpdateKeyPush(database.getUSER(Integer.valueOf(session.getUserDetail().get(SessionManager.Key_ID))).getID(),Keypush);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void CountNotification()
    {
        String url_sendkey = Const.dns.concat("/colis/ColisApi/public/api/AfficherCountNotification?ID_USER=").concat(String.valueOf(database.getUSER(Integer.valueOf(session.getUserDetail().get(SessionManager.Key_ID))).getID()));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_sendkey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            reponse = new JSONObject(response);
                            mCartItemCount = reponse.getInt("count_notification");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(mCartItemCount == 0){
                            navigation.removeBadge(R.id.notification);
                        }else {
                            BadgeDrawable badge = navigation.showBadge(R.id.notification);
                            badge.setNumber(mCartItemCount);
                            badge.setBadgeTextColor(Color.WHITE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //CountNotification();
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
