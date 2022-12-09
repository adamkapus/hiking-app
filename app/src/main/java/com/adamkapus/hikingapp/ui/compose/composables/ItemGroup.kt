package com.adamkapus.hikingapp.ui.compose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppDimens
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors

typealias ComposableFun = @Composable () -> Unit

@Composable
fun ItemGroup(
    itemList: List<ComposableFun>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(MaterialTheme.hikingAppDimens.inputMinHeight)
            .background(MaterialTheme.hikingappColors.primaryLightColor)
    ) {
        for ((i, item) in itemList.withIndex()) {
            item()
            if (i != itemList.lastIndex) {
                Divider(
                    color = MaterialTheme.hikingappColors.primaryTextColor,
                    thickness = MaterialTheme.hikingAppDimens.dividerHeight
                )
            }
        }
    }
}