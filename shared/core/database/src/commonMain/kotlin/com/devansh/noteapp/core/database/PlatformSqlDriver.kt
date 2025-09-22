package com.devansh.noteapp.core.database

import app.cash.sqldelight.db.SqlDriver

expect fun getPlatformSqlDriver(context: Any? = null): SqlDriver