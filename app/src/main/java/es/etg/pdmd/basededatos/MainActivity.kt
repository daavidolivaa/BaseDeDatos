package es.etg.pdmd.basededatos

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import es.etg.pdmd.basededatos.data.ClienteDatabase
import es.etg.pdmd.basededatos.data.ClienteEntity
import es.etg.pdmd.basededatos.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var database: ClienteDatabase
        const val DATABASE_NAME = "cliente-db"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainActivity.database = Room.databaseBuilder(
            this,
            ClienteDatabase::class.java,
            DATABASE_NAME
        ).build()
    }


    fun guardar(view: View) {

        val nombre: String = binding.etNombre.text.toString()
        val apellidos: String = binding.etApellidos.text.toString()
        val cliente = ClienteEntity(0, nombre, apellidos);
        val clienteDao = database.clienteDao()

        //En el disppatcher IO es para entradas y salidas: bases de datos, ficheros, redes...
        CoroutineScope(Dispatchers.IO).launch {
            clienteDao.insert(cliente)
        }

    }

    fun mostrar(view: View) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ArrayList<String>())
        val lv: ListView = binding.lvListado

        lv.adapter = adapter
        loadData(adapter)
    }

    fun loadData(adapter: ArrayAdapter<String>) {
        val datos = ArrayList<String>()
        //En el disppatcher IO es para entradas y salidas: bases de datos, ficheros, redes...
        CoroutineScope(Dispatchers.IO).launch {
            val clienteDao = database.clienteDao()
            val clientes = clienteDao.getAll()
            clientes.forEach { cliente ->
                datos.add("Nombre ${cliente.nombre} y apellidos ${cliente.apellidos}")
            }
            //Lo siguiente, que es un actualización de la vista, lo ejecutamos en el hilo principal
            //Cambiamos el contexto
            withContext(Dispatchers.Main) {
                adapter.addAll(datos)
                adapter.notifyDataSetChanged()
            }
        }
    }
}