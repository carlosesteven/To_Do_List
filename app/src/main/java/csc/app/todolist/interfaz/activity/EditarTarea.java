package csc.app.todolist.interfaz.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import csc.app.todolist.R;
import csc.app.todolist.room.ViewModel.VM_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditarTarea extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextInputEditText titulo;
    private TextInputEditText descripcion;

    private RadioButton colorA;
    private RadioButton colorB;
    private RadioButton colorC;

    private int idColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_tarea);

        titulo = findViewById( R.id.titulo );
        descripcion = findViewById( R.id.descripcion );
        FloatingActionButton btnAgregar = findViewById(R.id.btnAgregar);

        colorA = findViewById( R.id.colorA );
        colorB = findViewById( R.id.colorB );
        colorC = findViewById( R.id.colorC );

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

    private void informacionTarea( int key )
    {
        VM_tareas viewModel = new VM_tareas( getApplication() );
        Disposable disposable = viewModel
                .buscarTarea( key )
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tareaBuscada ->
                        {
                            if ( tareaBuscada != null )
                            {
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
        Tarea nuevaTarea = new Tarea();

        nuevaTarea.setIdTarea( idTarea );
        nuevaTarea.setNombreTarea( titulo );
        nuevaTarea.setDescripcionTarea( descripcion );
        nuevaTarea.setColorTarea( color );

        VM_tareas viewModel = new VM_tareas( getApplication() );
        viewModel.actualizarTarea(
                nuevaTarea
        );

        Toast.makeText(
                getBaseContext(),
                "Tarea Actualizada",
                Toast.LENGTH_LONG
        ).show();

        finish();
    }

    private void cambiarColorUI(int color)
    {
        if ( getSupportActionBar() != null )
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(color)));

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, color));

    }

}
