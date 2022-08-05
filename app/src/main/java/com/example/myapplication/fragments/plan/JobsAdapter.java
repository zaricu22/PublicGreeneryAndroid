package com.example.myapplication.fragments.plan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.activities.ShowActivity;
import com.example.myapplication.database.Job;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsViewHolder> {
    private WeakReference<Context> context;
    private WeakReference<Activity> activity;
    private LayoutInflater myInflater;
    private Resources res;
    private List<Job> poslovi;

    public JobsAdapter(WeakReference<Context> context, WeakReference<Activity> activity,
                       LayoutInflater myInflater, Resources res, List<Job> poslovi) {
        this.context = context;
        this.activity = activity;
        this.myInflater = myInflater;
        this.res = res;
        this.poslovi = poslovi;
    }

    // Provide a reference to the views for each data item
    public class JobsViewHolder extends RecyclerView.ViewHolder {
        ImageView status;
        TextView opis;
        TextView rok;
        ImageButton show;
        JobsViewHolder(View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.imgStatus);
            opis = itemView.findViewById(R.id.txtKratakOpis);
            rok = itemView.findViewById(R.id.txtRok);
            show = itemView.findViewById(R.id.btnShow);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public JobsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
        View view = myInflater.inflate(R.layout.recycler_plan_row, parent, false);
        return new JobsViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(JobsAdapter.JobsViewHolder holder, int position) {
        System.out.println("onBindViewHolder");
        position = holder.getAdapterPosition();
        String status = poslovi.get(position).getStatus();
        final ImageView imgStatus = holder.status;
        if(status.equals("Planirano"))
            imgStatus.setImageDrawable(res.getDrawable(R.drawable.ic_update_yell_48dp));
        else if(status.equals("U toku"))
            imgStatus.setImageDrawable(res.getDrawable(R.drawable.ic_rotate_right_gray_48dp));
        else
            imgStatus.setImageDrawable(res.getDrawable(R.drawable.ic_check_green_48dp));
        // Image Listener koji menja status na klik, zbog rada sa bazom zaseban task
        final int finalPosition = position;
        imgStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Komunikacija sa bazom mora u posebnoj niti
                StatusUpdateTask sut = new StatusUpdateTask(context.get(),res,poslovi.get(finalPosition),imgStatus);
                sut.execute(); // lako se zaboravi
            }
        });
        holder.opis.setText(poslovi.get(position).getOpis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.rok.setText("do " + sdf.format(poslovi.get(position).getRok()));
        final int finalPosition1 = position;
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DDDDD "+isOnline()+" "+isLocationEnabled());
                if (!isLocationEnabled() || !isOnline())
                    Toast.makeText(context.get(),"Internet ili lokacija nisu dostupni!",Toast.LENGTH_LONG).show();
                else {
                    // Prikaz odabranog posla u novoj aktivnosti
                    Intent i = new Intent(activity.get(), ShowActivity.class);
                    i.putExtra("jobID", poslovi.get(finalPosition1).getId());
                    context.get().startActivity(i);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() { return poslovi.size(); }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) context.get().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
