package csc.app.todolist.room.base_datos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import csc.app.todolist.room.dao.DAO_tarea;
import csc.app.todolist.room.objetos.Tarea;

@Database(entities = {Tarea.class}, version = 1)
public abstract class BD_tareas extends RoomDatabase {

    public abstract DAO_tarea tareasDao();

    private static volatile BD_tareas INSTANCE;

    public static BD_tareas getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BD_tareas.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BD_tareas.class, "lista_tareas")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
