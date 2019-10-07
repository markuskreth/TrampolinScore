package de.kreth.vereinsmeisterschaftprog.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import de.kreth.dbmanager.DatabaseType;

public class DatabaseTableCreator {

	private final DatabaseType type;

	private final DataSource dataSource;

	public DatabaseTableCreator(DataSource dataSource, DatabaseType type) {
		this.dataSource = dataSource;
		this.type = type;
	}

	// changed version
	public void checkVersion() {

		try (Connection conn = dataSource.getConnection(); Statement stm = conn.createStatement()) {

			ResultSet rs = conn.getMetaData().getTables(null, null, "version", new String[] { "TABLE" });
			boolean first = rs.next();

			if (!first) {
				executeFromVersion(0);
			}
			else {
				rs = stm.executeQuery("SELECT value FROM version");
				if (rs.next()) {

					try {
						int version = rs.getInt(1);
						executeFromVersion(version);
					}
					catch (SQLException e) {
						executeFromVersion(0);
					}
				}
				else {
					executeFromVersion(0);
				}
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void executeFromVersion(int version) throws SQLException {

		if (version > 0 && version < 4) {
			throw new IllegalStateException(
					"Datebase Version " + (version + 1) + " is not compatible with this version.");
		}
		switch (version) {
		case 0:
		case 5:
			String[] allSql = version4();
			execute(allSql);

			break;
		default:
			break;
		}
	}

	private void execute(String[] allSql) throws SQLException {

		for (String sql : allSql) {
			try (Connection conn = dataSource.getConnection(); Statement stm = conn.createStatement()) {
				stm.execute(sql);
			}
			catch (SQLException e) {
				throw new SQLException("Error in SQL: " + sql, e);
			}
		}
	}

	private String[] version4() {
		return new String[] {
				"CREATE TABLE VERSION (value INTEGER);",
				"INSERT INTO VERSION VALUES(4);",
				"CREATE TABLE ERGEBNIS (id INTEGER " + type.autoIncrementIdType
						+ ", startername VARCHAR(255) NOT NULL, wettkampf VARCHAR(25), ergebnis DOUBLE, platz INTEGER, random INTEGER);",
				"CREATE TABLE WERTUNG (id INTEGER " + type.autoIncrementIdType
						+ ", durchgang varchar(255) NOT NULL, ergebnis_id INTEGER NOT NULL, ergebnis DOUBLE, FOREIGN KEY (ergebnis_id) REFERENCES ERGEBNIS(id));",
				"CREATE TABLE VALUE (wertung INTEGER, ergebnis_index INTEGER, precision_value INTEGER, type varchar(255) NOT NULL, value DOUBLE"
						+ ", FOREIGN KEY (wertung) REFERENCES WERTUNG(id), PRIMARY KEY (wertung,ergebnis_index));",
				"CREATE TABLE GRUPPE (id INTEGER " + type.autoIncrementIdType
						+ ", name varchar(255) NOT NULL, beschreibung varchar(255) NULL)"
		};
	}
}
