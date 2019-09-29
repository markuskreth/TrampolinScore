package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;


public class WertungCalculatorFor5Values implements WertungCalcularor {

   @Override
   public double calculate(Wertung wertung) {

      BigDecimal result = BigDecimal.valueOf(wertung.getKari1());
      BigDecimal min = BigDecimal.valueOf(wertung.getKari1());
      BigDecimal max = BigDecimal.valueOf(wertung.getKari1());
      
      result = result
            .add(BigDecimal.valueOf(wertung.getKari2()))
            .add(BigDecimal.valueOf(wertung.getKari3()))
            .add(BigDecimal.valueOf(wertung.getKari4()))
            .add(BigDecimal.valueOf(wertung.getKari5()));

      if(wertung.getKari2()>max.doubleValue())
         max = BigDecimal.valueOf(wertung.getKari2());
      if(wertung.getKari3()>max.doubleValue())
         max = BigDecimal.valueOf(wertung.getKari3());
      if(wertung.getKari4()>max.doubleValue())
         max = BigDecimal.valueOf(wertung.getKari4());
      if(wertung.getKari5()>max.doubleValue())
         max = BigDecimal.valueOf(wertung.getKari5());

      if(wertung.getKari2()<min.doubleValue())
         min = BigDecimal.valueOf(wertung.getKari2());
      if(wertung.getKari3()<min.doubleValue())
         min = BigDecimal.valueOf(wertung.getKari3());
      if(wertung.getKari4()<min.doubleValue())
         min = BigDecimal.valueOf(wertung.getKari4());
      if(wertung.getKari5()<min.doubleValue())
         min = BigDecimal.valueOf(wertung.getKari5());
      
      result = result.subtract(min).subtract(max);

      if(wertung.getDurchgang()==Durchgang.KUER)
         result  =result.add(BigDecimal.valueOf(wertung.getSchwierigkeit()));
      
      return result.doubleValue();
   }

}
