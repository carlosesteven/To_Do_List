package csc.app.todolist.interfaz.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import csc.app.todolist.R;
import csc.app.todolist.room.ViewModel.VM_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MostrarTarea extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView titulo;
    private TextView descripcion;

    private VM_tareas viewModel;
    private Tarea objeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_tarea);

        viewModel = new VM_tareas( getApplication() );

        titulo = findViewById( R.id.titulo_tarea );
        descripcion = findViewById( R.id.descripcion_tarea );
        FloatingActionButton btnAgregar = findViewById(R.id.btnEditar);

        btnAgregar.setOnClickListener(
                view -> {
                    Intent editar = new Intent(getBaseContext(), EditarTarea.class);
                    editar.putExtra("idTarea", objeto.getIdTarea() );
                    startActivity( editar );
                    finish();
                }
        );

        int idTarea = getIntent().getIntExtra("idTarea", 0);

        informacionTarea( idTarea );
    }

    private void informacionTarea( int key )
    {
        Disposable disposable = viewModel
                .buscarTarea( key )
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tareaBuscada ->
                        {
                            if ( tareaBuscada != null )
                            {
                                objeto = tareaBuscada;
                                mostrarInformacion( tareaBuscada );
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

    private void mostrarInformacion( Tarea tarea )
    {
        titulo.setText( tarea.getNombreTarea() );
        descripcion.setText( tarea.getDescripcionTarea() );
        switch ( tarea.getColorTarea() )
        {
            case 1:
                cambiarColorUI( R.color.colorA );
                break;
            case 2:
                cambiarColorUI( R.color.colorB );
                break;
            case 3:
                cambiarColorUI( R.color.colorC );
                break;
        }
    }

    private void cambiarColorUI(int color)
    {
        if ( getSupportActionBar() != null )
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(color)));

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, color));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mostrar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.menu_borrar) {
            viewModel.eliminarTarea( objeto );
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}