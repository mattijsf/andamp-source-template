// SPDX-License-Identifier: Apache-2.0

package nl.mattix.andamp.pack.template

import nl.mattix.andamp.core.model.BrowseCapabilities
import nl.mattix.andamp.core.model.LibraryAlbum
import nl.mattix.andamp.core.model.LibraryArtist
import nl.mattix.andamp.core.model.Track
import nl.mattix.andamp.core.playback.BrowseSource

/**
 * The library of this source: one artist, one album, one song.
 *
 * A real source asks a server here. Every track's address starts with this
 * source's scheme; the player hands the address back when the track is played.
 */
internal class TemplateLibrary : BrowseSource {
    override val capabilities = BrowseCapabilities(hasArtists = true, hasAlbums = true)

    /** Always true: there is no sign-in and no server. */
    override val available = true

    override suspend fun artists(): List<LibraryArtist> =
        listOf(LibraryArtist(id = ARTIST, name = ARTIST, albumCount = 1, trackCount = 1))

    override suspend fun albums(artistId: String?): List<LibraryAlbum> =
        if (artistId == null || artistId == ARTIST) {
            listOf(LibraryAlbum(id = ALBUM, title = "An album", artist = ARTIST, trackCount = 1))
        } else {
            emptyList()
        }

    override suspend fun tracks(albumId: String): List<Track> = if (albumId == ALBUM) listOf(SONG) else emptyList()

    companion object {
        private const val ARTIST = "An artist"
        private const val ALBUM = "album-1"

        /**
         * The audio URL of each row, by the id in its address. The example URL
         * does not exist; use any file `MediaExtractor` can open over HTTP.
         */
        private val AUDIO = mapOf("1" to "https://example.com/a-song.mp3")

        val SONG =
            Track(
                id = "${SourceIdentity.SCHEME}:track:1",
                artist = ARTIST,
                title = "A song",
                durationMs = 180_000,
                uri = "${SourceIdentity.SCHEME}:track:1",
            )

        /** The URL a row plays from, or null when the row is not one of this source's. */
        fun audioOf(track: Track): String? {
            val address = track.uri ?: return null
            val prefix = "${SourceIdentity.SCHEME}:track:"
            return if (address.startsWith(prefix)) AUDIO[address.removePrefix(prefix)] else null
        }
    }
}
