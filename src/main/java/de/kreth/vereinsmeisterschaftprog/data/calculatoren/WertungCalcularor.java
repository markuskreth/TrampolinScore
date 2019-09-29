package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;


interface WertungCalcularor {
   /**
    * Liefert zu den Werten in wertung das entprechende Ergebnis
    * @param wertung Daten, aus denen das Ergebnis errechnet werden soll.
    * @return  Ergebnis
    */
   double calculate(Wertung wertung);
}
