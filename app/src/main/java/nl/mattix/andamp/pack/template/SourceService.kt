// SPDX-License-Identifier: Apache-2.0

package nl.mattix.andamp.pack.template

import kotlinx.coroutines.runBlocking
import nl.mattix.andamp.core.network.SystemNetworkWatch
import nl.mattix.andamp.core.packapi.PackAccount
import nl.mattix.andamp.core.packapi.PackAnswer
import nl.mattix.andamp.core.packapi.PackDescriptor
import nl.mattix.andamp.core.packapi.PackQuestion
import nl.mattix.andamp.core.playback.NetworkWatch
import nl.mattix.andamp.core.playback.PlaybackBackend
import nl.mattix.andamp.pack.common.PackAnswers
import nl.mattix.andamp.pack.common.PackServiceBase
import nl.mattix.andamp.pack.common.stream.StreamPlayback
import nl.mattix.andamp.pack.common.stream.StreamRequest

/**
 * The service the player binds. [PackServiceBase] provides the binder, the
 * service lifetime and the audio pipe. This class supplies what the source is,
 * who is signed in, the library, and where the audio of a row is.
 */
class SourceService : PackServiceBase() {
    private val library = TemplateLibrary()

    /** Answers the player's questions from the library, a page at a time. */
    private val answers = PackAnswers { library }

    /** Whether the phone has a network, so a song whose connection dropped waits for one. */
    private val network: NetworkWatch = SystemNetworkWatch(this)

    override val launcherAlias: String = SourceIdentity.LAUNCHER_ALIAS

    /** The SDK's playback of a URL. `locate` answers where a row's audio is. */
    override fun makeBackend(): PlaybackBackend =
        StreamPlayback.backend(
            tracks = emptyList(),
            startIndex = 0,
            scope = scope,
            out = audio,
            network = network,
            locate = { track, _ -> TemplateLibrary.audioOf(track)?.let { StreamRequest(it) } },
        )

    override fun descriptor(): PackDescriptor =
        SourceIdentity.descriptor(
            version = BuildConfig.VERSION_NAME,
            playback = StreamPlayback.RENDERING,
            browse = library.capabilities,
        )

    /** This source has no sign-in, so it is always signed in. A real source reads this from what it keeps on the phone. */
    override fun whoIsHere(): PackAccount = PackAccount(signedIn = true)

    /** Called on a binder thread and expected to block until there is an answer. */
    override fun answer(question: PackQuestion): PackAnswer? = runBlocking { answers.to(question) }
}
