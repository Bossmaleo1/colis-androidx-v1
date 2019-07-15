package com.kcolis.android.kcolis.appviews;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.model.Const;
import com.kcolis.android.kcolis.model.Database.SessionManager;
import com.kcolis.android.kcolis.model.dao.DatabaseHandler;
import com.kcolis.android.kcolis.model.data.Annonce;
import com.kcolis.android.kcolis.model.data.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Validation_Annonce extends AppCompatActivity {

    private Button validation;
    private EditText nomproprietaire;
    private EditText dateexpiration;
    private EditText numero_carte_derriere;
    private EditText numero_carte;
    private EditText poids;
    private EditText Description;
    private TextInputLayout nomproprietaire_error;
    private TextInputLayout dateexpiration_error;
    private TextInputLayout numero_carte_derriere_error;
    private TextInputLayout numero_carte_error;
    private TextInputLayout poids_error;
    private TextInputLayout Description_error;
    public static final int REQUEST_CODE = 11;
    static final int DATE_DIALOG_ID = 999;
    private int day;
    private int month;
    private int year;
    private Toolbar toolbar;
    private ImageView validation_image;
    private ProgressBar progressBar;
    private RelativeLayout block;
    private Intent intent;
    private Annonce annonce;
    private DatabaseHandler database;
    private SessionManager session;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation_annonce);
        toolbar = findViewById(R.id.toolbar);
        intent = getIntent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = new DatabaseHandler(this);
        session = new SessionManager(this);
        user = database.getUSER(Integer.valueOf(session.getUserDetail().get(SessionManager.Key_ID)));
        getSupportActionBar().setTitle("PAIEMENT");

        validation = findViewById(R.id.send);
        validation_image = findViewById(R.id.validation_maleo);
        nomproprietaire = findViewById(R.id.nomproprietaire);
        dateexpiration = findViewById(R.id.dateexpiration);
        numero_carte_derriere = findViewById(R.id.numero_carte_derriere);
        numero_carte = findViewById(R.id.numero_carte);
        poids = findViewById(R.id.poids);
        Description = findViewById(R.id.description);
        progressBar = findViewById(R.id.progressbar);
        block = findViewById(R.id.block);
        nomproprietaire_error = findViewById(R.id.text_input_layout_nomproprietaire);
        dateexpiration_error = findViewById(R.id.text_input_layout_dateexpiration);
        numero_carte_derriere_error = findViewById(R.id.text_input_layout_numero_carte_derriere);
        numero_carte_error = findViewById(R.id.text_input_layout_numero_carte);
        poids_error = findViewById(R.id.text_input_layout_poids);
        Description_error = findViewById(R.id.text_input_layout_description);
        annonce = intent.getParcelableExtra("annonce");
        dateexpiration.setText("09-2019");
        numero_carte_derriere.setText("854");
        numero_carte.setText("5860-4024-5627-1523");

        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate() == true) {
                    VALIDATION_ANNONCE();
                }
            }
        });

        addListenerOnButton();
        setCurrentDateOnView();
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();*/
        Intent intent = new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                /*Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();*/
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean validate() {
        boolean valid = true;

        String _nom_proprietaire = nomproprietaire.getText().toString();
        String _dateexpiration = dateexpiration.getText().toString();
        String _numero_carte_derriere = numero_carte_derriere.getText().toString();
        String _numero_carte = numero_carte.getText().toString();
        String _poids = poids.getText().toString();
        String _Description = Description.getText().toString();

        if (_nom_proprietaire.isEmpty()) {
            nomproprietaire_error.setError("Veuillez preciser le nom du proprietaire de la carte");
            valid = false;
        } else {
            nomproprietaire_error.setError(null);
        }

        if (_dateexpiration.isEmpty()) {
            dateexpiration_error.setError("Veuillez preciser la date d'expiration de la carte");
            valid = false;
        } else {
            dateexpiration_error.setError(null);
        }

        if (_numero_carte_derriere.isEmpty()) {
            numero_carte_derriere_error.setError("Veuillez preciser le numero de la carte");
            valid = false;
        } else {
            numero_carte_derriere_error.setError(null);
        }

        if (_numero_carte.isEmpty()) {
            numero_carte_error.setError("Veuillez preciser le numero derriere la carte");
            valid = false;
        } else {
            numero_carte_error.setError(null);
        }

        if (_poids.isEmpty()) {
            poids_error.setError("Veuillez preciser le poids de votre colis");
            valid = false;
        } else {
            poids_error.setError(null);
        }

        if (_Description.isEmpty()) {
            Description_error.setError("Veuillez preciser la description de votre colis");
            valid = false;
        } else {
            Description_error.setError(null);
        }

        return valid;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth+1;
            day = selectedDay;
            //dateexpiration.setText(day);

            if(month>9 && day>9) {
                dateexpiration.setText(day + "-" + month + "-" + year);
            }else if(day<9 && month>9)
            {
                dateexpiration.setText("0"+day + "-" + month + "-" + year);
            }else if(month<9 && day>9)
            {
                dateexpiration.setText(day + "-0" + month + "-" + year);
            }
            else if(month<9 && day<9)
            {
                dateexpiration.setText("0"+day + "-0" + month + "-" + year);
            }
        }
    };

    public void addListenerOnButton() {


        dateexpiration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });
    }

    public void setCurrentDateOnView() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }


    private void volley_de_sms_notification()
    {
        String message = "L'utilisateur "+user.getPRENOM()+" "+user.getNOM()+" vous effetuez une demande d'expedition de son colis";
        String url_connexion = "https://api.smsbox.fr/api.php?apikey=pub-ad1746a3c1fa0266937010c56e18e0b0-7dd7c66d-e54b-4b17-9c1d-73c4215482c1&msg="+
                message+"&dest=" +
                String.valueOf(annonce.getPHONE_USER())+"&mode=Expert";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_connexion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

    private void volley_de_fcm_notification()
    {
        String message = "L'utilisateur "+user.getPRENOM()+" "+user.getNOM()+" vous effetuez une demande d'expedition de son colis";
        String url_envoie =  Const.dns+"/colis/Apifcm/apiFCM.php?push_type=individual&regId="+String.valueOf(annonce.getKEYPUSH())+"&message="+message
                +"&title=Colis";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_envoie,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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



    private void VALIDATION_ANNONCE()
    {
        String message = "L'utilisateur "+user.getPRENOM()+" "+user.getNOM()+" vous effetuez une demande d'expedition de son colis";
        block.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String Url = Const.dns+"/colis/ColisApi/public/api/InsertValidation?nombrekilo="+String.valueOf(poids.getText().toString())
                +"&description="+String.valueOf(Description.getText().toString())+"&id_annonce="+String.valueOf(annonce.getID())+"&id_emmetteur="+user.getID()+"&libelle="+message;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //volley_de_fcm_notification();
                        //volley_de_sms_notification();
                        progressBar.setVisibility(View.GONE);
                        validation_image.setVisibility(View.VISIBLE);
                        Toast.makeText(Validation_Annonce.this,"Votre demande vient d'etre effectuer avec succes !!",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Validation_Annonce.this,"Une erreur serveur vient de se produire !!!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        validation_image.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        block.setVisibility(View.VISIBLE);
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
}
