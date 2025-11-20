/**
 * provides the Android SQLDelight database driver.
 * this file's 'actual' implementation corresponds to the expected version
 * in commonMain for cross-platform database usage.
 */

package org.example.pagepalapp.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class DatabaseDriverFactory(private val context: Context) {

    // creates the SQLDelight driver that wraps SQLite
    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            PagePalDatabase.Schema,
            context,
            "pagepal.db"
        )
    }
}
