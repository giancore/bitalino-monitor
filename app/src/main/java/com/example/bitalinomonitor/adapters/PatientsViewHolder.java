package com.example.bitalinomonitor.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_nome)
    TextView name;

    @BindView(R.id.item_telefone)
    TextView telephone;

    @BindView(R.id.textViewOptions)
    TextView buttonViewOption;

    @BindView(R.id.item_foto)
    ImageView photo;

    public PatientsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
