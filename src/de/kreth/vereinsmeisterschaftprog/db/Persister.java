package de.kreth.vereinsmeisterschaftprog.db;

import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;


public interface Persister {
   void fillWithStartern(Wettkampf p);
   Ergebnis createErgebnis(String starterName, Wettkampf wettkampf);
   
}
