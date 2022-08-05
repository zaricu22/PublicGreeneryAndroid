package com.example.myapplication.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.User;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Intent i = ((MainActivity)getActivity()).getIntent();
        System.out.println(i.getStringExtra("user"));
        User u = new Gson().fromJson(i.getStringExtra("user"), User.class);
        ((TextView)root.findViewById(R.id.text_ime)).setText(u.getFirstName()+" "+u.getLastName());
        ((TextView)root.findViewById(R.id.text_mesto)).setText(u.getMesto());
        ((TextView)root.findViewById(R.id.text_radno)).setText(u.getRadnoMesto());

        // NE RADI dekodiranje InputStream-a na Bitmap, A U FRAGMENTU 'SERVISI' TO ISTO RADI
        // Komunikacija sa mrezom moze dugo trajati, da nebi kocila UI(glavnu nit) mora u posebnoj asinh pozadinskoj niti
//        ProfileTask pt = new ProfileTask(root); - dobavljanje i prikaz profilne slike
//        pt.execute();

        return root;
    }
}