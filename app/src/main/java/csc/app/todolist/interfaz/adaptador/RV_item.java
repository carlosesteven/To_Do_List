package csc.app.todolist.interfaz.adaptador;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import csc.app.todolist.R;
import csc.app.todolist.room.base_datos.BD_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RV_item extends RecyclerView.Adapter<RV_item.ItemView>
{

    private List<Tarea> listaTareas;
    private INTERFACE_click listener;
    private BD_tareas baseDatos;

    public RV_item(List<Tarea> listaTareas, Context contex, INTERFACE_click listener )
    {
        this.listaTareas = listaTareas;
        this.listener = listener;
        this.baseDatos = BD_tareas.getDatabase( contex );
    }

    static class ItemView extends RecyclerView.ViewHolder
    {
        MaterialButton iconoTarea;
        TextView tituloTarea;
        ConstraintLayout itemContenedor;
        CheckBox itemCompleto;
        ItemView(View vista)
        {
            super(vista);
            iconoTarea = vista.findViewById( R.id.itemColor );
            tituloTarea = vista.findViewById( R.id.itemTitulo );
            itemContenedor = vista.findViewById( R.id.itemContenedor );
            itemCompleto = vista.findViewById( R.id.itemCompleto );
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

        view.itemCompleto.setOnClickListener(
                view1 -> verificarEstadoTarea(
                        actual.getIdTarea()
                )
        );

        if ( actual.isCompletaTarea() )
        {
            view.itemContenedor.setBackground(
                    ContextCompat.getDrawable(
                            view.itemContenedor.getContext(),
                            R.drawable.style_item_completado
                    )
            );
            view.itemCompleto.setChecked( true );
        }else{
            view.itemContenedor.setBackground(
                    ContextCompat.getDrawable(
                            view.itemContenedor.getContext(),
                            R.drawable.style_item_normal
                    )
            );
            view.itemCompleto.setChecked( false );
        }

        view.tituloTarea.setText( actual.getNombreTarea() );
    }

    /*
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
     */

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tarea, viewGroup, false);
        final ItemView itemActual = new ItemView(v);
        v.setOnClickListener(v1 -> listener.onItemClick(v1, itemActual.getLayoutPosition() ));
        return itemActual;
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    private void verificarEstadoTarea(int idTarea)
    {
        Single<Tarea> tareaSingle = baseDatos
                .tareasDao()
                .buscarTarea( idTarea );
        tareaSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Tarea>()
                           {
                               @Override
                               public void onSubscribe(Disposable d)
                               {
                               }
                               @Override
                               public void onSuccess(Tarea actual)
                               {
                                   Log.d("estado_tarea", "" + actual.isCompletaTarea() );
                                  if ( actual.isCompletaTarea() )
                                      cambiarEstadoTarea( actual, false );
                                  else
                                      cambiarEstadoTarea( actual, true );
                               }
                               @Override
                               public void onError(Throwable e) {

                               }
                           }
                );
    }

    private void cambiarEstadoTarea( Tarea actual, boolean estado )
    {

        Completable.fromAction(
                () -> {
                    actual.setCompletaTarea( estado );
                    baseDatos.tareasDao().actualizarTarea( actual );
                }
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}
