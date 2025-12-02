package org.example.pagepalapp.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

/**
 * this is responsible for creating the SQLDelight driver on Android.
 */
class DatabaseDriverFactory(private val context: Context) {

    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            PagePalDatabase.Schema,
            context,
            "pagepal.db"
        )
    }
}

class DatabaseWrapper(driverFactory: DatabaseDriverFactory) {
    val db: PagePalDatabase = PagePalDatabase(driverFactory.createDriver())
}
