package csc.app.todolist.interfaz.adaptador;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import csc.app.todolist.R;
import csc.app.todolist.room.objetos.Tarea;

public class RV_item extends RecyclerView.Adapter<RV_item.ItemView>
{

    private List<Tarea> listaTareas;
    private INTERFACE_click listener;

    public RV_item( List<Tarea> listaTareas, INTERFACE_click listener )
    {
        this.listaTareas = listaTareas;
        this.listener = listener;
    }

    static class ItemView extends RecyclerView.ViewHolder
    {
        MaterialButton iconoTarea;
        TextView tituloTarea;
        ItemView(View vista)
        {
            super(vista);
            iconoTarea = vista.findViewById( R.id.itemColor );
            tituloTarea = vista.findViewById( R.id.itemTitulo );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemView view, int i)
    {
        Tarea actual = listaTareas.get( i );

        ColorStateList colorNota = null;
        switch ( actual.getColorTarea() )
        {
            case 1:
                colorNota = ContextCompat.getColorStateList(view.iconoTarea.getContext(), R.color.colorA);
                break;
            case 2:
                colorNota = ContextCompat.getColorStateList(view.iconoTarea.getContext(), R.color.colorB);
                break;
            case 3:
                colorNota = ContextCompat.getColorStateList(view.iconoTarea.getContext(), R.color.colorC);
                break;
        }
        view.iconoTarea.setBackgroundTintList(
                colorNota
        );

        view.tituloTarea.setText( actual.getNombreTarea() );
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tarea, viewGroup, false);
        final ItemView pvh = new ItemView(v);
        v.setOnClickListener(v1 -> listener.onItemClick(v1, pvh.getLayoutPosition() ));
        return pvh;
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }
}
