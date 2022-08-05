package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.Job;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    EditText textLokacija;
    double selectedLatitude;
    double selectedLongitude;
    private static final int ADDRESS_PICKER_REQUEST = 1020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final EditText textOpis = findViewById(R.id.text_opis);
        final EditText textRok = findViewById(R.id.text_rok);
        final EditText textMaterijali = findViewById(R.id.text_materijali);

        textLokacija = findViewById(R.id.text_lokacija);
        textLokacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Koristi se specijalna gotova open-source bibl za rad sa mapama
                * https://android-arsenal.com/details/1/7761 */
                Intent i = new Intent(AddActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);
            }
        });

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("USPEO KLIK ");
                Job j = new Job();
                j.setOpis(textOpis.getText().toString());
                j.setMaterijal(textMaterijali.getText().toString());
                j.setPocetak(new Date());
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    j.setRok(sdf.parse(textRok.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                j.setStatus("Planirano");
                j.setLat(selectedLatitude);
                j.setLng(selectedLongitude);

                System.out.println("AAAA "+j.toString());
                // Komunikacija sa bazom u posebnoj niti
                AddTask at = new AddTask(getApplicationContext());
                at.execute(j);

                // Vratimo rezultat glavnoj aktivnosti i zavrsimo tekucu aktivnost
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_CANCELED, returnIntent);  // resultCode
                finish();
            }
        });
    }

    // Greskom upozorava nullable jer trazi anotacije JetBranis-a umesto Androida
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data !=null && data.getStringExtra(MapUtility.ADDRESS) !=null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    textLokacija.setText(address);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
