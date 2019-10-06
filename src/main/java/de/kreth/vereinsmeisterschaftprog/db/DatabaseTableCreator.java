package de.kreth.vereinsmeisterschaftprog.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.kreth.hsqldbcreator.HsqlCreator;

public class DatabaseTableCreator {

	HsqlCreator hsql = HsqlCreator.getInstance();

	// changed version
	public void checkVersion() {

		try {
			ResultSet rs = hsql.executeQuery(
					"SELECT count(*) as Anzahl FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'");
			boolean first = rs.next();
			int anzahl = rs.getInt(1);

			if (!first || anzahl < 1) {
				executeFromVersion(0);
			}
			else {
				rs = hsql.executeQuery("SELECT value FROM version");
				rs.next();
				int version = rs.getInt(1);
				executeFromVersion(version);
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
			String[] allSql = version4;
			execute(allSql);

			break;
		default:
			break;
		}
	}

	private void execute(String[] allSql) throws SQLException {

		for (String sql : allSql) {
			try {
				hsql.execute(sql);
			}
			catch (SQLException e) {
				throw new SQLException("Error in SQL: " + sql, e);
			}
		}
	}

	private String[] version4 = {
			"CREATE TABLE VERSION (value INTEGER);",
			"INSERT INTO VERSION VALUES(4);",
			"CREATE TABLE ERGEBNIS (id INTEGER IDENTITY, startername VARCHAR(255) NOT NULL, wettkampf VARCHAR(25), ergebnis REAL, platz INTEGER, random INTEGER);",
			"CREATE TABLE WERTUNG (id INTEGER IDENTITY, durchgang varchar(255) NOT NULL, ergebnis_id INTEGER NOT NULL, ergebnis REAL, FOREIGN KEY (ergebnis_id) REFERENCES ERGEBNIS(id));",
			"CREATE TABLE VALUE (wertung INTEGER, ergebnis_index INTEGER, precision INTEGER, type varchar(255) NOT NULL, value REAL"
					+ ", FOREIGN KEY (wertung) REFERENCES WERTUNG(id), PRIMARY KEY (wertung,ergebnis_index));",
			"CREATE TABLE GRUPPE (id INTEGER IDENTITY, name varchar(255) NOT NULL, beschreibung varchar(255) NULL)"
	};
}
