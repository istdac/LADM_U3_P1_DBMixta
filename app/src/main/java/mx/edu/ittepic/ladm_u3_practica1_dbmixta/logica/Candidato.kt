package mx.edu.ittepic.ladm_u3_practica1_dbmixta.logica

import android.content.ContentValues
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.BaseDatos
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Candidato (val p: AppCompatActivity) {
    //Modelo
    private var id=0
    private var nombre =""
    private var escuela = ""
    private var telefono = ""
    private var carrera1 = ""
    private var carrera2 = ""
    private var correo = ""
    private var fecha = SimpleDateFormat("yyyy-MM-dd").format(Date())
    //DB
    private var db = BaseDatos(p,"Prac1",null,2)
    //Funciones
    fun getId():Int{return id}
    fun getNombre():String{return nombre}
    fun getEscuela():String{return escuela}
    fun getTelefono():String{return telefono}
    fun getCarrera1():String{return carrera1}
    fun getCarrera2():String{return carrera2}
    fun getCorreo():String{return correo}
    fun getFecha():String{return fecha}

    fun getByID(id: String):Candidato?{
        var cursor = db.readableDatabase.rawQuery("SELECT * FROM Candidato WHERE ID=?",
            arrayOf(id))
        if(!cursor.moveToFirst()){
            return null
        }
        val resu = Candidato(p)
        resu.id=id.toInt()
        resu.nombre=cursor.getString(1)
        resu.escuela=cursor.getString(2)
        resu.telefono=cursor.getString(3)
        resu.carrera1=cursor.getString(4)
        resu.carrera2=cursor.getString(5)
        resu.correo=cursor.getString(6)
        resu.fecha=cursor.getString(7)
        return resu

    }

    fun obtenerTodos():ArrayList<Candidato>{
        var todos=ArrayList<Candidato>()
        var cursor = db.readableDatabase.rawQuery("Select * from Candidato",null)
        if(!cursor.moveToFirst()){
            return todos
        }
        do{
            var resu = Candidato(p)
            resu.id=cursor.getInt(0)
            resu.nombre=cursor.getString(1)
            resu.escuela=cursor.getString(2)
            resu.telefono=cursor.getString(3)
            resu.carrera1=cursor.getString(4)
            resu.carrera2=cursor.getString(5)
            resu.correo=cursor.getString(6)
            resu.fecha=cursor.getString(7)
            todos.add(resu)
        }while(cursor.moveToNext())
        return todos
    }//obtenerTodos

    fun mostrar():ArrayList<String>{
        var res= ArrayList<String>()
        var cursor= db.readableDatabase.rawQuery("Select * from Candidato",null)
        if(!cursor.moveToFirst()){
            res.add("No hay registros")
            return res
        }
        do {
            var cad=cursor.getString(1)+" : " + cursor.getString(6)+ "\n"+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getString(4)+" -- "+cursor.getString(5)
            res.add(cad)
        }while(cursor.moveToNext())
        return res
    }//mostrar

    fun eliminarID(id:String):Boolean{
        var res = db.writableDatabase.delete("Candidato","ID=?",arrayOf(id))
        if(res==0) return false
        return true
    }

    fun eliminarTodos():Boolean{
        var res = db.writableDatabase.rawQuery("delete from Candidato",null)
        if(!res.moveToFirst()){
            return false
        }
        return true
    }
    fun insertar(  nombre:EditText,escuela:EditText,telefono:EditText,
                   car1:Spinner,car2:Spinner,email:EditText):Boolean{
        var data = ContentValues()
        var f = SimpleDateFormat("yyyy-MM-dd").format(Date())
        println("Fecha insertada "+f )
        data.put("NOMBRE",nombre.text.toString())
        data.put("ESCUELA",escuela.text.toString())
        data.put("TELEFONO",telefono.text.toString())
        data.put("CARRERA1",car1.selectedItem.toString())
        data.put("CARRERA2",car2.selectedItem.toString())
        data.put("CORREO",email.text.toString())
        data.put("FECHA",f)
        var res = db.writableDatabase.insert("Candidato","ID",data)
        if(res==-1L)return false
        return true
    }//insertar

    fun actualizar(idCandidato:String,
                   nombre:EditText,escuela:EditText,telefono:EditText,
                   car1:Spinner,car2:Spinner,email:EditText):Boolean{
        var data = ContentValues()
        data.put("NOMBRE",nombre.text.toString())
        data.put("ESCUELA",escuela.text.toString())
        data.put("TELEFONO",telefono.text.toString())
        data.put("CARRERA1",car1.selectedItem.toString())
        data.put("CARRERA2",car2.selectedItem.toString())
        data.put("CORREO",email.text.toString())

        var res = db.writableDatabase.update("Candidato",data,"ID=?",arrayOf(idCandidato))
        if(res==0)return false
        return true
    }//actualizar

    fun listaID():ArrayList<String>{
        var res=ArrayList<String>()
        var cursor =db.readableDatabase.rawQuery("Select * from Candidato",null)
        if(!cursor.moveToFirst()){
            return res
        }
        do{
            res.add(cursor.getInt(0).toString())
        }while(cursor.moveToNext())
        return res
    }//listaID

    fun busCar1(car1:String):ArrayList<String>{
        var cursor = db.readableDatabase.rawQuery("Select * from Candidato where CARRERA1 = ?", arrayOf(car1))
        var candidatos = ArrayList<String>()
        if(!cursor.moveToFirst()){
            candidatos.add("No se encontraron candidatos para "+car1)
            return candidatos
        }
        do{
            var cad=cursor.getString(1)+" : " + cursor.getString(6)+ "\n"+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getString(4)+" -- "+cursor.getString(5)
            candidatos.add(cad)
        }while(cursor.moveToNext())
        return candidatos
    }//buscar Carrera1
    fun busCar2(car2:String):ArrayList<String>{
        var cursor = db.readableDatabase.rawQuery("Select * from Candidato where CARRERA2 = ?", arrayOf(car2))
        var candidatos = ArrayList<String>()
        if(!cursor.moveToFirst()){
            candidatos.add("No se encontraron candidatos para "+car2)
            return candidatos
        }
        do{
            var cad=cursor.getString(1)+" : " + cursor.getString(6)+ "\n"+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getString(4)+" -- "+cursor.getString(5)
            candidatos.add(cad)
        }while(cursor.moveToNext())
        return candidatos
    }//buscar Carrera2

    fun buscarEscuela(escuela:String):ArrayList<String>{
        var cursor = db.readableDatabase.rawQuery("Select * from Candidato where Escuela like ?",
            arrayOf('%'+escuela+'%'))
        var candidatos= ArrayList<String>()
        if(!cursor.moveToFirst()){
            candidatos.add("No se encontraron candidatos de "+escuela)
            return candidatos
        }
        do {
            var cad=cursor.getString(1)+" : " + cursor.getString(6)+ "\n"+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getString(4)+" -- "+cursor.getString(5)
            candidatos.add(cad)
        }while (cursor.moveToFirst())
        return candidatos
    }//buscarEscuela

    fun buscarFecha(fecha:String):ArrayList<String>{
        println("Fecha consulta "+fecha)
        var cursor = db.readableDatabase.rawQuery("Select * from Candidato where FECHA = ?",
            arrayOf(fecha+""))
        var candidatos= ArrayList<String>()
        if(!cursor.moveToFirst()){
            candidatos.add("No se encontraron candidatos registrados en "+fecha)
            return candidatos
        }
        do {
            var cad=cursor.getString(1)+" : " + cursor.getString(6)+ "\n"+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getString(4)+" -- "+cursor.getString(5)
            candidatos.add(cad)
        }while (cursor.moveToFirst())
        return candidatos

    }

    fun listarFecha():ArrayList<String>{
        var res= ArrayList<String>()
        var cursor= db.readableDatabase.rawQuery("Select FECHA from Candidato",null)
        if(!cursor.moveToFirst()){
            res.add("No hay registros")
            return res
        }
        do {
            var cad=cursor.getString(0)
            res.add(cad)
        }while(cursor.moveToNext())
        return res

    }
}//candidato