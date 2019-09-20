package csc.app.todolist.interfaz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import csc.app.todolist.R;
import csc.app.todolist.interfaz.adaptador.RV_item;
import csc.app.todolist.room.ViewModel.VM_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView RvTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RvTareas = findViewById( R.id.listaTareas );
        FloatingActionButton btnAgregar = findViewById( R.id.btnAgregar);

        btnAgregar.setOnClickListener(view -> startActivity( new Intent(getBaseContext(), AgregarTarea.class) ));

        RvTareas.setLayoutManager( new LinearLayoutManager( this ) );
        RvTareas.setHasFixedSize(true);
        RvTareas.setFocusable( false );

        VM_tareas viewModel = new VM_tareas( getApplication() );
        Disposable disposable = viewModel
                .getListaTareas()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lista ->
                        {
                            Log.d("csc_debug", "Cantidad Tareas -> " + lista.size());
                            if ( lista.size() > 0 )
                                RV_tareas(lista);
                        }, e -> {
                            if ( e != null && e.getMessage() != null )
                                Log.d("csc_debug", e.getMessage());
                            else
                                Log.d("csc_debug", "Se produjo un error al verificar la lista de tareas");
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void RV_tareas(List<Tarea> lista)
    {
        RV_item adaptador = new RV_item(
                lista,
                (v, position) -> {
                    Intent editar = new Intent( getBaseContext(), EditarTarea.class );
                    editar.putExtra( "idTarea", lista.get( position ).getIdTarea() );
                    startActivity( editar );
                }
        );
        RvTareas.setAdapter(adaptador);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
