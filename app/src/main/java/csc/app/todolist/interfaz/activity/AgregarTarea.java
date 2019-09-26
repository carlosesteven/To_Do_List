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
import csc.app.todolist.room.objetos.Tarea;
import csc.app.todolist.room.view_model.VM_tareas;

public class AgregarTarea extends AppCompatActivity {

    private int idColor = 0;

    private FloatingActionButton btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_tarea);

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
        Tarea nuevaTarea = new Tarea();

        nuevaTarea.setNombreTarea( titulo );
        nuevaTarea.setDescripcionTarea( descripcion );
        nuevaTarea.setColorTarea( color );

        VM_tareas viewModel = new VM_tareas( getApplication() );
        viewModel.crearTarea(
                nuevaTarea
        );

        Toast.makeText(
                getBaseContext(),
                "Tarea Creada",
                Toast.LENGTH_LONG
        ).show();

        finish();
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
