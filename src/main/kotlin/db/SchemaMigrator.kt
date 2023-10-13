package db

import db.connection.DataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

class SchemaMigrator (private val datasource: DataSource) {
    init {
        Database.connect(datasource.hikari())
    }
    fun run() {
        val flyway = Flyway.configure().dataSource(datasource.hikari()).load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            throw e
        }
    }
}