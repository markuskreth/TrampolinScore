package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;


public class WertungCalculatorFor5Values implements WertungCalcularor {

   @Override
   public double calculate(Wertung wertung) {

      double result = 0;
      double min = wertung.getKari1();
      double max = wertung.getKari1();
      
      result +=wertung.getKari1();
      result +=wertung.getKari3();
      result +=wertung.getKari2();
      result +=wertung.getKari4();
      result +=wertung.getKari5();

      if(wertung.getKari2()>max)
         max = wertung.getKari2();
      if(wertung.getKari3()>max)
         max = wertung.getKari3();
      if(wertung.getKari4()>max)
         max = wertung.getKari4();
      if(wertung.getKari5()>max)
         max = wertung.getKari5();

      if(wertung.getKari2()<min)
         min = wertung.getKari2();
      if(wertung.getKari3()<min)
         min = wertung.getKari3();
      if(wertung.getKari4()<min)
         min = wertung.getKari4();
      if(wertung.getKari5()<min)
         min = wertung.getKari5();
      
      result = BigDecimal.valueOf(result).subtract(BigDecimal.valueOf(min)).subtract(BigDecimal.valueOf(max)).doubleValue();

      if(wertung.getDurchgang()==Durchgang.KUER)
         result  += wertung.getSchwierigkeit();
      
      return result;
   }

}
