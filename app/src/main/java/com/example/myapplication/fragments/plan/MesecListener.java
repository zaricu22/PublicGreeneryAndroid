package com.example.myapplication.fragments.plan;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import java.lang.ref.WeakReference;

public class MesecListener implements AdapterView.OnItemSelectedListener {
    // Azuriranje RecyclerView-a
    private WeakReference<Context> context;
    private WeakReference<Activity> activity;
    private WeakReference<View> root;
    private Resources res;
    private PlanFragment.StringWrapper statusWrapper;
    // Postavljanje odabranog 'meseca' iz spinner-a
    private PlanFragment.IntegerWrapper mesecWrapper;

    public MesecListener(Context context, Activity activity, View root, Resources res,
                         PlanFragment.StringWrapper status, PlanFragment.IntegerWrapper mesec) {
        this.context = new WeakReference<Context>(context);
        this.activity = new WeakReference<Activity>(activity);
        this.root = new WeakReference<View>(root);
        this.res = res;
        this.statusWrapper = status;
        this.mesecWrapper = mesec;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Azuriramo mesec izabranim iz spinner-a
        this.mesecWrapper.setMesec(pos+1);
        // AZURIRANJE RECYCLERVIEW-A ZA PRIKAZ POSLOVA PO KATEGORIJAMA
        // Komun sa bazom u posebnoj niti
        ShowJobsTask sjt = new ShowJobsTask(context, activity, root, res,
                statusWrapper.getStatus(), mesecWrapper.getMesec());
        sjt.execute();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        this.mesecWrapper.setMesec(1);
    }
}
