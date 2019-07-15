package com.kcolis.android.kcolis.appviews;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.model.Const;
import com.kcolis.android.kcolis.model.data.Annonce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailsAnnonceur extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Resources res;
    private Annonce annonce;
    private Intent intent;
    private SimpleDraweeView pictureuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsannonceur);
        toolbar = findViewById(R.id.toolbar);
        res = getResources();
        intent = getIntent();
        annonce = intent.getParcelableExtra("annonce");
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(annonce.getNOM_USER());
        collapsingToolbarLayout.setContentScrimColor(res.getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        pictureuser = findViewById(R.id.my_image_view);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!annonce.getPHOTO_USER().equals("null")) {
            Uri uri = Uri.parse(Const.dns+"/colis/uploads/photo_de_profil/" + annonce.getPHOTO_USER());
            pictureuser.setImageURI(uri);
        }else
        {
            pictureuser.setImageResource(R.drawable.ic_profile_colorier);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
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



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

