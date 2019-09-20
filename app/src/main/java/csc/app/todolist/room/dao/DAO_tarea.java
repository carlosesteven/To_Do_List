package csc.app.todolist.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import csc.app.todolist.room.objetos.Tarea;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DAO_tarea {

    @Query("SELECT * FROM Tarea")
    Flowable<List<Tarea>> getListaTareas();

    @Query("SELECT * FROM Tarea WHERE idTarea = :idTarea")
    Single<Tarea> buscarTarea( int idTarea );

    @Insert(onConflict = REPLACE)
    void crearTarea(Tarea... favorito);

    @Insert(onConflict = REPLACE)
    void actualizarTarea(Tarea... favorito);

    @Delete
    void eliminarTarea(Tarea favorito);

}
