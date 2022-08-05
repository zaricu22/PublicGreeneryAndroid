package com.example.myapplication.fragments.services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import androidx.room.Room;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.DatabaseDAO;
import com.example.myapplication.database.Event;
import java.lang.ref.WeakReference;
import java.util.Date;

public class UnosTask extends AsyncTask<Void, Void, Void> {
    // Slabe reference da bi GC mogao da ih obrise iako se nit predugo izvrsava
    private WeakReference<Context> context;
    private WeakReference<Date> date;
    private WeakReference<String> text;
    private boolean update = false;

    public UnosTask(Context context, Date date, String text) {
        this.context = new WeakReference<Context>(context);
        this.date = new WeakReference<Date>(date);
        this.text = new WeakReference<String>(text);
    }

    @Override   // Na pozadinskoj niti
    protected Void doInBackground(Void... voids) {
        AppDatabase appDB = Room.databaseBuilder(context.get(),
                AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
        DatabaseDAO dbDAO = appDB.databaseDao();
        Event e = dbDAO.getEventByDate(date.get());
        if(e == null) { // dogadjaj ne postoji treba napraviti novi
            e = new Event();
            e.setText(text.get());
            e.setDate(date.get());
            dbDAO.insertEvents(e);
        } else {    // dogadjaj postoji treba azurirati postojeci
            e.setText(text.get());
            dbDAO.updateEvents(e);
            update = true;
        }
        return null;
    }

    @Override   // Na glavnoj UI niti
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(update)
            Toast.makeText(context.get(),"Azurirali ste belesku",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context.get(),"Uneli ste belesku",Toast.LENGTH_LONG).show();
    }
}
