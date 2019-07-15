package com.kcolis.android.kcolis.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.appviews.AnnoncesList;
import com.kcolis.android.kcolis.appviews.SearchTown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class Recherche extends Fragment {


    private MaterialButton rechercher;
    private TextInputEditText dateannonce;
    private TextInputEditText depart;
    private TextInputEditText arrivee;
    private TextInputLayout dateannonce_error;
    private TextInputLayout depart_error;
    private TextInputLayout arrivee_error;
    public static final int REQUEST_CODE = 11;
    public static final int REQUEST_CODE_DEPART = 12;
    public static final int REQUEST_CODE_ARRIVEE = 13;
    String selectedDate;
    private OnFragmentInteractionListener mListener;
    private int idaeroportdepart;
    private int idaeroportarrivee;

    public Recherche() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.recherche, container, false);
        // get fragment manager so we can launch from fragment
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        rechercher = inflatedView.findViewById(R.id.rechercher);
        depart = inflatedView.findViewById(R.id.depart);
        arrivee = inflatedView.findViewById(R.id.arrivvee);
        dateannonce = inflatedView.findViewById(R.id.dateannonce);

        depart_error = inflatedView.findViewById(R.id.text_input_layout_depart);
        arrivee_error = inflatedView.findViewById(R.id.text_input_layout_password);
        dateannonce_error = inflatedView.findViewById(R.id.text_input_layout_dateannonce);


        depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchTown.class);
                intent.putExtra("title","Ville depart");
                startActivityForResult(intent, REQUEST_CODE_DEPART);
            }
        });

        arrivee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchTown.class);
                intent.putExtra("title","Ville d'arrivee");
                startActivityForResult(intent, REQUEST_CODE_ARRIVEE);
            }
        });

        depart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(getActivity(), SearchTown.class);
                    intent.putExtra("title","Ville depart");
                    startActivityForResult(intent, REQUEST_CODE_DEPART);
                }
            }
        });

        rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()==true) {
                    Intent intent = new Intent(getActivity(), AnnoncesList.class);
                    intent.putExtra("depart", String.valueOf(idaeroportdepart));
                    intent.putExtra("arrivee", String.valueOf(idaeroportarrivee));
                    intent.putExtra("date", String.valueOf(dateannonce.getText().toString()));
                    startActivity(intent);
                }
            }
        });

        dateannonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(Recherche.this, REQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        dateannonce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // create the datePickerFragment
                    AppCompatDialogFragment newFragment = new DatePickerFragment();
                    // set the targetFragment to receive the results, specifying the request code
                    newFragment.setTargetFragment(Recherche.this, REQUEST_CODE);
                    // show the datePicker
                    newFragment.show(fm, "datePicker");
                }
            }
        });

        return  inflatedView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            dateannonce.setText(selectedDate);
        } else if(requestCode == REQUEST_CODE_ARRIVEE && resultCode == Activity.RESULT_OK) {

            arrivee.setText(data.getStringExtra("ville"));
            idaeroportarrivee =  data.getIntExtra("id",0);

        } else if(requestCode == REQUEST_CODE_DEPART && resultCode == Activity.RESULT_OK) {
            depart.setText(data.getStringExtra("ville"));
            idaeroportdepart = data.getIntExtra("id",0);
        }
    }

    public boolean validate() {
        boolean valid = true;

        String _depart_voyage = depart.getText().toString();
        String _arrivee_voyage = arrivee.getText().toString();
        String _date_voyage = dateannonce.getText().toString();


        if (_depart_voyage.isEmpty()) {
            depart_error.setError("La ville de depart doit etre non vide");
            valid = false;
        } else {
            depart_error.setError(null);
        }

        if (_arrivee_voyage.isEmpty()) {
            arrivee_error.setError("La ville d'arrivee doit etre non vide");
            valid = false;
        }else {
            arrivee_error.setError(null);
        }

        if (_date_voyage.isEmpty()) {
            dateannonce_error.setError("La date de l'annonce doit etre non vide");
            valid = false;
        }else {
            dateannonce_error.setError(null);
        }

        return valid;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
