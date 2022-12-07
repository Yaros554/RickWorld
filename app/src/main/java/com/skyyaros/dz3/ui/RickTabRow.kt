package com.skyyaros.dz3.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RickTabRow(currentScreen: RickDestination, onClick: ()->Unit) {
    Surface(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Box {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = if (currentScreen.route == "list_characters") "Rick World" else "Person details",
                    fontSize = 20.sp
                )
            }
            if (currentScreen.route == "character_detail") {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "back",
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp).clickable { onClick() }
                )
            }
        }
    }
}