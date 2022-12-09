package com.adamkapus.hikingapp.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.adamkapus.hikingapp.domain.model.map.Route

object RouteComparator : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.id == newItem.id
    }
}