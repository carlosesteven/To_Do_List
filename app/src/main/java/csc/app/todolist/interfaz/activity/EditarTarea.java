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
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditarTarea extends AppCompatActivity {

    private TextInputEditText titulo;
    private TextInputEditText descripcion;
    private FloatingActionButton btnAgregar;

    private BD_tareas baseDatos;

    private RadioButton colorA;
    private RadioButton colorB;
    private RadioButton colorC;

    private int idColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_tarea);

        baseDatos = BD_tareas.getDatabase( this );

        titulo = findViewById( R.id.titulo );
        descripcion = findViewById( R.id.descripcion );
        btnAgregar = findViewById(R.id.btnAgregar);

        colorA = findViewById( R.id.colorA );
        colorA.setOnClickListener(view -> cambiarColorUI(R.color.colorA) );
        colorB = findViewById( R.id.colorB );
        colorB.setOnClickListener(view -> cambiarColorUI(R.color.colorB) );
        colorC = findViewById( R.id.colorC );
        colorC.setOnClickListener(view -> cambiarColorUI(R.color.colorC) );

        int idTarea = getIntent().getIntExtra("idTarea", 0);

        informacionTarea( idTarea );

        btnAgregar.setOnClickListener(view ->
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
                actualizarTarea(
                        idTarea,
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
        titulo.setText( tarea.getNombreTarea() );
        descripcion.setText( tarea.getDescripcionTarea() );
        switch ( tarea.getColorTarea() )
        {
            case 1:
                cambiarColorUI( R.color.colorA );
                colorA.setChecked( true );
                break;
            case 2:
                cambiarColorUI( R.color.colorB );
                colorB.setChecked( true );
                break;
            case 3:
                cambiarColorUI( R.color.colorC );
                colorC.setChecked( true );
                break;
        }
    }

    private void actualizarTarea(int idTarea, String titulo, String descripcion, int color)
    {
        Completable.fromAction(
                () -> {
                    Tarea nuevaTarea = new Tarea();
                    nuevaTarea.setIdTarea( idTarea );
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
                                "Tarea Actualizada",
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
