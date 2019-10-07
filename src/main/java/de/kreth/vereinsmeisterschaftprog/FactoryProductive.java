package de.kreth.vereinsmeisterschaftprog;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hsqldb.jdbc.JDBCDataSource;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.WertungFactory;
import de.kreth.vereinsmeisterschaftprog.db.Persister;

public class FactoryProductive extends Factory {

	private final PersisterProductive persister;

	private final WertungFactory wertungFactory;

	public FactoryProductive() {
		instance = this;

		JDBCDataSource ds = new JDBCDataSource();
		ds.setUrl("jdbc:hsqldb:file:db/DatabaseFile;shutdown=true");
		ds.setUser("SA");
		persister = new PersisterProductive(ds);
		wertungFactory = new WertungFactory();
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
