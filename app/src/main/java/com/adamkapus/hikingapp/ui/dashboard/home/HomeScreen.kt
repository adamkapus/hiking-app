package com.adamkapus.hikingapp.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors

@Composable
fun HomeScreen(
modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.hikingappColors.grey01).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.height(80.dp).padding(10.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.hikingappColors.grey02
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.size(48.dp).padding(10.dp),
                    shape = CircleShape,
                    elevation = 2.dp
                ) {
                    Image(
                        painterResource(com.adamkapus.hikingapp.R.drawable.tura),
                            contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(text = "This is an article Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore .")
            }

        }
    }
}