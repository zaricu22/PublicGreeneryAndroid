package com.example.myapplication.activities;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.Room;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.DatabaseDAO;
import com.example.myapplication.database.Job;
import java.lang.ref.WeakReference;

public class AddTask extends AsyncTask<Job, Void, Void> {
    private WeakReference<Context> context;

    public AddTask(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override   // Sporedna nit
    protected Void doInBackground(Job... jobs) {
        System.out.println("DDDD");
        AppDatabase appDB = Room.databaseBuilder(context.get(),
                AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
        DatabaseDAO dbDAO = appDB.databaseDao();
        dbDAO.insertJobs(jobs);
        System.out.println("BBBB "+dbDAO.getAllJobs());
        return null;
    }

    @Override   // Glavna UI nit
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
