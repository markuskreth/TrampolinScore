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

			for (String sql : version4) {
				hsql.execute(sql);
			}

			break;
		default:
			break;
		}
	}

	private String[] version4 = {
			"CREATE TABLE VERSION (value INTEGER);",
			"INSERT INTO VERSION VALUES(4);",
			"CREATE TABLE WERTUNG (id INTEGER IDENTITY, durchgang varchar(255) NOT NULL, ergebnis REAL);",
			"CREATE TABLE VALUE (wertung INTEGER, ergebnis_index INTEGER, precision INTEGER, type varchar(255) NOT NULL, value REAL);",
			"ALTER TABLE VALUE ADD PRIMARY KEY(wertung,type, ergebnis_index);",
			"CREATE TABLE GRUPPE (id INTEGER IDENTITY, name varchar(255) NOT NULL, beschreibung varchar(255) NULL)",
			"CREATE TABLE ERGEBNIS (id INTEGER IDENTITY, startername VARCHAR(255) NOT NULL, wettkampf VARCHAR(25), pflicht INTEGER, kuer INTEGER, ergebnis REAL, platz INTEGER, random INTEGER, FOREIGN KEY (pflicht) REFERENCES wertung(id), FOREIGN KEY (kuer) REFERENCES wertung(id));"
	};

}
