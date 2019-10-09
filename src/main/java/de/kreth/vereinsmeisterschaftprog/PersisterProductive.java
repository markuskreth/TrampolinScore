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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import de.kreth.dbmanager.DatabaseType;
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

	private final DataSource dataSource;

	public PersisterProductive(DataSource dataSource, DatabaseType dbType) {
		this.dataSource = dataSource;

		DatabaseTableCreator creator = new DatabaseTableCreator(this.dataSource, dbType);
		creator.checkVersion();
	}

	@Override
	public void fillWithStartern(Wettkampf wk) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stm = conn
						.prepareStatement("SELECT * from ergebnis where wettkampf=?")) {
			stm.setString(1, wk.getGruppe().getName());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String starterName = rs.getString("startername");
				int random = rs.getInt("random");
				List<Wertung> wertungen = getWertung(id, conn);

				Ergebnis e = new Ergebnis(id, starterName, wk, random, wertungen);
				e.addPropertyChangeListener(new PersisterErgebnisChangeListener(e));
				wk.add(e);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private List<Wertung> getWertung(int ergebnis_id, Connection conn) throws SQLException {

		List<Wertung> wertungen = new ArrayList<>();

		try (PreparedStatement stm = conn.prepareStatement("SELECT * FROM wertung where ergebnis_id=?")) {
			stm.setInt(1, ergebnis_id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String durchgString = rs.getString("durchgang");
				Durchgang durchgang = Durchgang.valueOf(durchgString);
				Wertung w = new Wertung(rs.getInt("id"), durchgang);
				fillValues(w, conn);
				w.addPropertyChangeListener(new PeristerWertungChangeListener(w));
				wertungen.add(w);
			}
		}

		return wertungen;
	}

	private void fillValues(Wertung w, Connection conn) throws SQLException {
		try (Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM VALUE WHERE wertung=" + w.getId())) {

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

	}

	private Value createValueFrom(ResultSet rs) throws SQLException {
		ValueType type = ValueType.valueOf(rs.getString("type"));
		int precision = rs.getInt("precision_value");
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

		try (Connection connection = dataSource.getConnection();
				PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

		try (Connection con = dataSource.getConnection(); Statement stm = con.createStatement()) {

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
		try (Connection con = dataSource.getConnection(); Statement stm = con.createStatement()) {
			stm.executeUpdate("INSERT INTO GRUPPE (NAME, BESCHREIBUNG) VALUES('" + name + "','" + Beschreibung + "')",
					Statement.RETURN_GENERATED_KEYS);

			ResultSet generatedKeys = stm.getGeneratedKeys();
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
		try (Connection con = dataSource.getConnection(); Statement stm = con.createStatement()) {
			ResultSet rs = stm.executeQuery(sql);
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

		private String stmPunkteUpdate;

		private String stmPlatzUpdate;

		private String stmNameUpdate;

		public PersisterErgebnisChangeListener(Ergebnis ergebnis) {
			super();
			this.ergebnis = ergebnis;
			stmPunkteUpdate = "UPDATE ergebnis SET ERGEBNIS=? WHERE ID=" + ergebnis.getId();
			stmPlatzUpdate = "UPDATE ergebnis SET PLATZ=? WHERE ID=" + ergebnis.getId();
			stmNameUpdate = "UPDATE ergebnis SET startername=? WHERE ID=" + ergebnis.getId();

		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (evt.getPropertyName().matches(Ergebnis.ERGEBNIS_CHANGE_PROPERTY)) {
				try (Connection con = dataSource.getConnection();
						PreparedStatement stm = con.prepareStatement(stmPunkteUpdate)) {
					stm.setBigDecimal(1, ergebnis.getErgebnis());
					int count = stm.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von Ergebnis ID=" + ergebnis.getId() + " wurden "
								+ count + " Zeilen geändert!");
					}

				}
				catch (SQLException e) {
					throw new IllegalStateException("Error executing " + stmPunkteUpdate, e);
				}

			}
			else if (evt.getPropertyName().matches(Ergebnis.PLATZ_CHANGE_PROPERTY)) {
				try (Connection con = dataSource.getConnection();
						PreparedStatement stm = con.prepareStatement(stmPlatzUpdate)) {
					stm.setInt(1, ergebnis.getPlatz());
					int count = stm.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von Platz ID=" + ergebnis.getId() + " wurden "
								+ count + " Zeilen geändert!");
					}

				}
				catch (SQLException e) {
					e.printStackTrace();
				}

			}
			else if (evt.getPropertyName().matches(Ergebnis.STARTERNAME_CHANGE_PROPERTY)) {
				try (Connection con = dataSource.getConnection();
						PreparedStatement stm = con.prepareStatement(stmNameUpdate)) {

					stm.setString(1, ergebnis.getStarterName());
					int count = stm.executeUpdate();

					if (count != 1) {
						throw new IllegalStateException("Beim Update von StarterName ID=" + ergebnis.getId()
								+ " wurden " + count + " Zeilen geändert(" + ergebnis.getStarterName()
								+ ") !");
					}

				}
				catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private class PeristerWertungChangeListener implements PropertyChangeListener {

		private String valueUpdate;

		private String valueInsert;

		private String selectCount;

		private String updateWertung;

		private PeristerWertungChangeListener(Wertung wertung) {
			super();
			selectCount = "select count(*) FROM VALUE WHERE wertung=" + wertung.getId()
					+ " AND type=? AND ERGEBNIS_INDEX=?";
			valueUpdate = "UPDATE VALUE SET value=? WHERE wertung=" + wertung.getId()
					+ " AND ergebnis_index=? AND type=?";
			valueInsert = "INSERT INTO VALUE ( WERTUNG, ERGEBNIS_INDEX, precision_value, TYPE, VALUE ) VALUES ("
					+ wertung.getId() +
					",?,?,?,?);";
			updateWertung = "UPDATE WERTUNG SET ERGEBNIS=? WHERE ID = ?";
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() instanceof Value) {
				Value changed = (Value) evt.getSource();
				try (Connection con = dataSource.getConnection()) {

					boolean exists;

					try (PreparedStatement stmSelectCount = con.prepareStatement(selectCount)) {

						stmSelectCount.setString(1, changed.getType().name());
						stmSelectCount.setInt(2, changed.getIndex());
						ResultSet rs = stmSelectCount.executeQuery();

						exists = rs.next() && rs.getInt(1) > 0;
					}

					int count = -1;
					if (exists) {
						try (PreparedStatement prepared = con.prepareStatement(valueUpdate)) {
							prepared.setBigDecimal(1, changed.getValue());
							prepared.setInt(2, changed.getIndex());
							prepared.setString(3, changed.getType().name());
							count = prepared.executeUpdate();
						}
					}
					else {
						try (PreparedStatement prepared = con.prepareStatement(valueInsert)) {
							prepared.setInt(1, changed.getIndex());
							prepared.setInt(2, changed.getPrecision());
							prepared.setString(3, changed.getType().name());
							prepared.setBigDecimal(4, changed.getValue());
							count = prepared.executeUpdate();
						}
					}

					if (count != 1) {
						throw new IllegalStateException("Beim Update von " + changed.identifier()
								+ " wurden " + count + " Zeilen geändert!");
					}

				}
				catch (SQLException | SecurityException | IllegalArgumentException e) {
					throw new IllegalStateException(e);
				}
			}
			else if (evt.getSource() instanceof Wertung
					&& Wertung.ERGEBNIS_CHANGE_PROPERTY.equals(evt.getPropertyName())) {
				Wertung w = (Wertung) evt.getSource();
				try (Connection connection = dataSource.getConnection();
						PreparedStatement stm = connection.prepareStatement(updateWertung,
								Statement.RETURN_GENERATED_KEYS)) {
					stm.setBigDecimal(1, w.getErgebnis());
					stm.setInt(2, w.getId());
					int count = stm.executeUpdate();

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

	@Override
	public List<Ergebnis> allErgebnisse() throws SQLException {

		List<Gruppe> pflichten = loadPflichten();
		Map<String, Wettkampf> wettkampMap = new HashMap<>();
		for (Gruppe g : pflichten) {
			Wettkampf wk = new Wettkampf(g.getName(), g);
			wettkampMap.put(g.getName(), wk);
		}
		List<Ergebnis> ergebnisse = new ArrayList<>();

		try (Connection conn = dataSource.getConnection();
				PreparedStatement stm = conn
						.prepareStatement("SELECT * from ergebnis")) {

			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String starterName = rs.getString("startername");
				int random = rs.getInt("random");
				List<Wertung> wertungen = getWertung(id, conn);
				String wettkampfName = rs.getString("wettkampf");

				Ergebnis e = new Ergebnis(id, starterName, wettkampMap.get(wettkampfName), random, wertungen);
				e.addPropertyChangeListener(new PersisterErgebnisChangeListener(e));
				ergebnisse.add(e);
			}

		}
		return ergebnisse;
	}

}
