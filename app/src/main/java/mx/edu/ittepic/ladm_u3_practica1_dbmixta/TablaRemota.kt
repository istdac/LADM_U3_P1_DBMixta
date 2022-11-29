package mx.edu.ittepic.ladm_u3_practica1_dbmixta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.databinding.ActivityTablaRemotaBinding
import mx.edu.ittepic.ladm_u3_practica1_dbmixta.logica.Candidato

class TablaRemota : AppCompatActivity() {
    lateinit var binding: ActivityTablaRemotaBinding
    var listaIds = ArrayList<String>()
    var fechas = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTablaRemotaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mostrarTodo()
        binding.filtro.isVisible=false
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
        }
        binding.filtro.setOnClickListener {
            binding.filtro.isVisible=false
            binding.filtro.isSelected=false
            mostrarTodo()
        }

    }

    private fun consulta(comboCampos: Spinner, claveBusqueda: EditText) {
        var posicionCampoSeleccionado = comboCampos.selectedItemId.toInt()
        when(posicionCampoSeleccionado){
            0->{
                FirebaseFirestore.getInstance().collection("candidatos")
                .whereEqualTo("carrera1",claveBusqueda.text.toString().uppercase())
                    .get()
                    .addOnSuccessListener {
                        var resul = ArrayList<String>()
                        for(doc in it!!){
                            var cad=doc.getString("nombre")+" : " + doc.getString("correo")+ "\n"+doc.getString("escuela")+"\n"+doc.getString("telefono")+"\n"+doc.getString("carrera1")+" -- "+doc.getString("carrera2")
                            resul.add(cad)
                        }//for
                        binding.tablaRemota.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,resul)
                    }//success
            }
            1->{
                FirebaseFirestore.getInstance().collection("candidatos")
                    .whereEqualTo("carrera2",claveBusqueda.text.toString().uppercase())
                    .get()
                    .addOnSuccessListener {
                        var resul = ArrayList<String>()
                        for(doc in it!!){
                            var cad=doc.getString("nombre")+" : " + doc.getString("correo")+ "\n"+doc.getString("escuela")+"\n"+doc.getString("telefono")+"\n"+doc.getString("carrera1")+" -- "+doc.getString("carrera2")
                            resul.add(cad)
                        }//for
                        binding.tablaRemota.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,resul)
                    }//success

            }
            2->{
                FirebaseFirestore.getInstance().collection("candidatos")
                    .whereEqualTo("escuela",claveBusqueda.text.toString())
                    .get()
                    .addOnSuccessListener {
                        var resul = ArrayList<String>()
                        for(doc in it!!){
                            var cad=doc.getString("nombre")+" : " + doc.getString("correo")+ "\n"+doc.getString("escuela")+"\n"+doc.getString("telefono")+"\n"+doc.getString("carrera1")+" -- "+doc.getString("carrera2")
                            resul.add(cad)
                        }//for
                        binding.tablaRemota.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,resul)
                    }//success

            }
            3->{
                FirebaseFirestore.getInstance().collection("candidatos")
                    .whereEqualTo("fecha",claveBusqueda.text.toString())
                    .get()
                    .addOnSuccessListener {
                        var resul = ArrayList<String>()
                        for(doc in it!!){
                            var cad=doc.getString("nombre")+" : " + doc.getString("correo")+ "\n"+doc.getString("escuela")+"\n"+doc.getString("telefono")+"\n"+doc.getString("carrera1")+" -- "+doc.getString("carrera2")
                            resul.add(cad)
                        }//for
                        binding.tablaRemota.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,resul)
                    }//success

            }
        }
    }

    fun mostrarTodo(){
        FirebaseFirestore.getInstance().collection("candidatos")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    aler("No se ha podido realizar la consulta")
                    return@addSnapshotListener
                }
                var lista = ArrayList<String>()
                listaIds.clear()

                for(doc in querySnapshot!!){
                    var cad=doc.getString("nombre")+" : " + doc.getString("correo")+ "\n"+doc.getString("escuela")+"\n"+doc.getString("telefono")+"\n"+doc.getString("carrera1")+" -- "+doc.getString("carrera2")
                    lista.add(cad)
                    listaIds.add(doc.id)
                    fechas.add(doc.getString("fecha").toString())
                }
                binding.tablaRemota.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista)
                println(listaIds)

            }//snapshotlistenre
        binding.tablaRemota.setOnItemClickListener { adapterView, view, i, l ->
            var idSel = listaIds.get(i)
            AlertDialog.Builder(this).setTitle("Atención")
                .setMessage("¿Qué desea hacer?")
                .setPositiveButton("Eliminar"){d,ia->
                    eliminar(idSel)
                }
                .setNeutralButton("Actualizar"){d,ia->
                    actualizarAlert(idSel)
                }
                .setNegativeButton("Nada"){d,ia->}
                .show()

        }//itemclick
    }//mostrarTodo

    fun eliminar(id:String){
        FirebaseFirestore.getInstance().collection("candidatos")
            .document(id).delete()
            .addOnSuccessListener {
                toast("Se eliminó exitosamente")
                mostrarTodo()//actualizar listaIds
            }
            .addOnFailureListener { aler(it.message!!) }
    }
    fun actualizarAlert(idSel:String){
        //var can = Candidato(this).getByID(idSel)
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
        layin.addView(escu)
        layin.addView(tel)
        layin.addView(comboCampos1)
        layin.addView(comboCampos2)

        layin.addView(emai)

        var can = FirebaseFirestore.getInstance().collection("candidatos").document(idSel).get().addOnSuccessListener {
            nom.setText(it.getString("nombre"))
            escu.setText(it.getString("escuela"))
            tel.setText(it.getString("telefono"))
            emai.setText(it.getString("correo"))
            for(i in itemsCampos){
                if (i==it.getString("carrera1")){
                    comboCampos1.setSelection(itemsCampos.indexOf(i))
                }
                if (i==it.getString("carrera2")){
                    comboCampos2.setSelection(itemsCampos.indexOf(i))
                }
            }//for
        }

        //Mostrar en alert
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("Actualice la información del candidato")
            .setView(layin)
            .setPositiveButton("Actualizar"){d,i->
                //var resUpdate= Candidato(this).actualizar(idSel, nom, escu, tel, comboCampos1,comboCampos2,emai)
                FirebaseFirestore.getInstance().collection("candidatos").document(idSel).update(
                        "nombre",nom.text.toString(),
                        "escuela",escu.text.toString(),
                        "telefono",tel.text.toString(),
                        "carrera1",comboCampos1.selectedItem.toString(),
                        "carrera2",comboCampos2.selectedItem.toString(),
                        "correo",emai.text.toString(),
                    )
                    .addOnSuccessListener {
                        nom.isEnabled=false
                        escu.isEnabled=false
                        tel.isEnabled=false
                        comboCampos1.isEnabled=false
                        comboCampos2.isEnabled=false
                        emai.isEnabled=false
                        Toast.makeText(this,"Se actualizó correctamente",Toast.LENGTH_LONG).show()

                    }
                    .addOnFailureListener {
                        AlertDialog.Builder(this).setMessage("No se actualizó").show()
                    }
            }
            .setNeutralButton("Cancelar"){d,i->}
            .show()

    }





    private fun toast(m:String){
        Toast.makeText(this,m, Toast.LENGTH_LONG).show()
    }
    fun aler(m:String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }

}