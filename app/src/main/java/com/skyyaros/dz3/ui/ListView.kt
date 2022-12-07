package com.skyyaros.dz3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.skyyaros.dz3.entity.RickPersonalWithLocation
import kotlinx.coroutines.flow.Flow
import com.skyyaros.dz3.R
import kotlinx.coroutines.launch

@Composable
fun ListView(mainViewModel: MainViewModel, items: LazyPagingItems<RickPersonalWithLocation>, lazyListState: LazyListState, onCharacterClick: (Int) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    mainViewModel.pageDataSnapShot = items.itemSnapshotList.items
    coroutineScope.launch {
        lazyListState.scrollToItem(mainViewModel.lastIndex, mainViewModel.lastOffset)
    }
    LazyColumn(state = lazyListState) {
        items(items) {
            it?.let { ListItem(it, onCharacterClick) } ?: Text(text = "Ooops")
        }
        items.apply {
            when {
                loadState.refresh is LoadState.NotLoading && items.itemCount < 1 -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Ничего не найдено",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.append is LoadState.Loading -> {
                    mainViewModel.lastIndex = lazyListState.firstVisibleItemIndex
                    mainViewModel.lastOffset = lazyListState.firstVisibleItemScrollOffset
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = items.loadState.refresh as LoadState.Error
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            e.error.localizedMessage?.let { Text(text = it) }
                            Button(onClick = { retry() }) {
                                Text(text = "Retry")
                            }
                        }
                    }
                }
                loadState.append is LoadState.Error -> {
                    mainViewModel.lastIndex = lazyListState.firstVisibleItemIndex
                    mainViewModel.lastOffset = lazyListState.firstVisibleItemScrollOffset
                    val e = items.loadState.append as LoadState.Error
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            e.error.localizedMessage?.let { Text(text = it) }
                            Button(onClick = { retry() }) {
                                Text(text = "Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(rickPersonalWithLocation: RickPersonalWithLocation, onCharacterClick: (Int) -> Unit) {
    Card(
        backgroundColor = Color(0xFF26282F/*0x802140AD*/),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clickable { onCharacterClick(rickPersonalWithLocation.id) },
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(color = Color(0xFF26282F/*0x802140AD*/))
        ) {
            GlideImageWithPreview(data = rickPersonalWithLocation.image, Modifier.size(170.dp))
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = rickPersonalWithLocation.name,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${rickPersonalWithLocation.status} - ${rickPersonalWithLocation.species}",
                        maxLines = 1,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    text = "Last known location:",
                    maxLines = 1,
                    fontSize = 14.sp
                )
                Text(
                    text = rickPersonalWithLocation.location.name,
                    maxLines = 1,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${rickPersonalWithLocation.type}, ${rickPersonalWithLocation.dimension}",
                    maxLines = 1,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}