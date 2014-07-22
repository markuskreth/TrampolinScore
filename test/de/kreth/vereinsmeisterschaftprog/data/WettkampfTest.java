package de.kreth.vereinsmeisterschaftprog.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class WettkampfTest {

   @Test
   public void test() {
      Wettkampf w = new Wettkampf("Test", new Gruppe(1, "P2", "Gruppe P2"));
      Ergebnis m = new Ergebnis(1,"Markus", w, new Wertung(1, Durchgang.PFLICHT), new Wertung(2, Durchgang.KUER));
      w.add(m);
      Ergebnis j = new Ergebnis(2,"Jasmin", w, new Wertung(3, Durchgang.PFLICHT), new Wertung(4, Durchgang.KUER));
      w.add(j);
      Ergebnis k = new Ergebnis(3,"Kira", w, new Wertung(5, Durchgang.PFLICHT), new Wertung(6, Durchgang.KUER));
      w.add(k);
//      Ergebnis l = new Ergebnis("Markus", w);
//      w.add(l);
      m.getPflicht().setKari1(7.0);
      m.getPflicht().setKari2(7.0);
      m.getPflicht().setKari3(7.0);

      j.getPflicht().setKari1(7.1);
      j.getPflicht().setKari2(7.1);
      j.getPflicht().setKari3(7.1);

      k.getPflicht().setKari1(7.2);
      k.getPflicht().setKari2(7.2);
      k.getPflicht().setKari3(7.2);

      assertEquals(1, k.getPlatz());
      assertEquals(2, j.getPlatz());
      assertEquals(3, m.getPlatz());
   }

}
