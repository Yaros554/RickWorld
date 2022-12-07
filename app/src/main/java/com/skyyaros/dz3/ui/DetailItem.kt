package com.skyyaros.dz3.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.skyyaros.dz3.R
import com.skyyaros.dz3.entity.RickPersonalWithLocation

@Composable
fun DetailItem(mainViewModel: MainViewModel, id: Int) {
    val rickPersonalWithLocation = mainViewModel.pageDataSnapShot.first { it.id == id }
    Column(
        Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(start = 8.dp, end = 8.dp)
    ) {
        GlideImageWithPreview(
            data = rickPersonalWithLocation.image,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = rickPersonalWithLocation.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
        )
        Row(Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
            (0..255).forEach { item ->
                Spacer(
                    modifier = Modifier
                        .weight(1.0f/256)
                        .height(2.dp)
                        .background(Color(255 - item, 255 - item, 255 - item))
                )
            }
        }

        Text(
            text = "Live status:",
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(127, 127, 127)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Image(
                painter = painterResource(
                    if (rickPersonalWithLocation.status == "Alive")
                        R.drawable.circle_green
                    else if (rickPersonalWithLocation.status == "Dead")
                        R.drawable.circle_red
                    else
                        R.drawable.circle_yellow
                ),
                contentDescription = "green",
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = rickPersonalWithLocation.status,
                fontSize = 20.sp
            )
        }

        Text(
            text = "Species and gender:",
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            color = Color(127, 127, 127)
        )
        Text(
            text = "${rickPersonalWithLocation.species} (${rickPersonalWithLocation.gender})",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp)
        )

        Text(
            text = "Last known location:",
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            color = Color(127, 127, 127)
        )
        Text(
            text = "${rickPersonalWithLocation.location.name} (${rickPersonalWithLocation.type})",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp)
        )

        Text(
            text = "First seem in:",
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            color = Color(127, 127, 127)
        )
        Text(
            text = rickPersonalWithLocation.episodes[0].name,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp)
        )

        Text(
            text = "Episodes:",
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            fontWeight = FontWeight.Bold,
        )
        rickPersonalWithLocation.episodes.forEach {
            Card(
                backgroundColor = Color(0xFF26282F),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.background(color = Color(0xFF26282F)).padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Color(0xFF26282F))
                            .padding(start = 16.dp)
                            .weight(0.8f, true)
                    ) {
                        Text(
                            text = it.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = it.date,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = it.unicCode,
                        fontSize = 16.sp,
                        color = Color(127, 127, 127),
                        modifier = Modifier.padding(end = 8.dp).weight(0.2f, true)
                    )
                }
            }
        }
    }
}