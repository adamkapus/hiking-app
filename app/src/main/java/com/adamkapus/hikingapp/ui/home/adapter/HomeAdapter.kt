package com.adamkapus.hikingapp.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adamkapus.hikingapp.databinding.RouteItemBinding
import com.adamkapus.hikingapp.domain.model.map.Route

class HomeAdapter() :
    ListAdapter<Route, HomeAdapter.ViewHolder>(RouteComparator) {
    private lateinit var binding: RouteItemBinding
    private var routeList = emptyList<Route>()

    private var itemClickListener: ((route: Route) -> Unit)? = null

    fun setOnItemClickListener(listener: ((news: Route) -> Unit)?) {
        itemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RouteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = routeList[position]

        holder.news = route
        holder.name.text = route.name
    }


    fun replaceList(newList: List<Route>) {
        Log.d("PLS", "NEWLIST ADAOTERBEN" + newList.toString())
        routeList = newList
        submitList(routeList)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = binding.routeName
        var news: Route? = null

        init {
            itemView.setOnClickListener {
                news?.let { itemClickListener?.let { it1 -> it1(it) } }
            }
        }
    }
}