package csc.app.todolist.interfaz.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import csc.app.todolist.R;
import csc.app.todolist.room.base_datos.BD_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AgregarTarea extends AppCompatActivity {

    private int idColor = 0;
    private BD_tareas baseDatos;

    private FloatingActionButton btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_tarea);

        baseDatos = BD_tareas.getDatabase( this );

        TextInputEditText titulo = findViewById( R.id.titulo );
        TextInputEditText descripcion = findViewById( R.id.descripcion );
        btnAgregar = findViewById( R.id.btnAgregar);

        RadioButton colorA = findViewById( R.id.colorA );
        colorA.setOnClickListener(view -> cambiarColorUI(R.color.colorA) );
        RadioButton colorB = findViewById( R.id.colorB );
        colorB.setOnClickListener(view -> cambiarColorUI(R.color.colorB) );
        RadioButton colorC = findViewById( R.id.colorC );
        colorC.setOnClickListener(view -> cambiarColorUI(R.color.colorC) );

        btnAgregar.setOnClickListener( view ->
        {
            String tituloTarea = "";
            String descripionTarea = "";

            if ( colorA.isChecked() )
            {
                idColor = 1;
            }else if ( colorB.isChecked() )
            {
                idColor = 2;
            }else if (colorC.isChecked())
            {
                idColor = 3;
            }

            if ( titulo.getText() != null )
                tituloTarea = titulo.getText().toString();

            if ( descripcion.getText() != null )
                descripionTarea = descripcion.getText().toString();

            if ( !tituloTarea.isEmpty() && !descripionTarea.isEmpty() && idColor > 0 )
            {
                agregarTarea(
                        tituloTarea,
                        descripionTarea,
                        idColor
                );
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Hay campos vacios.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

    }

    private void agregarTarea(String titulo, String descripcion, int color)
    {
        Completable.fromAction(
                () -> {
                    Tarea nuevaTarea = new Tarea();
                    nuevaTarea.setNombreTarea( titulo );
                    nuevaTarea.setDescripcionTarea( descripcion );
                    nuevaTarea.setColorTarea( color );
                    baseDatos.tareasDao().crearTarea( nuevaTarea );
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
                        Toast.makeText(
                                getBaseContext(),
                                "Tarea Creada",
                                Toast.LENGTH_LONG
                        ).show();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                getBaseContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        finish();
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

}
