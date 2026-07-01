package com.example.publicgreenery.activities;

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
import java.util.List;

import com.example.publicgreenery.R;
import com.example.publicgreenery.database.AppDatabase;
import com.example.publicgreenery.database.User;
import com.example.publicgreenery.database.DatabaseDAO;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Lokalne promenljive za polja forme
        final TextView tvUsername = findViewById(R.id.text_username);
        final TextView tvPassword = findViewById(R.id.text_password);
        Button btnLogin = findViewById(R.id.btnLogin);

        // ========== PODESAVANJE BAZE (TESTNI REZIM) ==========
        // fallbackToDestructiveMigration() — kada se promeni sema baze (nova verzija), Room
        //   brise celu bazu i pravi je ispocetka umesto da pravi migraciju. Posledica: svi
        //   podaci (korisnici, poslovi, eventi) se gube nakon promene seme.
        // allowMainThreadQueries() — dozvoljava sinhrono citanje/pisanje baze na glavnoj niti.
        //   Pogodno za testiranje, ali moze da zamrzne UI kod sporih upita. UKLONITI pre produkcije.
        AppDatabase appDB = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "gradsko-zelenilo")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        DatabaseDAO dbDAO = appDB.databaseDao();

        // ========== SEED: AUTOMATSKO KREIRANJE PREDEFINISANOG KORISNIKA ==========
        // Na svakom pokretanju proveravamo da li korisnik "Perax" postoji u bazi.
        // Ako ne postoji (npr. nakon sto je fallbackToDestructiveMigration obrisao bazu),
        // automatski ga ubacujemo. Na taj nacin uvek imamo korisnika za testiranje.
        //
        // PAZNJA: getAllUsers() koristimo sinhrono jer imamo allowMainThreadQueries().
        // Alternativa (LiveData login()) ne bi radila ovde — LiveData okida upit asinhrono
        // na pozadinskoj niti, pa bi onChanged() mogao da vrati null pre nego sto Room
        // registruje upravo upisanog korisnika, i prelaz na MainActivity se ne bi desio.
        List<User> sviKorisnici = dbDAO.getAllUsers();

        User loggedInUser = null;
        for (User u : sviKorisnici) {
            if ("Perax".equals(u.getUsername())) {
                loggedInUser = u;
                break;
            }
        }

        if (loggedInUser == null) {
            // Korisnik nije u bazi — kreiramo ga (prvi pokretaj ili nakon resetovanja baze)
            loggedInUser = new User();
            loggedInUser.setUsername("Perax");
            loggedInUser.setPassword("px");
            loggedInUser.setFirstName("Petar");
            loggedInUser.setLastName("Peric");
            loggedInUser.setMesto("Veternik");
            loggedInUser.setRadnoMesto("Orezivac");
            try {
                dbDAO.insertUsers(loggedInUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ========== AUTO-LOGIN (SAMO ZA TESTIRANJE) ==========
        // Preskacemo manuelni unos kredencijala i odmah prelazimo na MainActivity.
        // Korisnik se prosledjuje kao JSON string kroz Intent extra.
        // NAPOMENA: LoginActivity ovde ne poziva finish(), sto znaci da ce pritiskom
        //   na Back dugme iz MainActivity korisnik biti vracen na LoginActivity,
        //   koji ce ga odmah ponovo ulogovati. Da se to izbegne, dodati finish() posle startActivity().
        tvUsername.setText(loggedInUser.getUsername());
        tvPassword.setText(loggedInUser.getPassword());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("user", new Gson().toJson(loggedInUser));
        startActivity(intent);

        // ========== MANUELNI LOGIN PUTEM DUGMETA (TRENUTNO ISKLJUCENO) ==========
        // Odkomentarisati ovaj blok i zakomentarisati auto-login blok iznad da bi se
        // vratilo na rucni unos kredencijala putem forme.
        // VAZNO: Ne postoji ekran za registraciju — jedini korisnici koji mogu da se uloguju
        // su oni koji su hardkodovani u seed bloku iznad. Unos bilo cega drugog uvek vraca null.
        // Koristi LiveData za asinhrono citanje iz baze — rezultat stize u onChanged().
        // Sve promenljive iz spoljne klase koje se koriste u unutrasnjim klasama moraju biti 'final'.
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                // Dobavljanje instance baze
//                AppDatabase kk = Room.databaseBuilder(getApplicationContext(),
//                        AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
//
//                // Asinhrono dobavljanje rezultata iz baze kao LiveData (automatska pozadinska nit)
//                // i osmatranje/cekanje da pristignu
//                DatabaseDAO ss = kk.databaseDao();
//                ss.login(tvUsername.getText().toString(), tvPassword.getText().toString())
//                        .observe(LoginActivity.this, new Observer<User>() {
//                    @Override
//                    public void onChanged(@Nullable User user){
//                        if(user != null) {
//                            // String s = (new Gson().toJson(client)); - Object to JSON preko GSON bibl
//                            // Cli client = new Gson().fromJson(s, Cli.class); - JSON to Object preko GSON bibl
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
