package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;

interface WertungCalcularor {
	/**
	 * Liefert zu den Werten in wertung das entprechende Ergebnis
	 * @param wertung Daten, aus denen das Ergebnis errechnet werden soll.
	 * @return Ergebnis
	 */
	BigDecimal calculate(Wertung wertung);

}
