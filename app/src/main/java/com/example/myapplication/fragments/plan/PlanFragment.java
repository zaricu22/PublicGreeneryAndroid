package com.example.myapplication.fragments.plan;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.activities.AddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Date;

public class PlanFragment extends Fragment {

    /* String i Integer se tretiraju kao prosti tipovi i ne prosledjuju se po referenci
    * pa je neophodno napraviti posebne objekte 'omotace', oba listener-a moraju videti promene/znati podatak onog drugog */
    class StringWrapper {
        String status;
        public StringWrapper(String status) { this.status = status; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    class IntegerWrapper {
        Integer mesec;
        public IntegerWrapper(Integer mesec) { this.mesec = mesec; }
        public Integer getMesec() { return mesec; }
        public void setMesec(Integer mesec) { this.mesec = mesec; }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Da bi oba Spinner Listener-a mogla azurirati RecyclerView
        StringWrapper statusWrapper = new StringWrapper("Sve");
        IntegerWrapper mesecWrapper = new IntegerWrapper(new Date().getMonth()+1); // trenutni mesec

        View root = inflater.inflate(R.layout.fragment_plan, container, false);

        // Nakon kreiranja spinner-a poziva se njegov listener sa pocetnom vrednoscu
        // SPINNER STATUS PRIKAZ I LISTENER
        Spinner spinStatus = root.findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                // arrays.xml i default spinner item layout
                R.array.status_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears, default spinner dropdown item layout
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinStatus.setAdapter(adapter1);
        spinStatus.setOnItemSelectedListener(new StatusListener(getContext(),getActivity(),root,getResources(),statusWrapper,mesecWrapper));

        // SPINNER MESEC PRIKAZ I LISTENER
        Spinner spinMesec = root.findViewById(R.id.spinner_mesec);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.mesec_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMesec.setAdapter(adapter2);
        spinMesec.setSelection(new Date().getMonth());  // postavi spinner na trenutni mesec
        spinMesec.setOnItemSelectedListener(new MesecListener(getContext(),getActivity(),root,getResources(),statusWrapper,mesecWrapper));

        /* // BRISANJE SVIH POSLOVA RADI TESTIRANJA
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDB = Room.databaseBuilder(getContext(),
                        AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
                DatabaseDAO dbDAO = appDB.databaseDao();
                for (Job jb: dbDAO.getAllJobs()) {
                    dbDAO.deleteJobs(jb);
                }
            }
        });*/

        // AKCIONO DUGME ZA DODAVANJE POSLOVA
        FloatingActionButton btnFA = root.findViewById(R.id.faBtnAdd);
        btnFA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CCCCC "+isOnline()+" "+isLocationEnabled());
                if (!isLocationEnabled() || !isOnline())
                    Toast.makeText(getContext(),"Internet ili lokacija nisu dostupni!",Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    getActivity().startActivityForResult(intent, 1);  // requestCode
                }
            }
        });

        return root;
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager)this.getContext().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}

/* HIJERARHIJA POZIVA:
    PlanFragment -> StatusListener -> ShowJobsTask -> JobAdapter -> StatusUpdateTask & ShowActivity(ShowTask)
    PlanFragment -> MesecListener -> ShowJobsTask -> JobAdapter -> StatusUpdateTask & ShowActivity(ShowTask)
 */

