package csc.app.todolist.interfaz.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import csc.app.todolist.R;
import csc.app.todolist.room.base_datos.DB_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MostrarTarea extends AppCompatActivity {

    private TextView titulo;
    private TextView descripcion;

    private DB_tareas baseDatos;

    private int idTarea = 0;

    private Tarea objeto;

    private FloatingActionButton btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_tarea);

        baseDatos = DB_tareas.getDatabase( this );

        titulo = findViewById( R.id.titulo_tarea );
        descripcion = findViewById( R.id.descripcion_tarea );
        btnAgregar = findViewById(R.id.btnEditar);

        btnAgregar.setOnClickListener(
                view -> {
                    Intent editar = new Intent(getBaseContext(), EditarTarea.class);
                    editar.putExtra("idTarea", objeto.getIdTarea() );
                    startActivity( editar );
                }
        );
        idTarea = getIntent().getIntExtra("idTarea", 0);
    }

    private void informacionTarea( int idTarea )
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
                                   mostrarInformacion( actual );
                               }
                               @Override
                               public void onError(Throwable e) {
                                   Toast.makeText( getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                );
    }

    private void mostrarInformacion( Tarea tarea )
    {
        objeto = tarea;
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

    private void eliminarTarea()
    {
        Completable.fromAction(
                () -> baseDatos.tareasDao().eliminarTarea( objeto )
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(
                                getBaseContext(),
                                "Tarea Eliminada",
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                getBaseContext(),
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void cambiarColorUI(int color)
    {
        if ( getSupportActionBar() != null )
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(color)));

        btnAgregar.setBackgroundTintList( getResources().getColorStateList(color) );

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
            eliminarTarea();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        informacionTarea( idTarea );
    }
}