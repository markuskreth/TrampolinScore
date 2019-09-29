package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;


public class WertungCalculatorFactory {

   public static double calculate(Wertung wertung) {
      double result = 0;
            
      switch (getAnzahlWerte(wertung)) {
         case 3:
            result = new WertungCalculatorFor3Values().calculate(wertung);
            break;

         case 4:
            result = new WertungCalculatorFor4Values().calculate(wertung);
            break;

         case 5:
            result = new WertungCalculatorFor5Values().calculate(wertung);
            break;

         default:
            break;
      }
      return result;
   }
   
   private static int getAnzahlWerte(Wertung wertung){
      int anzahl = 0;

      if(wertung.getKari1()>0)
         anzahl++;

      if(wertung.getKari2()>0)
         anzahl++;

      if(wertung.getKari3()>0)
         anzahl++;

      if(wertung.getKari4()>0)
         anzahl++;

      if(wertung.getKari5()>0)
         anzahl++;
      
      return anzahl;
   }
}
