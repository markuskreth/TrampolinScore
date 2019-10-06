package de.kreth.vereinsmeisterschaftprog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
				int random = rs.getInt("random");
				List<Wertung> wertungen = getWertung(id);

				Ergebnis e = new Ergebnis(id, starterName, wk, random, wertungen);
				e.addPropertyChangeListener(new PersisterErgebnisChangeListener(e));
				wk.add(e);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private List<Wertung> getWertung(int ergebnis_id) throws SQLException {

		List<Wertung> wertungen = new ArrayList<>();

		ResultSet rs = hsql.executeQuery("SELECT * FROM wertung where ergebnis_id=" + ergebnis_id);
		while (rs.next()) {
			String durchgString = rs.getString("durchgang");
			Durchgang durchgang = Durchgang.valueOf(durchgString);
			Wertung w = new Wertung(rs.getInt("id"), durchgang);
			fillValues(w);
			w.addPropertyChangeListener(new PeristerWertungChangeListener(w));
			wertungen.add(w);
		}

		return wertungen;
	}

	private void fillValues(Wertung w) throws SQLException {
		ResultSet rs = hsql.executeQuery("SELECT * FROM VALUE WHERE wertung=" + w.getId());
		List<Value> werte = new ArrayList<>();

		while (rs.next()) {
			werte.add(createValueFrom(rs));
		}
		try {
			Method valueSetter = Wertung.class.getDeclaredMethod("setValues", List.class);
			valueSetter.setAccessible(true);
			valueSetter.invoke(w, werte);
		}
		catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
				| InvocationTargetException e) {
			throw new SQLException("Unable to fill Values of " + w, e);
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

		int rand = random.nextInt();
		String sql = "INSERT INTO ergebnis (startername, wettkampf, random) VALUES(?, ?, ?)";
		Connection connection;
		try {
			connection = hsql.getConnection();
		}
		catch (SQLException e1) {
			throw new IllegalStateException("Unable to connect to database", e1);
		}

		try (PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stm.setString(1, starterName);
			stm.setString(2, wettkampf.getGruppe().toString());
			stm.setInt(3, rand);
			stm.executeUpdate();
			ResultSet generatedKeys = stm.getGeneratedKeys();

			if (generatedKeys.next()) {

				int id = generatedKeys.getInt(1);
				List<Durchgang> durchgaenge = Factory.getInstance().getDurchgaenge();
				List<Wertung> wertungen = new ArrayList<>();
				for (Durchgang d : durchgaenge) {
					wertungen.add(create(d, id));
				}

				result = new Ergebnis(id, starterName, wettkampf, rand, wertungen);
				result.addPropertyChangeListener(new PersisterErgebnisChangeListener(result));
			}
			else {
				throw new IllegalStateException("No id was generated for Ergebnis " + starterName);
			}

		}
		catch (SQLException e) {
			throw new IllegalStateException("Unable to chreate Database Ergebnis for " + starterName, e);
		}

		return result;
	}

	private Wertung create(Durchgang durchgang, int ergebnisId) throws SQLException {
		String sql = "INSERT INTO wertung (durchgang, ergebnis_id) VALUES('" + durchgang + "', "
				+ ergebnisId + ")";
		Wertung wertung = null;
		Connection con = hsql.getConnection();
		try (Statement stm = con.createStatement()) {

			stm.executeUpdate(sql,
					Statement.RETURN_GENERATED_KEYS);

			try (ResultSet generatedKeys = stm.getGeneratedKeys()) {

				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
					wertung = new Wertung(id, durchgang);
					new PeristerWertungChangeListener(wertung);
				}
				else {
					throw new IllegalStateException("Unable to create Wertung for Durchgang=" + durchgang);
				}
			}
		}

		return wertung;
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

		private PreparedStatement valueInsert;

		private PreparedStatement selectCount;

		private PreparedStatement updateWertung;

		private PeristerWertungChangeListener(Wertung wertung) {
			super();
			try {
				this.connection = hsql.getConnection();
				selectCount = connection.prepareStatement("select count(*) FROM VALUE WHERE wertung=" + wertung.getId()
						+ " AND type=?");
				valueUpdate = connection.prepareStatement("UPDATE VALUE SET value=? WHERE wertung=" + wertung.getId()
						+ " AND ergebnis_index=? AND type=?");
				valueInsert = connection.prepareStatement(
						"INSERT INTO VALUE ( WERTUNG, ERGEBNIS_INDEX, PRECISION, TYPE, VALUE ) VALUES ("
								+ wertung.getId() +
								",?,?,?,?);");
				updateWertung = connection.prepareStatement("UPDATE WERTUNG SET ERGEBNIS=? WHERE ID = ?");
			}
			catch (SQLException e) {
				throw new IllegalStateException("Unable to create Database Statements", e);
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() instanceof Value) {
				Value changed = (Value) evt.getSource();
				try {

					PreparedStatement prepared;

					selectCount.setString(1, changed.getType().name());
					ResultSet rs = selectCount.executeQuery();

					boolean exists = rs.next() && rs.getInt(1) > 0;
					rs.close();
					if (exists) {

						prepared = valueUpdate;
						prepared.setBigDecimal(1, changed.getValue());
						prepared.setInt(2, changed.getIndex());
						prepared.setString(3, changed.getType().name());
					}
					else {
						prepared = valueInsert;
						prepared.setInt(1, changed.getIndex());
						prepared.setInt(2, changed.getPrecision());
						prepared.setString(3, changed.getType().name());
						prepared.setBigDecimal(4, changed.getValue());
					}

					int count = prepared.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von Kari wurden " + count + " Zeilen geändert!");
					}
					connection.commit();

				}
				catch (SQLException | SecurityException | IllegalArgumentException e) {
					throw new IllegalStateException(e);
				}
			}
			else if (evt.getSource() instanceof Wertung
					&& Wertung.ERGEBNIS_CHANGE_PROPERTY.equals(evt.getPropertyName())) {
				Wertung w = (Wertung) evt.getSource();
				try {
					updateWertung.setBigDecimal(1, w.getErgebnis());
					updateWertung.setInt(2, w.getId());
					int count = updateWertung.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von " + w
								+ " wurden " + count + " Zeilen geändert!");
					}
				}
				catch (SQLException e) {
					throw new IllegalStateException(e);
				}
			}
		}

	}

}
