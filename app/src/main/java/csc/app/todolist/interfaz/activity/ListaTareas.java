package csc.app.todolist.interfaz.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import csc.app.todolist.R;
import csc.app.todolist.interfaz.adaptador.RV_item;
import csc.app.todolist.room.ViewModel.VM_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListaTareas extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView RvTareas;

    private ImageView user_foto;
    private TextView user_nombre;
    private MaterialButton user_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView no_tareas = findViewById( R.id.error_no_tareas );
        user_foto = findViewById( R.id.user_foto );
        user_nombre = findViewById( R.id.user_nombre );
        user_btn = findViewById( R.id.user_btn );
        user_btn.setOnClickListener(
                view -> startActivity( new Intent( getBaseContext(), Autenticacion.class ) )
        );

        RvTareas = findViewById( R.id.listaTareas );
        RvTareas.setLayoutManager( new LinearLayoutManager( this ) );
        RvTareas.setHasFixedSize(true);
        RvTareas.setFocusable( false );

        FloatingActionButton btnAgregar = findViewById( R.id.btnAgregar);
        btnAgregar.setOnClickListener(view -> startActivity( new Intent(getBaseContext(), AgregarTarea.class) ));

        VM_tareas viewModel = new VM_tareas( getApplication() );
        Disposable disposable = viewModel
                .getListaTareas()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lista ->
                        {
                            Log.d("csc_debug", "Cantidad Tareas -> " + lista.size());
                            if ( lista.size() > 0 ) {
                                RvTareas.setVisibility(View.VISIBLE);
                                no_tareas.setVisibility( View.GONE );
                                RV_tareas(lista);
                            }else {
                                RvTareas.setVisibility(View.INVISIBLE);
                                no_tareas.setVisibility( View.VISIBLE );
                            }
                        }, e -> {
                            if ( e != null && e.getMessage() != null )
                                Log.d("csc_debug", e.getMessage());
                            else
                                Log.d("csc_debug", "Se produjo un error al verificar la lista de tareas");
                        }
                );
        compositeDisposable.add(disposable);

    }

    private void validarAutenticacion()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String userNombre = sharedPrefs.getString("user_nombre", null);
        String userFoto = sharedPrefs.getString("user_foto", null);

        if ( userNombre != null && userFoto != null )
        {
            userNombre = "Hola " + userNombre + "!";
            user_nombre.setText( userNombre );
            Picasso.get().load(userFoto).into(user_foto);
            user_btn.setVisibility( View.GONE );
            Log.d("csc-debug", userFoto);
        }else{
            user_btn.setVisibility( View.VISIBLE );
        }
    }

    private void RV_tareas(List<Tarea> lista)
    {
        RV_item adaptador = new RV_item(
                lista,
                (v, position) -> {
                    Intent editar = new Intent( getBaseContext(), MostrarTarea.class );
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

    @Override
    protected void onResume() {
        super.onResume();
        validarAutenticacion();
    }
}
