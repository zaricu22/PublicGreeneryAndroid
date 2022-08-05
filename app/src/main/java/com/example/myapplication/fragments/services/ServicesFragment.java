package com.example.myapplication.fragments.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.DatabaseDAO;
import com.example.myapplication.database.Event;
import java.util.Date;

public class ServicesFragment extends Fragment {

    private Date date = null;   // trenutno izabran datum kalendara

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_services, container, false);
        // Prikazemo vremensku prognozu
        root.findViewById(R.id.textView).setVisibility(View.VISIBLE);
        root.findViewById(R.id.recycler_service).setVisibility(View.VISIBLE);
        // Sakrijemo kalendar
        root.findViewById(R.id.textView2).setVisibility(View.GONE);
        root.findViewById(R.id.calendarView).setVisibility(View.GONE);
        final EditText beleska = root.findViewById(R.id.txtBeleska);
        root.findViewById(R.id.txtBeleska).setVisibility(View.GONE);
        root.findViewById(R.id.btnBeleska).setVisibility(View.GONE);

        // Prikaz Kalendara, postavljanje na trenutni datum
        final CalendarView calendarView = root.findViewById(R.id.calendarView);
        date = new Date(calendarView.getDate());
        date.setHours(12);  date.setMinutes(0);  date.setSeconds(0);
        // Prikaz beleski za odabrani datum
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    Date date2 = getDate(); // uvek radimo sa globalnom instancom koja nece nesatti
                    date2.setYear(year-1900); date2.setMonth(month); date2.setDate(dayOfMonth);
                    date2.setHours(12);  date2.setMinutes(0);  date2.setSeconds(0);
                    // Komunikacija sa bazom mora u sporednoj niti da ne koci glavnu UI nit
                    PrikazTask pt = new PrikazTask(getContext(),getDate(),beleska);
                    pt.execute();
            }
        });

        // STALNO BRISANJE SVIH DOGADJAJA RADI TESTIRANJA
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDB = Room.databaseBuilder(getContext(),
                        AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
                DatabaseDAO dbDAO = appDB.databaseDao();
                for (Event ev: dbDAO.getAllEvents()) {
                    dbDAO.deleteEvents(ev);
                }
            }
        });

        // Unos nove ili azuriranje postojece beleske
        ImageButton imageButton = root.findViewById(R.id.btnBeleska);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Skrivanje tastature nakon unosa
                InputMethodManager inputManager = (InputMethodManager) getView()
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder binder = getView().getWindowToken();
                inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);

                UnosTask ut = new UnosTask(getContext(),getDate(),beleska.getText().toString());
                ut.execute();
            }
        });


        // Prosta provera dostupnosti interneta pre zahtevanja online prognoze
        if (isOnline()) {
            // Mrezna komunikacija mora u sporednoj niti da ne koci glavnu UI nit
            PrognozaTask pt = new PrognozaTask(root, getContext());
            pt.execute();
        }
        else {
            Toast.makeText(getContext(),"Internet nije dostupan!",Toast.LENGTH_LONG).show();
        }

        return root;
    }

    // Ukljucivanje opcionog menija u fragmentu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    // Prikaz opcionog menija fragmenta
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.services_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Upravljanje klikovima u opcionom meniju
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Da ne bi kreirali vise layout-a za isti fragmenat, prikazujemo i sklanjamo odgovarajuce komponente layout-a
        // GONE - da nestane i ne zauzima prostor   /   INVISIBLE - samo da se ne vidi
        if (id == R.id.item_weather) {
            getView().findViewById(R.id.textView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.recycler_service).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.textView2).setVisibility(View.GONE);
            getView().findViewById(R.id.calendarView).setVisibility(View.GONE);
            getView().findViewById(R.id.txtBeleska).setVisibility(View.GONE);
            getView().findViewById(R.id.btnBeleska).setVisibility(View.GONE);

        }
        if (id == R.id.item_calendar) {
            getView().findViewById(R.id.textView).setVisibility(View.GONE);
            getView().findViewById(R.id.recycler_service).setVisibility(View.GONE);
            getView().findViewById(R.id.textView2).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.calendarView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.txtBeleska).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.btnBeleska).setVisibility(View.VISIBLE);

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}