package com.example.myapplication.fragments.plan;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import androidx.room.Room;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.DatabaseDAO;
import com.example.myapplication.database.Job;

import java.lang.ref.WeakReference;

public class StatusUpdateTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Context> context;
    private Resources res;
    private Job job;
    private WeakReference<ImageView> imgStatus;
    private boolean statusChanged;

    public StatusUpdateTask(Context context, Resources res, Job job, ImageView img) {
        this.context = new WeakReference<Context>(context);
        this.res = res;
        this.job = job;
        this.imgStatus = new WeakReference<ImageView>(img);
    }

    @Override   // Sporedna nit
    protected Void doInBackground(Void... voids) {
        AppDatabase appDB = Room.databaseBuilder(context.get(),
                AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
        DatabaseDAO dbDAO = appDB.databaseDao();

        // Azuriranje u bazi
        if(job.getStatus().equals("Planirano")) {
            job.setStatus("U toku");
            statusChanged = true;
        }
        else if(job.getStatus().equals("U toku")) {
            job.setStatus("Urađeno");
            statusChanged = true;
        }
        System.out.println("AZURIRANO "+job.getStatus());
        dbDAO.updateJobs(job);

        return null;
    }

    @Override   // Glavna UI nit
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Azuriranje prikaza na ekranu
        if(statusChanged) {
            if (job.getStatus().equals("U toku"))
                imgStatus.get().setImageDrawable(res.getDrawable(R.drawable.ic_rotate_right_gray_48dp));
            else if (job.getStatus().equals("Urađeno"))
                imgStatus.get().setImageDrawable(res.getDrawable(R.drawable.ic_check_green_48dp));
        }
    }
}
