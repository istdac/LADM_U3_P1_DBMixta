package mx.edu.ittepic.ladm_u3_practica1_dbmixta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.databinding.ActivityInsertarBinding
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.logica.Candidato

class InsertarActivity : AppCompatActivity() {
    lateinit var binding: ActivityInsertarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInsertar.setOnClickListener {
            val resInsert = Candidato(this).insertar(
                binding.txtNombre,
                binding.txtEscuela,
                binding.txtTelefono,
                binding.spCareer1,
                binding.spCareer2,
                binding.txtEmail
            )
            if(resInsert){
                Toast.makeText(this,"Se insertó con éxito", Toast.LENGTH_LONG).show()
                binding.txtNombre.setText("")
                binding.txtEscuela.setText("")
                binding.txtTelefono.setText("")
                binding.txtEmail.setText("")
                binding.spCareer2.setSelection(0)
                binding.spCareer1.setSelection(0)
            }else{
                AlertDialog.Builder(this).setMessage("No se pudo insertar").show()
            }
        }
    }//onCreate


}