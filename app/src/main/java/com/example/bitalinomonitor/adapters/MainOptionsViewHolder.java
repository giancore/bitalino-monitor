package com.example.bitalinomonitor.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bitalinomonitor.R;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainOptionsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_title)
    TextView title;

    @BindView(R.id.list_desc)
    TextView description;

    @BindView(R.id.list_avatar)
    ImageView imageView;

    @BindView(R.id.card_view)
    CardView cardView;

    public MainOptionsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}