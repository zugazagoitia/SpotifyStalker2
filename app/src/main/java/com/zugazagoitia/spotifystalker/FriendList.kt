package com.zugazagoitia.spotifystalker

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.net.URI


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

        var loading by remember {
            mutableStateOf(true)
        }

        fun refresh() {
            coroutineScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    loading = true
                }

                val temp = friendListRepository.updateFriendList()
                list.clear()
                list.addAll(temp)
                withContext(Dispatchers.Main) {
                    loading = false
                }
            }
        }

        refresh()

        Scaffold(
            topBar = {
                Column() {
                    TopBar { refresh() }
                    if (loading)
                        LinearProgressIndicator(
                            modifier = Modifier
                                .semantics(mergeDescendants = true) {}
                                .fillMaxWidth()
                        )
                }
            },
            content = { innerPadding ->
                Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
                LazyColumn(
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(top = 8.dp)
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


    @Composable
    fun DataRow(
        icon: ImageVector,
        iconDescription: String,
        value: String,
        centerRow: Boolean = false
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
            Text(text = value, Modifier.alpha(if (centerRow) 0.7f else 1f))
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfilePic(userPlayingInfo)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = userPlayingInfo.user?.name ?: "",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = "${timeStampToDisplayString(userPlayingInfo.timestamp)} ago", //TODO: Extract string
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    LinksMenu(userPlayingInfo)
                }

                Spacer(modifier = Modifier.height(8.dp))

                userPlayingInfo.track?.name?.let {
                    DataRow(
                        icon = Icons.Filled.MusicNote,
                        iconDescription = stringResource(R.string.songIcon),
                        value = userPlayingInfo.track?.name ?: ""
                    )
                }
                userPlayingInfo.track?.album?.name?.let {
                    Divider(thickness = Dp.Hairline, modifier = Modifier.padding(all = 2.dp))
                    DataRow(
                        icon = Icons.Filled.Album,
                        iconDescription = stringResource(R.string.albumIcon),
                        value = userPlayingInfo.track?.album?.name ?: "",
                        centerRow = true
                    )
                }
                userPlayingInfo.track?.context?.name?.let {
                    Divider(thickness = Dp.Hairline, modifier = Modifier.padding(all = 2.dp))
                    DataRow(
                        icon = Icons.Filled.QueueMusic,
                        iconDescription = stringResource(R.string.playlistIcon),
                        value = it
                    )
                }

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
    private fun LinksMenu(
        userPlayingInfo: UserPlayingInfo
    ) {
        var menuOpen by remember {
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier
                //.fillMaxSize()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = { menuOpen = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.openInSpotify)) },
                    enabled = false,
                    onClick = { /*None*/ },
                )
                Divider()
                userPlayingInfo.user?.uri?.let {
                    DropdownMenuItem(
                        text = { Text("User Profile") },
                        onClick = { openLinkInSpotify(it) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = stringResource(R.string.profileIcon)
                            )
                        })
                }
                userPlayingInfo.track?.uri?.let {
                    DropdownMenuItem(
                        text = { Text("Song") },
                        onClick = { openLinkInSpotify(it) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.MusicNote,
                                contentDescription = stringResource(R.string.songIcon)
                            )
                        })
                }

                userPlayingInfo.track?.album?.uri?.let {
                    DropdownMenuItem(
                        text = { Text("Album") },
                        onClick = { openLinkInSpotify(it) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Album,
                                contentDescription = stringResource(R.string.albumIcon)
                            )
                        })
                }

                userPlayingInfo.track?.context?.uri?.let {
                    DropdownMenuItem(
                        text = { Text("Playlist") },
                        onClick = { openLinkInSpotify(it) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.QueueMusic,
                                contentDescription = stringResource(R.string.playlistIcon)
                            )
                        })
                }

            }
        }
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
                contentScale = ContentScale.Crop,
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

    private fun openLinkInSpotify(uri: String) {
        try {
            val intent = Intent(ACTION_VIEW, Uri.parse(uri)).apply {
                // The URL should either launch directly in a non-browser app (if it's
                // the default) or in the disambiguation dialog.
                addCategory(CATEGORY_BROWSABLE)
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Only browser apps are available, or a browser is the default.
            // So you can open the URL directly in your app, for example in a
            // Custom Tab.
            val toast =
                Toast.makeText(applicationContext, "Spotify is not installed!", Toast.LENGTH_SHORT)
            toast.show()
        }
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

    fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }
}
