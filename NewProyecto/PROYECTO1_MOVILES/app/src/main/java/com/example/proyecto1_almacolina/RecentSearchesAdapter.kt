package com.example.proyecto1_almacolina
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecentSearchesAdapter : RecyclerView.Adapter<RecentSearchesAdapter.RecentSearchViewHolder>() {

    private var recentSearches: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_search, parent, false)
        return RecentSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        val recentSearch = recentSearches[position]
        holder.bind(recentSearch)
    }

    override fun getItemCount(): Int {
        return recentSearches.size
    }

    fun updateData(recentSearches: List<String>) {
        this.recentSearches = recentSearches
        notifyDataSetChanged()
    }

    inner class RecentSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val searchWordTextView: TextView = itemView.findViewById(R.id.searchWord)

        fun bind(recentSearch: String) {
            searchWordTextView.text = recentSearch
        }
    }
}