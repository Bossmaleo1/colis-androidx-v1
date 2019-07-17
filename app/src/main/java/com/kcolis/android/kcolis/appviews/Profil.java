package com.kcolis.android.kcolis.appviews;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.model.Const;
import com.kcolis.android.kcolis.model.Database.SessionManager;
import com.kcolis.android.kcolis.model.dao.DatabaseHandler;
import com.kcolis.android.kcolis.model.data.User;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Profil extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Resources res;
    private Intent intent;
    private ImageView pictureuser;
    private DatabaseHandler database;
    private SessionManager session;
    private User user;
    private SimpleDraweeView draweeView;
    private ImageView mention_icon;
    private RelativeLayout notation_block;
    private RelativeLayout piece_identite_block;
    private ImageView verification_piece_icon;
    private TextView verification_piece_text;
    private Drawable piece_identite_icon_failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        toolbar = findViewById(R.id.toolbar);
        res = getResources();
        intent = getIntent();
        database = new DatabaseHandler(this);
        session = new SessionManager(this);
        user = database.getUSER(Integer.valueOf(session.getUserDetail().get(SessionManager.Key_ID)));
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        mention_icon = findViewById(R.id.mention_icon);
        draweeView = findViewById(R.id.my_image_view);
        collapsingToolbarLayout.setTitle(user.getPRENOM()+" "+user.getNOM());
        collapsingToolbarLayout.setContentScrimColor(res.getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        notation_block = findViewById(R.id.notation_block);
        verification_piece_icon = findViewById(R.id.verification_piece_icon);
        verification_piece_text = findViewById(R.id.verification_piece_text);
        piece_identite_block = findViewById(R.id.piece_identite_block);
        if(!user.getPHOTO().equals("null")) {
            Uri uri = Uri.parse(Const.dns+"/colis/uploads/photo_de_profil/" + user.getPHOTO());
            draweeView.setImageURI(uri);
        }else
        {
            pictureuser.setImageResource(R.drawable.ic_profile_colorier);
        }

        notation_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Avis.class);
                startActivity(intent);
            }
        });

        piece_identite_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VerificationPieceIdentite.class);
                startActivity(intent);
            }
        });

        //verification piece d'identite
        piece_identite_icon_failed = res.getDrawable(R.drawable.baseline_block_black_24);
        piece_identite_icon_failed.mutate().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_IN);
        verification_piece_icon.setImageDrawable(piece_identite_icon_failed);
        verification_piece_text.setTextColor(res.getColor(R.color.colorError));
        verification_piece_text.setText("Faites verifier votre piece d'identite");

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profil, menu);
        MenuItem edit_profil_menu = menu.findItem(R.id.profil_user);
        Drawable edit_profil_icon = (Drawable)edit_profil_menu.getIcon();
        edit_profil_icon.mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        edit_profil_menu.setIcon(edit_profil_icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
                return true;

            case R.id.profil_user:
                Intent intentprofildetails = new Intent(getApplicationContext(),ProfilDetails.class);
                startActivity(intentprofildetails);
            return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
