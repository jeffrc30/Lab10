package cr.ac.menufragment

import retrofit2.HttpException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cr.ac.una.controlfinancierocamera.R
import cr.ac.una.controlfinancierocamera.adapter.BuscadorAdapter
import cr.ac.una.controlfinancierocamera.controller.PageController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListControlFinancieroFragment : Fragment() {

    private lateinit var buscadorAdapter: BuscadorAdapter
    val pageController = PageController();
    private lateinit var botonBuscar: Button
    private lateinit var buscadorView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_control_financiero, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //cambio
        super.onViewCreated(view, savedInstanceState)

        botonBuscar = view.findViewById<Button>(R.id.botonIngresar)
        buscadorView = view.findViewById(R.id.buscadorView)

        botonBuscar.setOnClickListener {
            var textoBusqueda = buscadorView.query.toString()
            textoBusqueda = textoBusqueda.replace(" ", "_")
            Log.d("TextoBusqueda", textoBusqueda)
            insertEntity(textoBusqueda)
        }

        val listView: ListView = view.findViewById(R.id.listaMovimientos)
        buscadorAdapter = BuscadorAdapter(requireContext(), mutableListOf())
        listView.adapter = buscadorAdapter
    }

    private fun insertEntity(textoBusqueda: String) {
        lifecycleScope.launch {
            try {
                val resultadoBusqueda = withContext(Dispatchers.IO) {
                    pageController.Buscar(textoBusqueda)
                }
                withContext(Dispatchers.Main) {
                    Log.d("ResultadoBusqueda", resultadoBusqueda.toString())
                    buscadorAdapter.clear()
                    buscadorAdapter.addAll(resultadoBusqueda)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Log.e("HTTP_ERROR", "Error: ${e.message}")
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ERROR", "Error: ${e.message}")
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}