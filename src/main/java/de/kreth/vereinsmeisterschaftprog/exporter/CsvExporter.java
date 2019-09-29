package de.kreth.vereinsmeisterschaftprog.exporter;

import java.io.*;
import java.util.Collection;

import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;


public class CsvExporter {

   private Writer writer;

   public CsvExporter(Writer writer) throws IOException {
      this.writer = writer;
      writer.append("Name,Punkte,Platz\n");
   }

   public void export(Collection<Ergebnis> ergebnisse) throws IOException {
      for(Ergebnis e: ergebnisse){
         writer.append('"').append(e.getStarterName()).append('"').append(',');
         writer.append(String.valueOf(e.getErgebnis())).append(',');
         writer.append(String.valueOf(e.getPlatz())).append('\n');
      }
   }

}
