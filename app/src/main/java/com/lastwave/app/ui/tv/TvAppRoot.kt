package com.lastwave.app.ui.tv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.lastwave.app.playback.PlayableTrack
import com.lastwave.app.ui.player.LocalAddToPlaylist
import com.lastwave.app.ui.player.LocalMusicPlayer
import com.lastwave.app.ui.player.PlayerViewModel
import com.lastwave.app.util.LocalIsTv

@Composable
fun TvAppRoot(
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    var playlistTrack by remember { mutableStateOf<PlayableTrack?>(null) }
    val requestAddToPlaylist = remember(viewModel) {
        { track: PlayableTrack ->
            viewModel.prepareCustomPlaylists()
            playlistTrack = track
        }
    }

    CompositionLocalProvider(
        LocalMusicPlayer provides viewModel.player,
        LocalAddToPlaylist provides requestAddToPlaylist,
        LocalIsTv provides true,
    ) {
        TvShell(
            navController = navController,
            playerViewModel = viewModel,
        ) {
            // The TV-native screens install their own tuned specs inside; this
            // outer default is what the reused phone screens (settings,
            // downloads, playlist/album/artist detail) inherit so D-pad focus
            // scrolls them smoothly instead of rubber-banding at the edges.
            com.lastwave.app.ui.tv.focus.PositionFocusedItemInLazyLayout(
                parentFraction = 0.2f,
            ) {
                TvNavHost(navController = navController)
            }
        }
    }
}
