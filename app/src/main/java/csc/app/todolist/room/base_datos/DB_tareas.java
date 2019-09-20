package csc.app.todolist.room.base_datos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import csc.app.todolist.room.dao.DAO_tarea;
import csc.app.todolist.room.objetos.Tarea;

@Database(entities = {Tarea.class}, version = 1)
public abstract class DB_tareas extends RoomDatabase {

    public abstract DAO_tarea tareasDao();

    private static volatile DB_tareas INSTANCE;

    public static DB_tareas getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DB_tareas.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DB_tareas.class, "anime_favoritos")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
