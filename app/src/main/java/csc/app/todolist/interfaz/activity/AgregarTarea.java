package csc.app.todolist.interfaz.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import csc.app.todolist.R;
import csc.app.todolist.room.ViewModel.VM_tareas;
import csc.app.todolist.room.objetos.Tarea;

public class AgregarTarea extends AppCompatActivity {

    private int idColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_tarea);

        TextInputEditText titulo = findViewById( R.id.titulo );
        TextInputEditText descripcion = findViewById( R.id.descripcion );
        FloatingActionButton btnAgregar = findViewById( R.id.btnAgregar);

        RadioButton colorA = findViewById( R.id.colorA );
        RadioButton colorB = findViewById( R.id.colorB );
        RadioButton colorC = findViewById( R.id.colorC );

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

}
