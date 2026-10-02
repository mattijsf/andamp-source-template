// SPDX-License-Identifier: Apache-2.0

package nl.mattix.andamp.pack.template

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import nl.mattix.andamp.pack.common.PackLauncherEntry

/**
 * The one screen this source has. The player's Preferences opens it, and so
 * does the icon in the app list.
 *
 * A real source asks for a server or an account here.
 */
class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // shows the icon in the app list, unless the listener removed it
        PackLauncherEntry(this, SourceIdentity.LAUNCHER_ALIAS).restore()
        val text = TextView(this)
        text.setText(R.string.settings_text)
        val edge = (EDGE_DP * resources.displayMetrics.density).toInt()
        text.setPadding(edge, edge, edge, edge)
        setContentView(text)
    }

    private companion object {
        const val EDGE_DP = 24
    }
}
