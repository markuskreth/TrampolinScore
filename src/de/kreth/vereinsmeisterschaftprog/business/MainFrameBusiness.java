package de.kreth.vereinsmeisterschaftprog.business;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
   private List<Pflichten> pflichten;
   private Persister persister;
   
   public MainFrameBusiness() {
      wettkaempfe = new HashMap<Pflichten, Wettkampf>();

      persister = Factory.getInstance().getPersister();
      pflichten = persister.loadPflichten();
      
      pflicht = pflichten.get(0);
      
      for(Pflichten p: pflichten){
         Wettkampf wettkampf = new Wettkampf(p.getName(), p);
         wettkaempfe.put(p, wettkampf);
         persister.fillWithStartern(wettkampf);
      }
      
      wettkampfBusiness = new WettkampfBusiness();
      wettkampfBusiness.setWettkampf(wettkaempfe.get(pflicht));
   }
   
   public void addPflicht() {
      
   }
   
   public void pflichtChange(Pflichten p){
      this.pflicht = p;
      wettkampfBusiness.setWettkampf(wettkaempfe.get(p));
   }

   
   public List<Pflichten> getPflichten() {
      return pflichten;
   }
   
   public WettkampfPanel getPanel() {
      return wettkampfBusiness.getPanel();
   }

   public void doExport() {
      Wettkampf wettkampf = wettkaempfe.get(this.pflicht);
      File file = new File(this.pflicht.getName()+ ".csv");
      
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
