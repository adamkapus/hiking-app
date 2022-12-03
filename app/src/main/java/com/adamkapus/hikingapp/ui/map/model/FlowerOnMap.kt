package com.adamkapus.hikingapp.ui.map.model

import com.adamkapus.hikingapp.data.model.FlowerLocation
import com.adamkapus.hikingapp.utils.FlowerRarity
import com.adamkapus.hikingapp.utils.FlowerResolver

data class FlowerOnMap(
    val name: String? = null, //label név
    val Lat: Float? = null, //pozíció szélességi foka
    val Lng: Float? = null, //pozíció hosszúsági foka
    val imageUrl: String? = null, //Url a virág képére
    val rarity: FlowerRarity? = null, //virág ritkasága
    val displayName: String? = null, //a megjelenítés során használt név
)


fun FlowerLocation.toFlowerOnMap(flowerResolver: FlowerResolver): FlowerOnMap {
    return FlowerOnMap(
        name = this.name,
        Lat = this.Lat,
        Lng = this.Lng,
        imageUrl = this.imageUrl,
        rarity = this.rarity?.let { FlowerRarity.valueOf(it) },
        displayName = flowerResolver.getDisplayName(this.name)
    )
}


