package com.example.bitalinomonitor.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.activities.DeviceActivity;
import com.example.bitalinomonitor.activities.PatientListActivity;
import com.example.bitalinomonitor.models.MainOptionModel;

import java.util.ArrayList;
import java.util.List;

public class MainOptionsAdapter extends RecyclerView.Adapter<MainOptionsViewHolder> {

   private List<MainOptionModel> options = new ArrayList<>();

    public MainOptionsAdapter() {
        MainOptionModel myPatients = new MainOptionModel("Meus Pacientes", "Lista de pacientes", R.drawable.ic_accessibility_black_24dp, PatientListActivity.class);
        options.add(myPatients);

        MainOptionModel device = new MainOptionModel("Executar Exame", "Efetua uma exame com o dispositivo conectado", R.drawable.ic_sync_black_24dp, DeviceActivity.class);
        options.add(device);
    }

    @Override
    public MainOptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainOptionsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_mainoptions, parent, false));
    }

    @Override
    public void onBindViewHolder(MainOptionsViewHolder holder, int position) {
        MainOptionModel item = options.get(position);

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.cardView.setOnClickListener(view -> selectOption(view, item));
        holder.imageView.setImageResource(item.getImage());
        holder.imageView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    private void selectOption(View view, MainOptionModel option){
        Intent intent = new Intent(view.getContext(), option.getIntentClass());
        view.getContext().startActivity(intent);
    }
}
