package com.adamkapus.hikingapp.utils

import android.content.Context
import com.adamkapus.hikingapp.R
import javax.inject.Inject

class FlowerResolver @Inject constructor(val context: Context) {
    private var flowerDisplayNameMap = HashMap<String, Int>() // [label név, res ID]
    private var flowerFlowerRarityMap = HashMap<String, FlowerRarity>() // [label név, Ritkaság]

    init {
        flowerDisplayNameMap["bluebell"] = R.string.flower_name_bluebell
        flowerFlowerRarityMap["bluebell"] = FlowerRarity.COMMON

        flowerDisplayNameMap["buttercup"] = R.string.flower_name_buttercup
        flowerFlowerRarityMap["buttercup"] = FlowerRarity.COMMON

        flowerDisplayNameMap["colts foot"] = R.string.flower_name_colts_foot
        flowerFlowerRarityMap["colts foot"] = FlowerRarity.COMMON

        flowerDisplayNameMap["cowslip"] = R.string.flower_name_cowslip
        flowerFlowerRarityMap["cowslip"] = FlowerRarity.RARE

        flowerDisplayNameMap["crocus"] = R.string.flower_name_crocus
        flowerFlowerRarityMap["crocus"] = FlowerRarity.SUPER_RARE

        flowerDisplayNameMap["daffodil"] = R.string.flower_name_daffodil
        flowerFlowerRarityMap["daffodil"] = FlowerRarity.COMMON

        flowerDisplayNameMap["daisy"] = R.string.flower_name_daisy
        flowerFlowerRarityMap["daisy"] = FlowerRarity.COMMON

        flowerDisplayNameMap["dandelion"] = R.string.flower_name_dandelion
        flowerFlowerRarityMap["dandelion"] = FlowerRarity.COMMON

        flowerDisplayNameMap["fritillary"] = R.string.flower_name_fritillary
        flowerFlowerRarityMap["fritillary"] = FlowerRarity.SUPER_RARE

        flowerDisplayNameMap["iris"] = R.string.flower_name_iris
        flowerFlowerRarityMap["iris"] = FlowerRarity.RARE

        flowerDisplayNameMap["lily valley"] = R.string.flower_name_lily_valley
        flowerFlowerRarityMap["lily valley"] = FlowerRarity.RARE

        flowerDisplayNameMap["pansy"] = R.string.flower_name_pansy
        flowerFlowerRarityMap["pansy"] = FlowerRarity.COMMON

        flowerDisplayNameMap["snowdrop"] = R.string.flower_name_snowdrop
        flowerFlowerRarityMap["snowdrop"] = FlowerRarity.SUPER_RARE

        flowerDisplayNameMap["sunflower"] = R.string.flower_name_sunflower
        flowerFlowerRarityMap["sunflower"] = FlowerRarity.RARE

        flowerDisplayNameMap["tiger lily"] = R.string.flower_name_tiger_lily
        flowerFlowerRarityMap["tiger lily"] = FlowerRarity.SUPER_RARE


        flowerDisplayNameMap["tulip"] = R.string.flower_name_tulip
        flowerFlowerRarityMap["tulip"] = FlowerRarity.RARE

        flowerDisplayNameMap["windflower"] = R.string.flower_name_windflower
        flowerFlowerRarityMap["windflower"] = FlowerRarity.RARE

    }

    fun getDisplayName(label: String?): String {
        val resId = flowerDisplayNameMap[label] ?: R.string.flower_name_default
        return context.getString(resId)
    }

    fun getRarity(label: String?): FlowerRarity {
        return flowerFlowerRarityMap[label] ?: FlowerRarity.COMMON
    }


}

enum class FlowerRarity {
    COMMON,
    RARE,
    SUPER_RARE
}