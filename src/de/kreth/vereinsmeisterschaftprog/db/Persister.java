package de.kreth.vereinsmeisterschaftprog.db;

import java.util.List;

import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Pflichten;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;


public interface Persister {
   void fillWithStartern(Wettkampf p);
   Ergebnis createErgebnis(String starterName, Wettkampf wettkampf);
   Pflichten createPflicht(String name);
   List<Pflichten> loadPflichten();
}
