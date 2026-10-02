// SPDX-License-Identifier: Apache-2.0

package nl.mattix.andamp.pack.template

import nl.mattix.andamp.core.model.BrowseCapabilities
import nl.mattix.andamp.core.model.Capabilities
import nl.mattix.andamp.core.packapi.PackDescriptor
import nl.mattix.andamp.core.playback.PcmProvider

/** What this source tells the player about itself: its scheme, its label and its pages. */
internal object SourceIdentity {
    /**
     * The first word of every address of this source, as in
     * `template:track:<id>`. The player routes a row by it and saved playlists
     * record it, so choose yours before the first release and do not change it
     * after.
     */
    const val SCHEME = "template"

    /** What the player calls this source in Preferences and the Media Library. */
    const val LABEL = "Template"

    /** The name of the launcher alias; it has to equal the alias's `android:name` in the manifest. */
    const val LAUNCHER_ALIAS = "nl.mattix.andamp.pack.template.LauncherEntry"

    /**
     * The URL of a file you publish, `{"version": "1.1.0", "page": "https://..."}`.
     * The player reads it to tell the listener about a newer version. Empty
     * means no update check.
     */
    const val UPDATES = ""

    /** The page a listener gets this source from, or empty. */
    const val HOME = ""

    fun descriptor(
        version: String,
        playback: Capabilities,
        browse: BrowseCapabilities,
    ): PackDescriptor =
        PackDescriptor(
            scheme = SCHEME,
            label = LABEL,
            version = version,
            canSeek = playback.canSeek,
            canEditQueue = playback.canEditQueue,
            canAttenuate = playback.canAttenuate,
            hasArtists = browse.hasArtists,
            hasAlbums = browse.hasAlbums,
            canSearch = browse.canSearch,
            hasPlaylists = browse.hasPlaylists,
            hasCatalogue = browse.hasCatalogue,
            skinnable = true,
            updates = UPDATES,
            home = HOME,
            // the decoded audio goes to the player, which applies its
            // equalizer, effects and visualizer
            handsOverAudio = true,
            sampleRate = PcmProvider.SAMPLE_RATE_HZ,
            channels = PcmProvider.CHANNELS,
        )
}
