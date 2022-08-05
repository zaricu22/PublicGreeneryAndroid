package com.example.myapplication.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.User;
import com.example.myapplication.database.DatabaseDAO;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // lokalne promenljive
        final TextView tvUsername = findViewById(R.id.text_username);
        final TextView tvPassword = findViewById(R.id.text_password);
        Button btnLogin = findViewById(R.id.btnLogin);

        // BRZ LOGIN RADI TESTIRANJA
        AppDatabase appDB = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        // Kada menjamo bazu to je nova verzija baze, Room ocekuje pravljenje migracija sa verzije na verziju
        // ukoliko to ne zelimo mozemo navesti da izbrise migracije i da poslednja verzija baze bude pocetna
        // allowMainThreadQueries() - dozvoljava radi testiranja potencijalno spore upite nad bazom koje mogu zakociti korisnicki interfejs

        System.out.println("DOSAO1");

        DatabaseDAO dbDAO = appDB.databaseDao();
//      Dodavanje nobog korisnika ukoliko je baza resetovana
//        User user = new User();
//        user.setUsername("Perax");
//        user.setPassword("px");
//        user.setFirstName("Petar");
//        user.setLastName("Peric");
//        user.setMesto("Veternik");
//        user.setRadnoMesto("Orezivac");
//        dbDAO.insertUsers(user);
        tvUsername.setText("Perax");
        tvPassword.setText("px");
        dbDAO.login("Perax", "px")
                .observe(LoginActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user){
                        System.out.println("DOSAO2 "+user);
                        if(user != null) {
                            // String s = (new Gson().toJson(client)); - Object to JSON preko GSON bibl
                            // Cli client = new Gson().fromJson(s, Cli.class); - JSON to Object preko GSON bibl
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user", new Gson().toJson(user));
                            startActivity(intent);
                        }
                    }
                });

        // LOGIN BUTTON KLIK LISTENER
        // Sve prom iz spolj klase koje zelimo koristiti u unu klasama moraju biti 'final'
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                // Dobavljanje instance baze
//                AppDatabase kk = Room.databaseBuilder(getApplicationContext(),
//                        AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
//
//                // Asinh dobav rez iz baze kao LiveData(auto nova nit) i osmatranje/cekanje da pristignu
//                DatabaseDAO ss = kk.databaseDao();
//                ss.login(tvUsername.getText().toString(), tvPassword.getText().toString())
//                        .observe(LoginActivity.this, new Observer<User>() {
//                    @Override
//                    public void onChanged(@Nullable User user){
//                        if(user != null) {
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            // String s = (new Gson().toJson(client));  - Object to JSON preko GSON bibl
//                            // Cli client = new Gson().fromJson(s, Cli.class);  - JSON to Object preko GSON bibl
//                            intent.putExtra("user", new Gson().toJson(user));
//                            startActivity(intent);
//                        } else {
//                            Toast toast = Toast.makeText(LoginActivity.this, "Neuspesno", Toast.LENGTH_SHORT);
//                            toast.show();
//                        }
//                    }
//                });
//            }
//        });

    }
}

