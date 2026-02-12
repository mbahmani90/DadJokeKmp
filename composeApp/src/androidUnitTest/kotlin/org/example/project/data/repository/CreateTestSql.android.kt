package org.example.project.data.repository

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual fun createTestSqlDriver(): app.cash.sqldelight.db.SqlDriver {
    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
}