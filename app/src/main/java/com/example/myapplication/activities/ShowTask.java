package com.example.myapplication.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import androidx.room.Room;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.DatabaseDAO;
import com.example.myapplication.database.Job;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;

public class ShowTask extends AsyncTask<Void, Void, Job> {
    private WeakReference<ShowActivity> activity;
    private Integer jobID;
    private ShowActivity.LatLngWrapper loc;

    public ShowTask(ShowActivity activity, Integer jobID, ShowActivity.LatLngWrapper loc) {
        this.activity = new WeakReference<ShowActivity>(activity);
        this.jobID = jobID;
        this.loc = loc;
    }

    @Override   // Sporedna nit
    protected Job doInBackground(Void... voids) {
        AppDatabase appDB = Room.databaseBuilder(activity.get(),
                AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
        DatabaseDAO dbDAO = appDB.databaseDao();

        // Dobavljanje posla iz baze na osnovu ID-a
        Job j = dbDAO.getJobById(jobID);

        // Azuriranje lokacije za prikaz mape
        loc.setLoc(new LatLng(j.getLat(),j.getLng()));

        return j;
    }

    @Override   // Glavna UI nit
    protected void onPostExecute(Job job) {
        super.onPostExecute(job);

        // Prikaz podataka dobavljanog posla na ekran
        EditText opisDuzi = activity.get().findViewById(R.id.text_opis2);
        if(!job.getOpis().equals(""))
            opisDuzi.setText(job.getOpis());
        EditText rok = activity.get().findViewById(R.id.text_rok2);
        if(!job.getRok().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            rok.setText(sdf.format(job.getRok()));
        }
        EditText materijali = activity.get().findViewById(R.id.text_materijali2);
        if(!job.getMaterijal().equals(""))
            materijali.setText(job.getMaterijal());
    }
}
