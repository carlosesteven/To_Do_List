package csc.app.todolist.room.view_model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import csc.app.todolist.room.base_datos.DB_tareas;
import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class VM_tareas extends AndroidViewModel {

    private DB_tareas baseDatos;

    public VM_tareas(@NonNull Application application)     {
        super(application);
        baseDatos = DB_tareas.getDatabase(application);
    }

    public Flowable< List<Tarea> > getListaTareas() {
        return baseDatos.tareasDao().getListaTareas();
    }

    public Single< Tarea > buscarTarea(int idTarea ) {
        return baseDatos.tareasDao().buscarTarea( idTarea );
    }

    public void crearTarea(Tarea tarea) {
        //baseDatos.tareasDao().crearTarea(modelClass);
        new taskAgregarTarea(
                tarea,
                baseDatos
        ).execute();
    }

    public void actualizarTarea(Tarea tarea) {
        new taskActualizarTarea(
                tarea,
                baseDatos
        ).execute();
    }

    public void eliminarTarea(Tarea tarea) {
        new taskEliminarTarea(
                tarea,
                baseDatos
        ).execute();
    }

    private static class taskAgregarTarea extends AsyncTask<Void, Void, Void>
    {
        private DB_tareas baseDatos;
        private Tarea tareaNueva;

        taskAgregarTarea(Tarea tareaNueva, DB_tareas baseDatos)
        {
            this.baseDatos = baseDatos;
            this.tareaNueva = tareaNueva;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            baseDatos.tareasDao().crearTarea(tareaNueva);
            return null;
        }
    }

    private static class taskActualizarTarea extends AsyncTask<Void, Void, Void>
    {
        private DB_tareas baseDatos;
        private Tarea tareaNueva;

        taskActualizarTarea(Tarea tareaNueva, DB_tareas baseDatos)
        {
            this.baseDatos = baseDatos;
            this.tareaNueva = tareaNueva;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            baseDatos.tareasDao().actualizarTarea(tareaNueva);
            return null;
        }
    }

    private static class taskEliminarTarea extends AsyncTask<Void, Void, Void>
    {
        private DB_tareas baseDatos;
        private Tarea tareaNueva;

        taskEliminarTarea(Tarea tareaNueva, DB_tareas baseDatos)
        {
            this.baseDatos = baseDatos;
            this.tareaNueva = tareaNueva;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            baseDatos.tareasDao().eliminarTarea(tareaNueva);
            return null;
        }
    }

}
