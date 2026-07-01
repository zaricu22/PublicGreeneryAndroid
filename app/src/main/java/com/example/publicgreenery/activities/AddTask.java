package com.example.publicgreenery.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import androidx.room.Room;
import com.example.publicgreenery.database.AppDatabase;
import com.example.publicgreenery.database.DatabaseDAO;
import com.example.publicgreenery.database.Job;
import java.lang.ref.WeakReference;

public class AddTask extends AsyncTask<Job, Void, Boolean> {
    private WeakReference<Activity> activity;

    public AddTask(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override   // Sporedna nit
    protected Boolean doInBackground(Job... jobs) {
        try {
            AppDatabase appDB = Room.databaseBuilder(activity.get(),
                    AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
            DatabaseDAO dbDAO = appDB.databaseDao();
            dbDAO.insertJobs(jobs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override   // Glavna UI nit
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        Activity act = activity.get();
        if (act == null || act.isFinishing()) return;
        if (success) {
            Toast.makeText(act, "Obaveza uspesno dodata", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(act, "Greska pri dodavanju obaveze", Toast.LENGTH_SHORT).show();
        }
        act.setResult(Activity.RESULT_CANCELED);
        act.finish();
    }
}
