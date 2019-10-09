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

		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);

//			printAllColumns(conn.getMetaData().getTables(null, null, null, new String[] { "TABLE" }));
			ResultSet rs = conn.getMetaData().getTables(null, null, "version", new String[] { "TABLE" });
			boolean first = rs.next();

			try (Statement stm = conn.createStatement()) {

				if (!first) {
					executeFromVersion(stm, 0);
				}
				else {
					rs = stm.executeQuery("SELECT value FROM version");
					if (rs.next()) {

						try {
							int version = rs.getInt(1);
							executeFromVersion(stm, version);
						}
						catch (SQLException e) {
							executeFromVersion(stm, 0);
						}
					}
					else {
						executeFromVersion(stm, 0);
					}
				}
				conn.commit();
			}
			catch (Exception e) {
				conn.rollback();
			}
			finally {
				conn.setAutoCommit(false);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void printAllColumns(ResultSet tables) throws SQLException {
		int columns[] = { 2, 3, 4, 7, 8 };
		for (int col : columns) {
			System.out.print(tables.getMetaData().getColumnName(col) + "\t\t");
		}
		System.out.println();
		System.out.println();
		while (tables.next()) {
			for (int col : columns) {
				System.out.print(tables.getString(col) + "\t\t");
			}
			System.out.println();
		}
	}

	private void executeFromVersion(Statement stm, int version) throws SQLException {

		if (version > 0 && version < 4) {
			throw new IllegalStateException(
					"Datebase Version " + (version + 1) + " is not compatible with this version.");
		}
		switch (version) {
		case 0:
		case 5:
			String[] allSql = version4();
			execute(stm, allSql);

			break;
		default:
			break;
		}
	}

	private void execute(Statement stm, String[] allSql) throws SQLException {

		for (String sql : allSql) {
			try {
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
						+ ", FOREIGN KEY (wertung) REFERENCES WERTUNG(id), PRIMARY KEY (wertung,ergebnis_index,type));",
				"CREATE TABLE GRUPPE (id INTEGER " + type.autoIncrementIdType
						+ ", name varchar(255) NOT NULL, beschreibung varchar(255) NULL)"
		};
	}
}
