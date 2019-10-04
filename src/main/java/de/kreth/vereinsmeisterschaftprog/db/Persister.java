package de.kreth.vereinsmeisterschaftprog.db;

import java.util.List;

import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;


public interface Persister {
   void fillWithStartern(Wettkampf p);
   Ergebnis createErgebnis(String starterName, Wettkampf wettkampf);
   Gruppe createPflicht(String name, String beschreibung);
   List<Gruppe> loadPflichten();
}
