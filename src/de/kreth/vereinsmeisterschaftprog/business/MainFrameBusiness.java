package de.kreth.vereinsmeisterschaftprog.business;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import de.kreth.vereinsmeisterschaftprog.Factory;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;
import de.kreth.vereinsmeisterschaftprog.db.Persister;
import de.kreth.vereinsmeisterschaftprog.exporter.CsvExporter;
import de.kreth.vereinsmeisterschaftprog.gui.MainView;
import de.kreth.vereinsmeisterschaftprog.gui.WettkampfPanel;


public class MainFrameBusiness {

   Map<Gruppe,Wettkampf> wettkaempfe;
   private WettkampfBusiness wettkampfBusiness;
   private Gruppe pflicht;
   private List<Gruppe> gruppen;
   private Persister persister;
   private MainView view;
   
   public MainFrameBusiness(MainView frame) {
      this.view = frame;
      wettkaempfe = new HashMap<Gruppe, Wettkampf>();

      persister = Factory.getInstance().getPersister();
      gruppen = persister.loadPflichten();
      
      if(gruppen.size()>0)
         pflicht = gruppen.get(0);
      else
         pflicht = Gruppe.INVALID;
      
      for(Gruppe p: gruppen){
         Wettkampf wettkampf = new Wettkampf(p.getName(), p);
         wettkaempfe.put(p, wettkampf);
         persister.fillWithStartern(wettkampf);
      }
      
      wettkampfBusiness = new WettkampfBusiness();
      wettkampfBusiness.setWettkampf(wettkaempfe.get(pflicht));
   }
   
   public void addPflicht() {
      view.showNewGruppeDialog();
   }
   
   public void pflichtChange(Gruppe p){
      if(p.getId()>=0) {
         this.pflicht = p;
         wettkampfBusiness.setWettkampf(wettkaempfe.get(p));
      } else
         addPflicht();
   }

   
   public List<Gruppe> getGruppen() {
      return gruppen;
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

   public void createGroup(String groupName, String groupDescription) {
      Gruppe g = persister.createPflicht(groupName, groupDescription);
      gruppen.add(g);
   }
   
}
