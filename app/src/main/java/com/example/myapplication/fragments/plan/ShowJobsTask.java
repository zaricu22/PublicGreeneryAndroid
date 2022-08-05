package com.example.myapplication.fragments.plan;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.DatabaseDAO;
import com.example.myapplication.database.Job;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ShowJobsTask extends AsyncTask<Void, Void, List<Job>> {
    // slabe reference u slucaju da se sporedna nit izvrsava dugo GC moze nesmetano da obrise njene reference
    private WeakReference<Context> context;
    private WeakReference<Activity> activity;
    private WeakReference<View> view;
    private Resources res;
    private String status;
    private Integer mesec;

    public ShowJobsTask(WeakReference<Context> context, WeakReference<Activity> activity, WeakReference<View> view,
                        Resources res, String status, Integer mesec) {
        this.context = context;
        this.activity = activity;
        this.view = view;
        this.res = res;
        this.status = status;
        this.mesec = mesec;
    }

    @Override   // Sporedna nit
    protected List<Job> doInBackground(Void... voids) {
        AppDatabase appDB = Room.databaseBuilder(context.get(),
                AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
        DatabaseDAO dbDAO = appDB.databaseDao();

        // Jako nezahvalne stare klase za rad sa datumom(npr. mesec-1, god-1900...) radi kompatibilnosti
        Date poc = new Date();  // pocetni dan u datom mesecu
        poc.setMonth(mesec-1); poc.setDate(1); poc.setHours(0); poc.setMinutes(0); poc.setSeconds(0);
        Date kraj = new Date(); // poslednji dan u datom mesecu
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, mesec-1, 1);
        kraj.setMonth(mesec-1); kraj.setDate(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        kraj.setHours(23); kraj.setMinutes(59); kraj.setSeconds(59);

        List<Job> listaPoslova;
        if(status.equals("Sve")){   // svi status-i
            System.out.println("EEEE "+poc+" "+kraj);
            listaPoslova = dbDAO.getJobsByMesec(poc,kraj);
            System.out.println(listaPoslova);
        }
        else{
            System.out.println("FFFF "+poc+" "+kraj);
            listaPoslova = dbDAO.getJobs(status,poc,kraj);
            System.out.println(listaPoslova);
        }

        return listaPoslova;
    }

    @Override   // Glavna nit
    protected void onPostExecute(List<Job> listaPoslova) {
        super.onPostExecute(listaPoslova);
        // Azuriramo UI
        // Da bi se video scroll u layout/*.xml dodati android:scrollbars="vertical"
        // RecyclerView je po default-u scrollable
        RecyclerView recyclerView = this.view.get().findViewById(R.id.recycler_plan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context.get()));
        System.out.println("RC1 "+recyclerView.getLayoutManager().getLayoutDirection());
        recyclerView.setAdapter(new JobsAdapter(context, activity, LayoutInflater.from(this.context.get()), res, listaPoslova));
        System.out.println("RC2 "+recyclerView.getAdapter().getItemCount());
    }
}
