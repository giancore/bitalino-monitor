package com.example.bitalinomonitor.adapters;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem edit = menu.add(Menu.NONE, 1, 1, "Editar");
        MenuItem delete = menu.add(Menu.NONE, 2, 2, "Deletar");

        delete.setOnMenuItemClickListener(onEditMenu);
    }

    private final MenuItem.OnMenuItemClickListener onEditMenu = (MenuItem item) ->{
        switch (item.getItemId()) {
            case 1:
                //Do stuff
                break;

            case 2:
                //Do stuff

                break;
        }
        return true;

    };

}
