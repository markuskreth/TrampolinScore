package de.kreth.vereinsmeisterschaftprog.business;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.kreth.vereinsmeisterschaftprog.Factory;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Pflichten;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;
import de.kreth.vereinsmeisterschaftprog.db.Persister;
import de.kreth.vereinsmeisterschaftprog.exporter.CsvExporter;
import de.kreth.vereinsmeisterschaftprog.gui.WettkampfPanel;


public class MainFrameBusiness {

   Map<Pflichten,Wettkampf> wettkaempfe;
   private WettkampfBusiness wettkampfBusiness;
   private Pflichten pflicht;
   private Persister persister;
   
   public MainFrameBusiness() {
      wettkaempfe = new HashMap<Pflichten, Wettkampf>();

      persister = Factory.getInstance().getPersister();
      pflicht = Pflichten.values()[0];
      
      for(Pflichten p: Pflichten.values()){
         Wettkampf wettkampf = new Wettkampf(p.name(), p);
         wettkaempfe.put(p, wettkampf);
         persister.fillWithStartern(wettkampf);
      }
      
      wettkampfBusiness = new WettkampfBusiness();
      wettkampfBusiness.setWettkampf(wettkaempfe.get(Pflichten.P2));
   }
   
   public void pflichtChange(Pflichten p){
      this.pflicht = p;
      wettkampfBusiness.setWettkampf(wettkaempfe.get(p));
   }

   public WettkampfPanel getPanel() {
      return wettkampfBusiness.getPanel();
   }

   public void doExport() {
      Wettkampf wettkampf = wettkaempfe.get(this.pflicht);
      File file = new File(this.pflicht.name()+ ".csv");
      
      if(file.exists())
         file.delete();
      
      Collection<Ergebnis> ergebnisse = wettkampf.getErgebnisse();
      CsvExporter exporter;
      
      try {
         FileWriter writer = new FileWriter(file);
         exporter = new CsvExporter(writer);
         exporter.export(ergebnisse);
         writer.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
}
