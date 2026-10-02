// SPDX-License-Identifier: Apache-2.0

package nl.mattix.andamp.pack.template

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TemplateLibraryTest {
    private val library = TemplateLibrary()

    @Test
    fun `an artist's album leads to its song`() =
        runTest {
            val artist = library.artists().single()
            val album = library.albums(artist.id).single()
            assertEquals(listOf(TemplateLibrary.SONG), library.tracks(album.id))
        }

    @Test
    fun `every row is addressed with this source's scheme`() =
        runTest {
            library.albums().flatMap { library.tracks(it.id) }.forEach { track ->
                assertTrue(track.uri.orEmpty().startsWith("${SourceIdentity.SCHEME}:"))
            }
        }

    @Test
    fun `a row of this source has audio, and another source's row has none here`() {
        assertNotNull(TemplateLibrary.audioOf(TemplateLibrary.SONG))
        assertNull(TemplateLibrary.audioOf(TemplateLibrary.SONG.copy(uri = "example:track:1")))
    }
}
