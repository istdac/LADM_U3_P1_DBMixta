package mx.edu.ittepic.ladm_u3_practica1_dbmixta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isVisible
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.databinding.ActivityTablaLocalBinding
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.logica.Candidato
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TablaLocal : AppCompatActivity() {
    lateinit var binding : ActivityTablaLocalBinding
    var fechas= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTablaLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)
         fechas = Candidato(this).listarFecha()

        for(f in fechas){
            println(f)
            println(f==SimpleDateFormat("yyyy-MM-dd").format(Date()))
        }
        mostrar()
        binding.filtro.isVisible=false
        binding.tablaLocal.setOnItemClickListener { adapterView, view, i, l ->
            var idSel = Candidato(this).listaID().get(i)
            AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("¿Qué desea hace con el Candidato seleccionado?")
                .setPositiveButton("ELIMINAR"){d,i->
                    eliminar(idSel)
                }
                .setNeutralButton("ACTUALIZAR"){d,i->
                    actualizarAlert(idSel)
                }
                .setNegativeButton("NADA"){d,i->}
                .show()
        }//tabla item click
        binding.btnBuscar.setOnClickListener {
            //Crear el linearlayout
            var layin = LinearLayout(this)
            var comboCampos = Spinner(this)
            var claveBusqueda = EditText(this)
            var itemsCampos = ArrayList<String>()

            //Crear filtros de buscar
            itemsCampos.add("Carrera 1")
            itemsCampos.add("Carrera 2")
            itemsCampos.add("Escuela")
            itemsCampos.add("Fecha (yyyy-mm-dd)")
            comboCampos.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemsCampos)

            //Insertar todo en layout
            layin.orientation= LinearLayout.VERTICAL
            claveBusqueda.setHint("Clave de busqueda")
            layin.addView(comboCampos)
            layin.addView(claveBusqueda)
            //Mostrar en alert
            AlertDialog.Builder(this)
                .setTitle("ATENCION")
                .setMessage("Elija campo para búsqueda")
                .setView(layin)
                .setPositiveButton("Buscar"){d,i->
                    consulta(comboCampos,claveBusqueda)
                    binding.filtro.isVisible=true
                    binding.filtro.isSelected=false
                }
                .setNeutralButton("Cancelar"){d,i->}
                .show()
        }//buscarClickLis
        binding.filtro.setOnClickListener {
            mostrar()
            binding.filtro.isVisible=false
            binding.filtro.isSelected=false
        }

    }//OnCreate

    private fun consulta(comboCampos: Spinner, claveBusqueda: EditText) {
        var posicionCampoSeleccionado = comboCampos.selectedItemId.toInt()
        when(posicionCampoSeleccionado){
            0->{//C1
                var datos = Candidato(this).busCar1(claveBusqueda.text.toString().uppercase())
                binding.tablaLocal.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos)
            }
            1->{//C2
                var datos = Candidato(this).busCar2(claveBusqueda.text.toString().uppercase())
                binding.tablaLocal.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos)

            }
            2->{//Escuela
                var datos = Candidato(this).buscarEscuela(claveBusqueda.text.toString())
                binding.tablaLocal.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos)

            }
            3->{//Fecha
                var datos = ArrayList<String>()
                var tot = Candidato(this).mostrar()
                var c=0
                for(f in fechas){
                    if(f==claveBusqueda.text.toString()){
                        datos.add(tot.get(c))
                    }
                    c++
                }
                binding.tablaLocal.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos)
            }
        }//when

    }

    fun mostrar(){
        var datos = Candidato(this).mostrar()
        binding.tablaLocal.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos)
    }
    fun eliminar(id:String){
        var resDel = Candidato(this).eliminarID(id)
        if(resDel){
            Toast.makeText(this,"Se eliminó con éxito", Toast.LENGTH_LONG).show()
            fechas = Candidato(this).listarFecha()
            onRestart()
        }else{
            AlertDialog.Builder(this).setMessage("No se pudo eliminar").show()
        }
    }//eliminar


    private fun toast(m:String){
        Toast.makeText(this,m,Toast.LENGTH_LONG).show()
    }
    fun aler(m:String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }

    fun actualizarAlert(idSel:String){
        var can = Candidato(this).getByID(idSel)
        //Crear el linearlayout
        var layin = LinearLayout(this)
        var nom = EditText(this)
        var escu = EditText(this)
        var tel = EditText(this)
        var emai = EditText(this)
        var comboCampos1 = Spinner(this)
        var comboCampos2 = Spinner(this)
        var itemsCampos = ArrayList<String>()
        itemsCampos.add("ISC")
        itemsCampos.add("IGE")
        itemsCampos.add("BIO")
        itemsCampos.add("QUI")
        itemsCampos.add("CIV")
        itemsCampos.add("ARQ")
        itemsCampos.add("ITI")

        //Crear filtros de buscar
        comboCampos1.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemsCampos)
        comboCampos2.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemsCampos)

        //Insertar todo en layout
        layin.orientation= LinearLayout.VERTICAL
        layin.addView(nom)
        nom.setText(can!!.getNombre())
        layin.addView(escu)
        escu.setText(can!!.getEscuela())
        layin.addView(tel)
        tel.setText(can!!.getTelefono())
        layin.addView(comboCampos1)
        layin.addView(comboCampos2)
        for(i in itemsCampos){
            if (i==can!!.getCarrera1()){
                comboCampos1.setSelection(itemsCampos.indexOf(i))
            }
            if (i==can!!.getCarrera2()){
                comboCampos2.setSelection(itemsCampos.indexOf(i))
            }
        }//for
        layin.addView(emai)
        emai.setText(can!!.getCorreo())

        //Mostrar en alert
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("Actualice la información del candidato")
            .setView(layin)
            .setPositiveButton("Actualizar"){d,i->
                var resUpdate= Candidato(this).actualizar(idSel, nom, escu, tel, comboCampos1,comboCampos2,emai)
                if(resUpdate){
                    Toast.makeText(this,"Se actualizó correctamente",Toast.LENGTH_LONG).show()
                    nom.isEnabled=false
                    escu.isEnabled=false
                    tel.isEnabled=false
                    comboCampos1.isEnabled=false
                    comboCampos2.isEnabled=false
                    emai.isEnabled=false
                    onRestart()
                }else{
                    AlertDialog.Builder(this).setMessage("No se actualizó").show()
                }
            }
            .setNeutralButton("Cancelar"){d,i->}
            .show()

    }


    override fun onRestart() {
        super.onRestart()
        mostrar()
    }
}//class