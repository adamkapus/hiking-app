package com.adamkapus.hikingapp.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.adamkapus.hikingapp.domain.model.map.Route

object RouteComparator : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.points == newItem.points && oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem == newItem
    }
}