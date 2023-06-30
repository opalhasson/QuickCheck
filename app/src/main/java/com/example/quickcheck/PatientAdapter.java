package com.example.quickcheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class PatientAdapter extends ArrayAdapter<Patient> {

    private Context context;
    private int resource;
    private ArrayList<Patient> patients;

    public PatientAdapter(Context context, int resource, ArrayList<Patient> patients) {
        super(context, resource, patients);
        this.context = context;
        this.resource = resource;
        this.patients = patients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        // Get the current patient object from the ArrayList
        Patient patient = patients.get(position);

        // Update the view with the patient data
        TextView patientNameTextView = view.findViewById(R.id.patient_name_text);
        TextView patientIdTextView = view.findViewById(R.id.patient_id_text);
        ImageView patientImageView = view.findViewById(R.id.patient_im);

        patientNameTextView.setText(patient.getFirstname() + " " + patient.getLastname());
        patientIdTextView.setText(String.valueOf(patient.getId()));
        if (patient.getGender().equalsIgnoreCase("female"))
            patientImageView.setImageResource(R.drawable.ic_female);
        if (patient.getGender().equalsIgnoreCase("other"))
            patientImageView.setImageResource(R.drawable.ic_g);

        return view;
    }
}


