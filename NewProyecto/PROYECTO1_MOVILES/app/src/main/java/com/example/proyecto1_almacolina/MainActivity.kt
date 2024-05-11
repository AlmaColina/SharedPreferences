
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto1_almacolina.RecentSearchesAdapter
import com.example.proyecto1_almacolina.RetrofitInstance
import com.example.proyecto1_almacolina.WordResult
import com.example.proyecto1_almacolina.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MeaningAdapter
    lateinit var recentSearchesAdapter: RecentSearchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el RecyclerView para mostrar las palabras buscadas recientemente
        recentSearchesAdapter = RecentSearchesAdapter()
        binding.recentSearchesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.recentSearchesRecyclerView.adapter = recentSearchesAdapter


        binding.searchBtn.setOnClickListener {
            val word = binding.searchInput.text.toString()
            getMeaning(word)
            saveRecentSearch(word)
        }

        adapter = MeaningAdapter(emptyList())
        binding.meaningRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.meaningRecyclerView.adapter = adapter
    }

    private fun getMeaning(word : String){
        setInProgress(true)
        GlobalScope.launch {
            try {
                val response = RetrofitInstance.dictionaryApi.getMeaning(word)
                if(response.body()==null){
                    throw (Exception())
                }
                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let {
                        setUI(it)
                    }
                }
            }catch (e : Exception){
                runOnUiThread{
                    setInProgress(false)
                    Toast.makeText(applicationContext,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveRecentSearch(word: String) {
        val sharedPreferences = getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val recentSearches = sharedPreferences.getStringSet("recent_searches", HashSet<String>()) ?: HashSet()
        recentSearches.add(word)

        if (recentSearches.size > 3) {
            val iterator = recentSearches.iterator()
            repeat(recentSearches.size - 3) {
                iterator.next()
                iterator.remove()
            }
        }
        editor.putStringSet("recent_searches", recentSearches)
        editor.apply()
    }

    private fun showRecentSearches() {
        val sharedPreferences = getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
        val recentSearches = sharedPreferences.getStringSet("recent_searches", HashSet<String>()) ?: HashSet()

        // Actualizar el RecyclerView de palabras buscadas recientemente
        recentSearchesAdapter.updateData(recentSearches.toList())
    }

    private fun setUI(response : WordResult){
        binding.wordTextview.text = response.word
        binding.phoneticTextview.text = response.phonetic
        adapter.updateNewData(response.meanings)
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.searchBtn.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.searchBtn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }
    }
}
