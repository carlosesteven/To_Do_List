package csc.app.todolist.room.objetos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "Tarea" )
public class Tarea
{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTarea")
    private int idTarea;

    @ColumnInfo(name = "nombreTarea")
    private String nombreTarea;

    @ColumnInfo(name = "descripcionTarea")
    private String descripcionTarea;

    @ColumnInfo(name = "colorTarea")
    private int colorTarea;

    @ColumnInfo(name = "completaTarea")
    private boolean completaTarea = false;

    public Tarea()
    {
        this.idTarea = 0;
    }

    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public String getDescripcionTarea() {
        return descripcionTarea;
    }

    public void setDescripcionTarea(String descripcionTarea) {
        this.descripcionTarea = descripcionTarea;
    }

    public int getColorTarea() {
        return colorTarea;
    }

    public void setColorTarea(int colorTarea) {
        this.colorTarea = colorTarea;
    }

    public boolean isCompletaTarea() {
        return completaTarea;
    }

    public void setCompletaTarea(boolean completaTarea) {
        this.completaTarea = completaTarea;
    }

}
