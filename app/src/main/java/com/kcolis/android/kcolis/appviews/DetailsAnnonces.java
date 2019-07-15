package com.kcolis.android.kcolis.appviews;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.model.Const;
import com.kcolis.android.kcolis.model.data.Annonce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailsAnnonces extends AppCompatActivity {

    private Toolbar toolbar;
    private Resources res;
    private Context context;
    private Intent intent;
    private Annonce annonce;
    private SimpleDraweeView pictureuser;
    private TextView user_name;
    private TextView user_label_time;
    private TextView ville_depart;
    private TextView ville_arrivee;
    private TextView dateannonce;
    private TextView prix;
    private TextView heure_depart;
    private TextView heure_arrivee;
    private TextView dateannonce2;
    private TextView poids;
    private TextView rdv1;
    private TextView rdv2;
    private RelativeLayout block_detail;
    private TextView USER_DETAILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_annonces);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Details Annonce");
        intent = getIntent();
        annonce = intent.getParcelableExtra("annonce");
        pictureuser = findViewById(R.id.icon);
        user_name  = findViewById(R.id.title);
        user_label_time = findViewById(R.id.title1);
        ville_depart = findViewById(R.id.contenu);
        ville_arrivee = findViewById(R.id.contenu_ville_arrivee_block);
        dateannonce = findViewById(R.id.contenu_heure_depart);
        dateannonce2 = findViewById(R.id.contenu_heure_depart2);
        prix = findViewById(R.id.contenu_heure_arrivee);
        heure_depart = findViewById(R.id.contenu_heure_depart_vrai);
        heure_arrivee = findViewById(R.id.contenu_heure_arrivee_vrai);
        poids = findViewById(R.id.poids_vrai);
        block_detail = findViewById(R.id.block_details);
        rdv1 = findViewById(R.id.rdv1);
        rdv2 = findViewById(R.id.rdv2);
        USER_DETAILS = findViewById(R.id.espace_details_annonceur4);
        user_name.setText(annonce.getNOM_USER());
        user_label_time.setText(annonce.getDATE_ANNONCE());
        ville_depart.setText(annonce.getVILLE_DEPART());
        ville_arrivee.setText(annonce.getVILLE_ARRIVEE());
        dateannonce.setText("Date de depart : "+annonce.getDATE_ANNONCE_VOYAGE());
        dateannonce2.setText("Date d'arrivee : "+annonce.getDATE_ANNONCE_VOYAGE2());
        prix.setText(annonce.getPRIX()+" euros/Kg");
        heure_depart.setText("Heure de depart : "+annonce.getHEURE_DEPART());
        heure_arrivee.setText("Heure d'arrivee : "+annonce.getHEURE_ARRIVEE());
        rdv1.setText("rdv de depart : "+annonce.getLIEUX_RDV1());
        rdv2.setText("rdv d'arrivee : "+annonce.getLIEUX_RDV2());
        poids.setText(annonce.getNOMBRE_KILO()+" Kg (max)");
        USER_DETAILS.setText("Plus de details sur "+annonce.getNOM_USER()+" ?");
        if(!annonce.getPHOTO_USER().equals("null")) {
            Uri uri = Uri.parse(Const.dns+"/colis/uploads/photo_de_profil/" + annonce.getPHOTO_USER());
            pictureuser.setImageURI(uri);
        }else
        {
            pictureuser.setImageResource(R.drawable.ic_profile_colorier);
        }

        block_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DetailsAnnonceur.class);
                intent.putExtra("annonce",annonce);
                startActivity(intent);
            }
        });

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
            case R.id.check_annonce :
                Intent intent = new Intent(getApplicationContext(),PaymentList.class);
                intent.putExtra("annonce",annonce);
                startActivity(intent);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.validationannonce, menu);
        MenuItem favoriteItem = menu.findItem(R.id.check_annonce);
        Drawable newIcon = (Drawable)favoriteItem.getIcon();
        newIcon.mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        favoriteItem.setIcon(newIcon);
        return true;
    }

}
