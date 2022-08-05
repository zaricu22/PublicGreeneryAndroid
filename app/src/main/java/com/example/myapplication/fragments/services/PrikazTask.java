package com.example.myapplication.fragments.services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import androidx.room.Room;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.DatabaseDAO;
import com.example.myapplication.database.Event;
import java.lang.ref.WeakReference;
import java.util.Date;

public class PrikazTask extends AsyncTask<Void, Void, Event> {
    // Slabe reference da bi GC mogao da ih obrise iako se nit predugo izvrsava
    private WeakReference<Context> context;
    private WeakReference<Date> date;
    private WeakReference<EditText> beleska;

    public PrikazTask(Context context, Date date, EditText beleska) {
        this.context = new WeakReference<Context>(context);
        this.date = new WeakReference<Date>(date);
        this.beleska = new WeakReference<EditText>(beleska);
    }

    @Override   // Na pozadinskoj niti
    protected Event doInBackground(Void... voids) {
        AppDatabase appDB = Room.databaseBuilder(context.get(),
                AppDatabase.class, "gradsko-zelenilo").fallbackToDestructiveMigration().build();
        DatabaseDAO dbDAO = appDB.databaseDao();
        Event e = dbDAO.getEventByDate(date.get());
        return e;
    }

    @Override   // Na glavnoj UI niti
    protected void onPostExecute(Event e) {
        super.onPostExecute(e);
        if (e != null){
            beleska.get().setText(e.getText());
        } else {
            beleska.get().setText("");
        }
    }
}
