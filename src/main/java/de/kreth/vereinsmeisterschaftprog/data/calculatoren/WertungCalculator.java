package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;


class WertungCalculatorFor3Values implements WertungCalcularor {

   @Override
   public double calculate(Wertung wertung) {
      BigDecimal result = BigDecimal.ZERO;

      if(wertung.getKari1()>0)
         result = result.add(BigDecimal.valueOf(wertung.getKari1()));

      if(wertung.getKari2()>0)
         result = result.add(BigDecimal.valueOf(wertung.getKari2()));

      if(wertung.getKari3()>0)
         result = result.add(BigDecimal.valueOf(wertung.getKari3()));

      if(wertung.getKari4()>0)
         result = result.add(BigDecimal.valueOf(wertung.getKari4()));

      if(wertung.getKari5()>0)
         result = result.add(BigDecimal.valueOf(wertung.getKari5()));
      
      if(wertung.getDurchgang()==Durchgang.KUER)
         result  = result.add(BigDecimal.valueOf(wertung.getSchwierigkeit()));
      
      return result.doubleValue();
   }

}
