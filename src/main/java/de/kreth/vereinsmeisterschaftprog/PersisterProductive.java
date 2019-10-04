package de.kreth.vereinsmeisterschaftprog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.kreth.hsqldbcreator.HsqlCreator;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.Value;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;
import de.kreth.vereinsmeisterschaftprog.db.DatabaseTableCreator;
import de.kreth.vereinsmeisterschaftprog.db.Persister;

class PersisterProductive implements Persister {

	private final Random random = new Random(System.currentTimeMillis());

	private HsqlCreator hsql;

	public PersisterProductive() {
		hsql = HsqlCreator.getInstance();
		DatabaseTableCreator creator = new DatabaseTableCreator();
		creator.checkVersion();
	}

	@Override
	public void fillWithStartern(Wettkampf wk) {
		try {
			ResultSet rs = hsql.executeQuery("SELECT * from ergebnis where wettkampf='" + wk.getGruppe() + "'");
			while (rs.next()) {
				int id = rs.getInt("id");
				String starterName = rs.getString("startername");
				int pflichtId = rs.getInt("pflicht");
				int kuerId = rs.getInt("kuer");
				int random = rs.getInt("random");
				Wertung pflicht = getWertung(pflichtId);
				Wertung kuer = getWertung(kuerId);

				Ergebnis e = new Ergebnis(id, starterName, wk, kuer, pflicht, random);
				e.addPropertyChangeListener(new PersisterErgebnisChangeListener(e));
				wk.add(e);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Wertung getWertung(int id) throws SQLException {

		ResultSet rs = hsql.executeQuery("SELECT * FROM wertung where id=" + id);
		rs.next();
		String durchgString = rs.getString("durchgang");
		Durchgang durchgang = Durchgang.valueOf(durchgString);
		Wertung w = new Wertung(rs.getInt("id"), durchgang);
		fillValues(w);
		w.addPropertyChangeListener(new PeristerWertungChangeListener(w));

		return w;
	}

	@SuppressWarnings("unchecked")
	private void fillValues(Wertung w) throws SQLException {
		ResultSet rs = hsql.executeQuery("SELECT * FROM VALUE WHERE wertung=" + w.getId());
		List<Value> werte;
		try {
			Field valueField = Wertung.class.getDeclaredField("werte");
			valueField.setAccessible(true);
			werte = (List<Value>) valueField.get(w);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new SQLException("Unable to fill Values of " + w, e);
		}

		while (rs.next()) {
			werte.add(createValueFrom(rs));
		}
	}

	private Value createValueFrom(ResultSet rs) throws SQLException {
		ValueType type = ValueType.valueOf(rs.getString("type"));
		int precision = rs.getInt("precision");
		int index = rs.getInt("ergebnis_index");
		BigDecimal value = rs.getBigDecimal("value");
		Value v = new Value(type, precision, index);
		v.setValue(value);
		return v;

	}

	@Override
	public Ergebnis createErgebnis(String starterName, Wettkampf wettkampf) {

		Ergebnis result = null;
		try {

			Wertung pflicht = null;
			Wertung kuer = null;

			hsql.executeUpdate("INSERT INTO wertung (durchgang) VALUES('" + Durchgang.PFLICHT + "')",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet generatedKeys = hsql.getGeneratedKeys();
			if (generatedKeys.next()) {
				int id = generatedKeys.getInt(1);
				pflicht = new Wertung(id, Durchgang.PFLICHT);
				new PeristerWertungChangeListener(pflicht);
			}

			hsql.executeUpdate("INSERT INTO wertung (durchgang) VALUES('" + Durchgang.KUER + "')",
					Statement.RETURN_GENERATED_KEYS);
			generatedKeys = hsql.getGeneratedKeys();

			if (generatedKeys.next()) {
				int id = generatedKeys.getInt(1);
				kuer = new Wertung(id, Durchgang.KUER);
				new PeristerWertungChangeListener(kuer);
			}

			if (pflicht == null || kuer == null)
				throw new IllegalStateException(
						"Db-Fehler! Konnte Pflicht oder Kür nicht anlegen! Pflicht=" + pflicht + " Kür=" + kuer);

			int rand = random.nextInt();
			hsql.executeUpdate("INSERT INTO ergebnis (startername, wettkampf, pflicht, kuer, random) " + "VALUES('"
					+ starterName + "', '" + wettkampf.getGruppe() + "', " + pflicht.getId()
					+ ", " + kuer.getId() + ", " + rand + ")", Statement.RETURN_GENERATED_KEYS);
			generatedKeys = hsql.getGeneratedKeys();

			if (generatedKeys.next()) {
				int id = generatedKeys.getInt(1);
				result = new Ergebnis(id, starterName, wettkampf, kuer, pflicht, rand);
				result.addPropertyChangeListener(new PersisterErgebnisChangeListener(result));
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Gruppe createPflicht(String name, String Beschreibung) {
		Gruppe result = Gruppe.INVALID;
		try {
			hsql.executeUpdate("INSERT INTO GRUPPE (NAME, BESCHREIBUNG) VALUES('" + name + "','" + Beschreibung + "')",
					Statement.RETURN_GENERATED_KEYS);

			ResultSet generatedKeys = hsql.getGeneratedKeys();
			if (generatedKeys.next()) {
				int id = generatedKeys.getInt(1);
				result = new Gruppe(id, name, Beschreibung);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Gruppe> loadPflichten() {
		List<Gruppe> result = new ArrayList<>();
		String sql = "SELECT * FROM GRUPPE";
		try {
			ResultSet rs = hsql.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				String beschreibung = rs.getString("BESCHREIBUNG");
				result.add(new Gruppe(id, name, beschreibung));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	private class PersisterErgebnisChangeListener implements PropertyChangeListener {

		private Ergebnis ergebnis;

		private PreparedStatement stmPunkteUpdate;

		private PreparedStatement stmPlatzUpdate;

		private Connection connection;

		private PreparedStatement stmNameUpdate;

		public PersisterErgebnisChangeListener(Ergebnis ergebnis) {
			super();
			this.ergebnis = ergebnis;
			try {
				this.connection = hsql.getConnection();
				stmPunkteUpdate = connection
						.prepareStatement("UPDATE ergebnis SET ERGEBNIS=? WHERE ID=" + ergebnis.getId());
				stmPlatzUpdate = connection
						.prepareStatement("UPDATE ergebnis SET PLATZ=? WHERE ID=" + ergebnis.getId());
				stmNameUpdate = connection
						.prepareStatement("UPDATE ergebnis SET startername=? WHERE ID=" + ergebnis.getId());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (evt.getPropertyName().matches(Ergebnis.ERGEBNIS_CHANGE_PROPERTY)) {
				try {
					stmPunkteUpdate.setBigDecimal(1, ergebnis.getErgebnis());
					int count = stmPunkteUpdate.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von Ergebnis ID=" + ergebnis.getId() + " wurden "
								+ count + " Zeilen geändert!");
					}

					connection.commit();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}

			}
			else if (evt.getPropertyName().matches(Ergebnis.PLATZ_CHANGE_PROPERTY)) {
				try {
					stmPlatzUpdate.setInt(1, ergebnis.getPlatz());
					int count = stmPlatzUpdate.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von Platz ID=" + ergebnis.getId() + " wurden "
								+ count + " Zeilen geändert!");
					}

					connection.commit();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}

			}
			else if (evt.getPropertyName().matches(Ergebnis.STARTERNAME_CHANGE_PROPERTY)) {
				try {

					stmNameUpdate.setString(1, ergebnis.getStarterName());
					int count = stmNameUpdate.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von StarterName ID=" + ergebnis.getId()
								+ " wurden " + count + " Zeilen geändert(" + ergebnis.getStarterName()
								+ ") !");
					}

					connection.commit();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private class PeristerWertungChangeListener implements PropertyChangeListener {

		private Connection connection;

		private PreparedStatement valueUpdate;

		private PeristerWertungChangeListener(Wertung wertung) {
			super();
			try {
				this.connection = hsql.getConnection();
				valueUpdate = connection.prepareStatement("UPDATE VALUE SET value=? WHERE wertung=" + wertung.getId()
						+ " AND ergebnis_index=? AND type=?");
				wertung.allValues().forEach(v -> v.addPropertyChangeListener(PeristerWertungChangeListener.this));
			}
			catch (SQLException e) {
				throw new IllegalStateException("Unable to create Database Statements", e);
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Value changed = (Value) evt.getNewValue();
			try {
				Field indexField = Value.class.getDeclaredField("index");
				indexField.setAccessible(true);
				int ergebnis_index = indexField.getInt(changed);

				valueUpdate.setBigDecimal(1, changed.getValue());
				valueUpdate.setInt(2, ergebnis_index);
				valueUpdate.setString(3, changed.getType().name());
				int count = valueUpdate.executeUpdate();

				if (count != 1) {
					throw new IllegalStateException("Beim Update von Kari1 wurden " + count + " Zeilen geändert!");
				}
				connection.commit();

			}
			catch (SQLException | NoSuchFieldException | SecurityException | IllegalArgumentException
					| IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

}
