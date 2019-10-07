package de.kreth.vereinsmeisterschaftprog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import de.kreth.dbmanager.DatabaseType;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.WertungFactory;
import de.kreth.vereinsmeisterschaftprog.db.Persister;

public class FactoryProductive extends Factory {

	private static final String PROPERTY_DATABASE_CLASS = "database.class";

	private static final String PROPERTY_DATABASE_TYPE = "DatabaseType";

	private final PersisterProductive persister;

	private final WertungFactory wertungFactory;

	public FactoryProductive() {
		instance = this;

		Properties database = new Properties();

		DataSource ds;
		try {
			database.load(getClass().getResourceAsStream("/database.properties"));
			ds = createDataSource(database);
		}
		catch (Exception e) {
			throw new IllegalStateException("Unable to create Datasource", e);
		}
		DatabaseType type = DatabaseType.valueOf(database.getProperty(PROPERTY_DATABASE_TYPE));
		persister = new PersisterProductive(ds, type);
		wertungFactory = new WertungFactory();
	}

	private DataSource createDataSource(Properties database) throws Exception {

		@SuppressWarnings("unchecked")
		Class<? extends DataSource> datasourceClass = (Class<? extends DataSource>) Class
				.forName(database.getProperty(PROPERTY_DATABASE_CLASS));

		DataSource ds = datasourceClass.getDeclaredConstructor().newInstance();
		List<String> keys = database.stringPropertyNames().stream()
				.filter(p -> !PROPERTY_DATABASE_CLASS.equals(p) && p.startsWith("database."))
				.collect(Collectors.toList());
		for (String key : keys) {
			configureProperty(datasourceClass, ds, database, key);
		}
		return ds;
	}

	private void configureProperty(Class<? extends DataSource> datasourceClass, DataSource ds, Properties database,
			String key) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method method = findSetter(datasourceClass, key.replace("database.", ""));
		String value = database.getProperty(key);
		method.invoke(ds, value);
	}

	private Method findSetter(Class<? extends DataSource> datasourceClass, String methodName)
			throws NoSuchMethodException, SecurityException {
		return datasourceClass.getMethod(methodName, String.class);
	}

	@Override
	public Persister getPersister() {
		return persister;
	}

	@Override
	public WertungFactory getWertungFactory() {
		return wertungFactory;
	}

	@Override
	public List<Durchgang> getDurchgaenge() {
		List<Durchgang> list = Arrays.asList(Durchgang.values());
		return Collections.unmodifiableList(list);
	}

}
