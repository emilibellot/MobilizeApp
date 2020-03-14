package com.app.mobilize.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.mobilize.R;
import com.app.mobilize.Usuari;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Usuari user;

    EditText peso, altura, dateNaixement;

    Button guardar_cambios, options, deleteAccount;

    Spinner genero;

    TextView username;

    FirebaseFirestore db;

    String gendre;

    int day, month, years;

    public PerfilFragment(Usuari user) {
        this.user = user;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        username = (TextView) view.findViewById(R.id.usernameTV);
        username.setText(user.getUsername());
        peso = (EditText) view.findViewById(R.id.pesoET);
        peso.setText(Double.toString(user.getWeight()));
        altura = (EditText) view.findViewById(R.id.alturaET);
        altura.setText(Integer.toString(user.getHeight()));
        genero = (Spinner)view.findViewById(R.id.generoSpin);
        String [] generos = {"","Hombre", "Mujer", "Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, generos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genero.setAdapter(adapter);
        genero.setSelection(obtenerPosicion(user.getGender()));
        genero.setOnItemSelectedListener(this);
        dateNaixement = (EditText)view.findViewById(R.id.dataCumpleañosTV);
        dateNaixement.setText(user.getDateNaixement());
        dateNaixement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        guardar_cambios = (Button) view.findViewById(R.id.guardar);
        guardar_cambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double peso_nuevo = Double.parseDouble(peso.getText().toString());
                int altura_nueva = Integer.parseInt(altura.getText().toString());
                user.setWeight(peso_nuevo);
                user.setHeight(altura_nueva);
                user.setGender(gendre);
                user.setDateNaixement(dateNaixement.getText().toString());
                db.collection("users").document(user.getUsername()).update("weight", peso_nuevo);
                db.collection("users").document(user.getUsername()).update("height", altura_nueva);
                db.collection("users").document(user.getUsername()).update("gender", gendre);
                db.collection("users").document(user.getUsername()).update("dateNaixement", user.getDateNaixement());
            }
        });
        return view;
    }

    private int obtenerPosicion(String gendre) {
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < genero.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (genero.getItemAtPosition(i).toString().equalsIgnoreCase(gendre)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        years = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // +1 because January is zero
                final String selectedDate = twoDigits(dayOfMonth) + "/" + twoDigits(monthOfYear+1) + "/" + twoDigits(year);
                dateNaixement.setText(selectedDate);
            }
        }
        , day, month, years);
        datePickerDialog.show();
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gendre = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
