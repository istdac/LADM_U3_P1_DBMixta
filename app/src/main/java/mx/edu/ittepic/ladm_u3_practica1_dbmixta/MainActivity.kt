package mx.edu.ittepic.ladm_u3_practica1_dbmixta

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.databinding.ActivityMainBinding
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.logica.Candidato


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegistrar.setOnClickListener {
            var actRegistrar = Intent(this,InsertarActivity::class.java)
            startActivity(actRegistrar)
        }
        binding.btnLocal.setOnClickListener {
            var actLocal = Intent(this,TablaLocal::class.java)
            startActivity(actLocal)
        }
        binding.btnRemoto.setOnClickListener {
            var actRemoto = Intent(this,TablaRemota::class.java)
            startActivity(actRemoto)
        }
        binding.btnSync.setOnClickListener {
            println(isOnline(this))
            if(isOnline(this)){
                FirebaseSync()
            }else{
                Toast.makeText(this,"No se tiene conexión",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }

    fun FirebaseSync(){
        //Obtener db local
        var cursor = Candidato(this).obtenerTodos()
        //Checar si vacío
        if(cursor.isEmpty()){
            AlertDialog.Builder(this).setTitle("Atención")
                .setMessage("No se tienen registros locales para sincronizar")
                .setPositiveButton("Ok"){d,i->}
                .show()
        }else{
            //Instancia
            var inst = FirebaseFirestore.getInstance().collection("candidatos")
            var cont=0
            for(c in cursor){
                //Crear datos
                var datos = hashMapOf(
                    "id" to c.getId(),
                    "nombre" to c.getNombre(),
                    "escuela" to c.getEscuela(),
                    "telefono" to c.getTelefono(),
                    "carrera1" to c.getCarrera1(),
                    "carrera2" to c.getCarrera2(),
                    "correo" to c.getCorreo(),
                    "fecha" to c.getFecha()
                )//datos
                inst.add(datos)
                    .addOnSuccessListener {
                        println("Insertado")

                    }
                    .addOnFailureListener { println(it.message!!) }
            }
            Candidato(this).eliminarTodos()
        }
    }
}