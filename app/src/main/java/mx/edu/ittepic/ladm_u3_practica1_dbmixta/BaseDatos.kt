package mx.edu.ittepic.ladm_u3_practica1_dbmixta

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context,name,factory,version) {
    override fun onCreate(p0: SQLiteDatabase?){
        p0!!.execSQL("CREATE TABLE CANDIDATO (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOMBRE VARCHAR(200)," +
                "ESCUELA VARCHAR(200)," +
                "TELEFONO VARCHAR(200)," +
                "CARRERA1 VARCHAR(3)," +
                "CARRERA2 VARCHAR(3)," +
                "CORREO VARCHAR(200)," +
                "FECHA VARCHAR(200)" +
                ")")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}