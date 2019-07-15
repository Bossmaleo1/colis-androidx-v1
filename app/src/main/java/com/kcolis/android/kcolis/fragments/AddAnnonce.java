package com.kcolis.android.kcolis.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.appviews.SearchTown;
import com.kcolis.android.kcolis.model.Const;
import com.kcolis.android.kcolis.model.Database.SessionManager;
import com.kcolis.android.kcolis.model.dao.DatabaseHandler;
import com.kcolis.android.kcolis.model.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddAnnonce extends Fragment {

    private int day;
    private int month;
    private int year;
    static final int DATE_DIALOG_ID = 999;
    private EditText date_annoonce;
    private EditText date_annonce2;
    private EditText ville_depart;
    private EditText ville_darrivee;
    private EditText heure_depart;
    private EditText heure_darrivee;
    private EditText prix_transaction;
    private EditText poids;
    private Button Ajouter;
    private Button Suivant;
    private EditText Lieux_rdv1;
    private EditText Lieux_rdv2;
    private TextInputLayout date_annoonce_error;
    private TextInputLayout date_annonce2_error;
    private TextInputLayout ville_depart_error;
    private TextInputLayout ville_darrivee_error;
    private TextInputLayout heure_depart_error;
    private TextInputLayout heure_darrivee_error;
    private TextInputLayout prix_transaction_error;
    private TextInputLayout poids_error;
    private TextInputLayout Lieux_rdv1_error;
    private TextInputLayout Lieux_rdv2_error;
    private RelativeLayout Block1;
    private RelativeLayout Block2;
    public static final int REQUEST_CODE = 11;
    public static final int REQUEST_CODE12 = 12;
    public static final int REQUEST_CODE13 = 13;
    public static final int REQUEST_CODE14 = 16;
    public static final int REQUEST_CODE_DEPART = 14;
    public static final int REQUEST_CODE_ARRIVEE = 15;
    private String selectedDate;
    private String selectedDate1;
    private String selectedDate2;
    private String selectedDate14;
    private OnFragmentInteractionListener mListener;
    private Snackbar snackbar;
    private ProgressDialog pDialog;
    private JSONObject reponse;
    private JSONObject data;
    private int succes;
    private Resources res;
    private CoordinatorLayout coordinatorLayout;
    private int idaeroportdepart;
    private int idaeroportarrivee;
    private DatabaseHandler database;
    private SessionManager session;
    private User user;
    private Animation anim;

    public AddAnnonce() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View bossmaleo =  inflater.inflate(R.layout.addannonce, container, false);
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        res = getResources();
        database = new DatabaseHandler(getActivity());
        session = new SessionManager(getActivity());
        user = database.getUSER(Integer.valueOf(session.getUserDetail().get(SessionManager.Key_ID)));

        coordinatorLayout = bossmaleo.findViewById(R.id.coordinatorLayout);
        date_annoonce = bossmaleo.findViewById(R.id.dateannonce);
        date_annonce2 = bossmaleo.findViewById(R.id.dateannonce_arrivee);
        ville_depart = bossmaleo.findViewById(R.id.depart);
        ville_darrivee = bossmaleo.findViewById(R.id.arrivvee);
        heure_depart = bossmaleo.findViewById(R.id.heure_depart);
        heure_darrivee = bossmaleo.findViewById(R.id.heure_arrivee);
        prix_transaction = bossmaleo.findViewById(R.id.prix);
        poids = bossmaleo.findViewById(R.id.kilo);
        Suivant = bossmaleo.findViewById(R.id.ajouter1);
        Ajouter = bossmaleo.findViewById(R.id.ajouter);
        Block1 = bossmaleo.findViewById(R.id.block);
        Block2 = bossmaleo.findViewById(R.id.block2);
        Lieux_rdv1 = bossmaleo.findViewById(R.id.lieux_rdv1);
        Lieux_rdv2 = bossmaleo.findViewById(R.id.lieux_rdv2);

        date_annoonce_error = bossmaleo.findViewById(R.id.text_input_layout_dateannonce);
        date_annonce2_error = bossmaleo.findViewById(R.id.text_input_layout_dateannonce_arrivee);
        ville_depart_error = bossmaleo.findViewById(R.id.text_input_layout_depart);
        ville_darrivee_error = bossmaleo.findViewById(R.id.text_input_layout_arrivvee);
        heure_depart_error = bossmaleo.findViewById(R.id.text_input_layout_heure_depart);
        heure_darrivee_error = bossmaleo.findViewById(R.id.text_input_layout_dheure_arrivee);
        prix_transaction_error = bossmaleo.findViewById(R.id.text_input_layout_prix);
        poids_error = bossmaleo.findViewById(R.id.text_input_layout_kilo);
        Lieux_rdv1_error = bossmaleo.findViewById(R.id.text_input_layout_lieux_rdv1);
        Lieux_rdv2_error = bossmaleo.findViewById(R.id.text_input_layout_lieux_rdv2);

        anim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in);

        ville_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchTown.class);
                intent.putExtra("title","D'ou partez-vous ?!");
                startActivityForResult(intent, REQUEST_CODE_DEPART);
            }
        });

        ville_darrivee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchTown.class);
                intent.putExtra("title","Ou allez-vous?!");
                startActivityForResult(intent, REQUEST_CODE_ARRIVEE);
            }
        });

        ville_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchTown.class);
                intent.putExtra("title","D'ou partez-vous ?!");
                startActivityForResult(intent, REQUEST_CODE_DEPART);
            }
        });

        ville_depart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(getActivity(), SearchTown.class);
                    startActivity(intent);
                }
            }
        });

        ville_darrivee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(getActivity(), SearchTown.class);
                    intent.putExtra("title","Ou allez-vous?!");
                    startActivityForResult(intent, REQUEST_CODE_ARRIVEE);
                }
            }
        });


        date_annoonce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // create the datePickerFragment
                    AppCompatDialogFragment newFragment = new DatePickerFragment();
                    // set the targetFragment to receive the results, specifying the request code
                    newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE);
                    // show the datePicker
                    newFragment.show(fm, "datePicker");
                }
            }
        });

        date_annoonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE);
                newFragment.show(fm, "datePicker");
            }
        });

        date_annonce2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE14);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        date_annonce2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // create the datePickerFragment
                    AppCompatDialogFragment newFragment = new DatePickerFragment();
                    // set the targetFragment to receive the results, specifying the request code
                    newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE);
                    // show the datePicker
                    newFragment.show(fm, "datePicker");
                }
            }
        });

        heure_depart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // create the datePickerFragment
                    AppCompatDialogFragment newFragment = new TimePickerFragment();
                    // set the targetFragment to receive the results, specifying the request code
                    newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE12);
                    // show the datePicker
                    newFragment.show(fm, "selectedTime");
                }
            }
        });

        heure_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new TimePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE12);
                // show the datePicker
                newFragment.show(fm, "selectedTime");
            }
        });

        heure_darrivee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // create the datePickerFragment
                    AppCompatDialogFragment newFragment = new TimePickerFragment();
                    // set the targetFragment to receive the results, specifying the request code
                    newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE13);
                    // show the datePicker
                    newFragment.show(fm, "selectedTime");
                }
            }
        });


        heure_darrivee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new TimePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(AddAnnonce.this, REQUEST_CODE13);
                // show the datePicker
                newFragment.show(fm, "selectedTime");
            }
        });

        Suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate1() == true) {
                    Block1.setVisibility(View.GONE);
                    Block2.setVisibility(View.VISIBLE);
                    Block2.startAnimation(anim);
                    /*pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Chargement en cours...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    InsertAnnonce();*/
                }
            }
        });

        Ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate2() == true) {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Chargement en cours...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    InsertAnnonce();
                }
            }
        });


        return  bossmaleo;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            date_annoonce.setText(selectedDate);
        }else if (requestCode == REQUEST_CODE12 && resultCode == Activity.RESULT_OK) {
            selectedDate1 = data.getStringExtra("selectedTime");
            heure_depart.setText(selectedDate1+":00");
        }else if (requestCode == REQUEST_CODE13 && resultCode == Activity.RESULT_OK) {
            selectedDate2 = data.getStringExtra("selectedTime");
            heure_darrivee.setText(selectedDate2);
        }else if(requestCode == REQUEST_CODE_ARRIVEE && resultCode == Activity.RESULT_OK) {

            ville_darrivee.setText(data.getStringExtra("ville"));
            idaeroportarrivee =  data.getIntExtra("id",0);

        }else if(requestCode == REQUEST_CODE_DEPART && resultCode == Activity.RESULT_OK) {
            ville_depart.setText(data.getStringExtra("ville"));
            idaeroportdepart = data.getIntExtra("id",0);
        }else if(requestCode == REQUEST_CODE14 && resultCode == Activity.RESULT_OK) {
            selectedDate14 = data.getStringExtra("selectedDate");
            // set the value of the editText
            date_annonce2.setText(selectedDate14);
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddAnnonce.OnFragmentInteractionListener) {
            mListener = (AddAnnonce.OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean validate1() {
        boolean valid = true;

        String _depart_voyage = ville_depart.getText().toString();
        String _arrivee_voyage = ville_darrivee.getText().toString();
        String _date_voyage = date_annoonce.getText().toString();
        String _date_voyage2 = date_annonce2.getText().toString();
        String _heure_depart = heure_depart.getText().toString();


        if (_depart_voyage.isEmpty()) {
            ville_depart_error.setError("Veuillez remplir la ville de depart svp !");
            valid = false;
        } else {
            ville_depart_error.setError(null);
        }

        if (_arrivee_voyage.isEmpty()) {
            ville_darrivee_error.setError("Veuillez remplir la ville d'arrivee svp!");
            valid = false;
        }else {
            ville_darrivee_error.setError(null);
        }

        if (_date_voyage.isEmpty()) {
            date_annoonce_error.setError("Veuillez remplir la date de depart svp !");
            valid = false;
        }else {
            date_annoonce_error.setError(null);
        }
        if (_date_voyage2.isEmpty()) {
            date_annonce2_error.setError("Veuillez remplir la date d'arrivee svp !");
            valid = false;
        }else {
            date_annonce2_error.setError(null);
        }

        if (_heure_depart.isEmpty()) {
            heure_depart_error.setError("Veuillez remplir l'heure de  depart svp !");
            valid = false;
        }else {
            heure_depart_error.setError(null);
        }




        return valid;
    }

    public boolean validate2() {
        boolean valid = true;


        String _heure_darrivee = heure_darrivee.getText().toString();
        String _prix_transaction = prix_transaction.getText().toString();
        String _poids = poids.getText().toString();
        String _lieux_rdv1 = Lieux_rdv1.getText().toString();
        String _lieux_rdv2 = Lieux_rdv2.getText().toString();

        if (_heure_darrivee.isEmpty()) {
            heure_darrivee_error.setError("Veuillez remplir l'heure d'arrivee svp !");
            valid = false;
        }else {
            heure_darrivee_error.setError(null);
        }

        if (_prix_transaction.isEmpty()) {
            prix_transaction_error.setError("Veuillez indiquer le prix de la transaction svp !");
            valid = false;
        }else {
            prix_transaction_error.setError(null);
        }

        if (_poids.isEmpty()) {
            poids_error.setError("Veuillez indiquer le poids par kilo svp !");
            valid = false;
        }else {
            poids_error.setError(null);
        }

        if (_lieux_rdv1.isEmpty()) {
            Lieux_rdv1_error.setError("Veuillez indiquer le lieux du rdv de depart");
            valid = false;
        }else {
            Lieux_rdv1_error.setError(null);
        }

        if (_lieux_rdv2.isEmpty()) {
            Lieux_rdv2_error.setError("Veuillez indiquer le lieux du rdv d'arrivee");
            valid = false;
        }else {
            Lieux_rdv2_error.setError(null);
        }




        return valid;
    }

    private void InsertAnnonce()
    {
        String dateannonce = String.valueOf(date_annoonce.getText().toString()).split("-")[2]+"-"
                +String.valueOf(date_annoonce.getText().toString()).split("-")[1]+"-"
                +String.valueOf(date_annoonce.getText().toString()).split("-")[0];
        String Url = Const.dns+"/colis/ColisApi/public/api/InsertAnnonce?heure_depart="+String.valueOf(heure_depart.getText().toString())+
                "&heure_arrivee="+String.valueOf(heure_darrivee.getText().toString())+"&max_kilo="+String.valueOf(poids.getText().toString())+
                "&lieux_rdv1="+String.valueOf(Lieux_rdv1.getText().toString())+"&lieux_rdv2="+String.valueOf(Lieux_rdv2.getText().toString())
                +"&dateannonce="+dateannonce+"&id_user="+user.getID()+"&id_aeroport1="+String.valueOf(idaeroportdepart)
                +"&id_aeroport2="+String.valueOf(idaeroportarrivee)
                +"&prix="+String.valueOf(prix_transaction.getText().toString()
                +"&dateannonce2="+selectedDate14);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        showJSON(response);
                        Block2.setVisibility(View.GONE);
                        Block1.setVisibility(View.VISIBLE);
                        Block1.startAnimation(anim);
                        selectedDate = null;
                        selectedDate1 = null;
                        selectedDate2 = null;
                        selectedDate14 = null;
                        date_annoonce.setText(null);
                        date_annonce2.setText(null);
                        ville_depart.setText(null);
                        ville_darrivee.setText(null);
                        heure_depart.setText(null);
                        heure_darrivee.setText(null);
                        prix_transaction.setText(null);
                        poids.setText(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(error instanceof ServerError)
                        {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),res.getString(R.string.error_volley_servererror),Toast.LENGTH_LONG).show();
                        }else if(error instanceof NetworkError)
                        {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),res.getString(R.string.error_volley_servererror),Toast.LENGTH_LONG).show();
                        }else if(error instanceof AuthFailureError)
                        {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),res.getString(R.string.error_volley_servererror),Toast.LENGTH_LONG).show();
                        }else if(error instanceof ParseError)
                        {

                            Toast.makeText(getActivity(),res.getString(R.string.error_volley_servererror),Toast.LENGTH_LONG).show();

                        }else if(error instanceof NoConnectionError)
                        {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),res.getString(R.string.error_volley_noconnectionerror),Toast.LENGTH_LONG).show();

                        }else if(error instanceof TimeoutError)
                        {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),res.getString(R.string.error_volley_timeouterror),Toast.LENGTH_LONG).show();
                        }else
                        {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),res.getString(R.string.error_volley_error),Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response)
    {
        try {
            reponse = new JSONObject(response);
            succes = reponse.getInt("succes");
            if(succes==1)
            {
                Toast.makeText(getActivity(),"Votre Annonce vient d'etre publier avec succes !", Toast.LENGTH_LONG).show();
                ville_depart.setText("");
                ville_darrivee.setText("");
                date_annoonce.setText("");
                prix_transaction.setText("");
                poids.setText("");
                heure_depart.setText("");
                heure_darrivee.setText("");
                Lieux_rdv1.setText("");
                Lieux_rdv2.setText("");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
