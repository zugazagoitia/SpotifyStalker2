package com.zugazagoitia.spotifystalker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.imageComponent
import com.skydoves.landscapist.fresco.FrescoImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.zugazagoitia.spotifystalker.data.FriendListDatasource
import com.zugazagoitia.spotifystalker.data.FriendListRepository
import com.zugazagoitia.spotifystalker.data.IFriendListRepository
import com.zugazagoitia.spotifystalker.data.LoginRepository
import com.zugazagoitia.spotifystalker.model.*
import com.zugazagoitia.spotifystalker.ui.theme.SpotifyStalkerTheme
import com.zugazagoitia.spotifystalker2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class FriendList : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val repository =
            FriendListRepository.getInstance(FriendListDatasource(LoginRepository.user!!))!!

        val pipelineConfig =
            OkHttpImagePipelineConfigFactory
                .newBuilder(this, OkHttpClient.Builder().build())
                .setDiskCacheEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .build()

        Fresco.initialize(this, pipelineConfig)

        setContent {
            SpotifyStalkerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Container(repository)
                }
            }
        }
    }


    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Container(friendListRepository: IFriendListRepository) {

        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        val list = remember { mutableStateListOf<UserPlayingInfo>() }

        fun refresh() {
            //TODO: Add loading bar
            coroutineScope.launch(Dispatchers.IO) {
                val temp = friendListRepository.updateFriendList()
                list.clear()
                list.addAll(temp)
            }
        }

        refresh()

        Scaffold(
            topBar = { TopBar { refresh() } },
            content = { innerPadding ->
                Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
                LazyColumn(
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list.size) { index ->
                        FriendItem(list[index])
                    }
                }
            }

        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(refresh: () -> Unit) {
        MediumTopAppBar(
            title = {
                Text(
                    stringResource(R.string.topBarText),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* TODO: Menu */ }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(R.string.menuIconDescription)
                    )
                }
            },
            actions = {
                IconButton(onClick = { refresh() }) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.refreshIconDescription)
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }


    private fun timeStampToDisplayString(timestamp: Long): String {
        val ONE_MINUTE = 60000L
        val ONE_HOUR = 3600000L
        val ONE_DAY = 86400000L
        val ONE_MONTH = 2592000000L
        val ONE_YEAR = 31536000000L
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - timestamp
        return if (difference < ONE_MINUTE) {
            "Just now"
        } else if (difference < ONE_HOUR) {
            val timeAgo = difference / ONE_MINUTE
            val finalUnits = timeAgo.toInt()
            "$finalUnits min"
        } else if (difference < ONE_DAY) {
            val timeAgo = difference / ONE_HOUR
            val finalUnits = timeAgo.toInt()
            "$finalUnits hr"
        } else if (difference < ONE_MONTH) {
            val timeAgo = difference / ONE_DAY
            val finalUnits = timeAgo.toInt()
            "$finalUnits d"
        } else if (difference < ONE_YEAR) {
            val timeAgo = difference / ONE_MONTH
            val finalUnits = timeAgo.toInt()
            "$finalUnits M"
        } else {
            val timeAgo = difference / ONE_MONTH
            val finalUnits = timeAgo.toInt()
            finalUnits.toString() + "" + "M"
        }
    }

    @Composable
    fun DataRow(
        icon: ImageVector,
        iconDescription: String,
        value: String,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = value)
        }


    }

    @Composable
    fun FriendItem(userPlayingInfo: UserPlayingInfo) {
        Card(
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    ProfilePic(userPlayingInfo)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = userPlayingInfo.user!!.name!!,
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = "${timeStampToDisplayString(userPlayingInfo.timestamp)} ago", //TODO: Extract string
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    IconButton(
                        onClick = { /*TODO: Open song menu*/ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.StarBorder,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                DataRow(
                    icon = Icons.Filled.MusicNote,
                    iconDescription = stringResource(R.string.songIcon),
                    value = userPlayingInfo.track!!.name!!
                )

                DataRow(
                    icon = Icons.Filled.Album,
                    iconDescription = stringResource(R.string.albumIcon),
                    value = userPlayingInfo.track!!.album!!.name!!
                )
                DataRow(
                    icon = Icons.Filled.QueueMusic,
                    iconDescription = stringResource(R.string.playlistIcon),
                    value = userPlayingInfo.track!!.context!!.name!!
                )
            }
        }


        /* Update from this:
        viewHolder.userUri = beans.getUser().getUri();
        viewHolder.albumUri = beans.getTrack().getAlbum().getUri();
        viewHolder.trackUri = beans.getTrack().getUri();
        viewHolder.contextUri = beans.getTrack().getContext() == null ? "null" : beans.getTrack().getContext().getUri();
         */


    }


    @Composable
    private fun ProfilePic(userPlayingInfo: UserPlayingInfo) {
        FrescoImage(
            imageUrl = userPlayingInfo.user!!.imageUrl ?: "",
            imageOptions = ImageOptions(
                contentDescription = buildString {
                    append(userPlayingInfo.user!!.name!!)
                    append(stringResource(R.string.userProfilePicture))
                },
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
            ),
            previewPlaceholder = R.drawable.resource_default,
            loading = {
                Box(Modifier.matchParentSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            },
            failure = {
                painterResource(R.drawable.resource_default)
            },
            component = imageComponent {
                +PlaceholderPlugin.Failure(painterResource(id = R.drawable.resource_default))
            },
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
    }

    @Composable
    @Preview(showBackground = true)
    fun DefaultPreview() {

        //Mock repo for previewing
        val repo = object : IFriendListRepository {
            override fun updateFriendList(): List<UserPlayingInfo> {
                return listOf(
                    UserPlayingInfo(
                        System.currentTimeMillis() - 60000L,
                        User(
                            "http://google.com",
                            "User1",
                            "https://randomuser.me/api/portraits/women/1.jpg"
                        ),
                        Track(
                            "uri",
                            "song",
                            "imageurl",
                            Album("https://i.imgur.com/3ZQ3Y7Q.jpg", "album"),
                            Artist("uri", "artist"),
                            Context("uri", "context", 1)
                        )
                    ),
                    UserPlayingInfo(
                        System.currentTimeMillis() - 600000L,
                        User(
                            "http://google.com",
                            "i have a HUGE username :)",
                            "https://randomuser.me/api/portraits/women/2.jpg"
                        ),
                        Track(
                            "uri",
                            "very very very long song name",
                            "imageurl",
                            Album(
                                "https://i.imgur.com/3ZQ3Y7Q.jpg",
                                "this is a reaaaaally long album name"
                            ),
                            Artist("uri", "very very long artist name"),
                            Context("uri", "this playlist is a fucking pain in the ass", 1)
                        )
                    ),
                    UserPlayingInfo(
                        System.currentTimeMillis() - 300000L,
                        User(
                            "http://google.com",
                            "User3",
                            "https://randomuser.me/api/portraits/women/3.jpg"
                        ),
                        Track(
                            "uri",
                            "song",
                            "imageurl",
                            Album("https://i.imgur.com/3ZQ3Y7Q.jpg", "album"),
                            Artist("uri", "artist"),
                            Context("uri", "context", 1)
                        )
                    ),
                    UserPlayingInfo(
                        System.currentTimeMillis() - 6000000L,
                        User(
                            "http://google.com",
                            "User3",
                            "https://failed.dom.ain"
                        ),
                        Track(
                            "uri",
                            "song",
                            "imageurl",
                            Album("https://i.imgur.com/3ZQ3Y7Q.jpg", "album"),
                            Artist("uri", "artist"),
                            Context("uri", "context", 1)
                        )
                    )


                )
            }
        }

        SpotifyStalkerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Container(repo)
            }
        }
    }
}
