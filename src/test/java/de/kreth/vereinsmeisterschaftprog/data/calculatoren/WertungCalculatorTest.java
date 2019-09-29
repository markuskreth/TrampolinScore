package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;


public class WertungCalculatorTest {

   private WertungCalculatorFor5Values calc5;
   private WertungCalculatorFor4Values calc4;
   private WertungCalculatorFor3Values calc3;

   @Before
   public void setUp() {
      calc5 = new WertungCalculatorFor5Values();
      calc4 = new WertungCalculatorFor4Values();
      calc3 = new WertungCalculatorFor3Values();

   }
   
   @Test
   public void testKrummeWerte() {

      Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
      pflicht.setKari1(5.8);
      pflicht.setKari2(5.7);
      pflicht.setKari3(5.6);
      pflicht.setKari4(5.7);
      pflicht.setKari5(5.5);

      double erg = calc5.calculate(pflicht);
      assertEquals(17.0, erg, 0.001);
   }
   
   @Test
   public void testCalculate5er() {

      Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
      pflicht.setKari1(7.1);
      pflicht.setKari2(7.2);
      pflicht.setKari3(7.3);
      pflicht.setKari4(7.4);
      pflicht.setKari5(7.5);

      Wertung kuer = new Wertung(1, Durchgang.KUER);
      kuer.setKari1(7.1);
      kuer.setKari2(7.2);
      kuer.setKari3(7.3);
      kuer.setKari4(7.4);
      kuer.setKari5(7.5);
      kuer.setSchwierigkeit(20.5);
      
      double erg = calc5.calculate(pflicht);
      assertEquals(21.9, erg, 0.000000001);
      
      erg = calc5.calculate(kuer);
      assertEquals(42.4, erg, 0.000000001);

      erg = WertungCalculatorFactory.calculate(kuer);
      assertEquals(42.4, erg, 0.000000001);
   }

   @Test
   public void testCalculate3er() {

      Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
      pflicht.setKari1(7.1);
      pflicht.setKari3(7.3);
      pflicht.setKari5(7.4);

      Wertung kuer = new Wertung(1, Durchgang.KUER);
      kuer.setKari1(7.1);
      kuer.setKari3(7.3);
      kuer.setKari5(7.4);
      kuer.setSchwierigkeit(20.5);

      double erg = calc3.calculate(pflicht);
      assertEquals(21.8, erg, 0.000000001);

      erg = calc3.calculate(kuer);
      assertEquals(42.3, erg, 0.000000001);

      kuer.setKari5(8.4);
      erg = calc3.calculate(kuer);
      assertEquals(43.3, erg, 0.000000001);
      
      pflicht.setKari1(7.1);
      pflicht.setKari3(0);
      pflicht.setKari4(7.7);
      pflicht.setKari5(7.4);
      erg = calc3.calculate(pflicht);
      assertEquals(22.2, erg, 0.000000001);
      
      erg = WertungCalculatorFactory.calculate(pflicht);
      assertEquals(22.2, erg, 0.000000001);

   }

   @Test
   public void testCalculate4er() {

      Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
      pflicht.setKari1(7.1);
      pflicht.setKari2(7.3);
      pflicht.setKari4(7.4);
      pflicht.setKari5(7.4);

      Wertung kuer = new Wertung(1, Durchgang.KUER);
      kuer.setKari1(7.1);
      kuer.setKari3(7.7);
      kuer.setKari4(7.3);
      kuer.setKari5(7.4);
      kuer.setSchwierigkeit(20.5);

      double erg = calc4.calculate(pflicht);
      assertEquals(21.9, erg, 0.000000001);

      erg = calc4.calculate(kuer);
      assertEquals(42.625, erg, 0.000000001);

      kuer.setKari5(8.4);
      erg = calc4.calculate(kuer);
      assertEquals(43.375, erg, 0.000000001);
      
      erg = WertungCalculatorFactory.calculate(kuer);
      assertEquals(43.375, erg, 0.000000001);
   }
   
}
