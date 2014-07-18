package de.kreth.vereinsmeisterschaftprog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;

import de.kreth.hsqldbcreator.HsqlCreator;
import de.kreth.vereinsmeisterschaftprog.data.*;
import de.kreth.vereinsmeisterschaftprog.db.DatabaseTableCreator;
import de.kreth.vereinsmeisterschaftprog.db.Persister;

class PersisterProductive implements Persister {

   private HsqlCreator hsql;

   public PersisterProductive() {
      hsql = HsqlCreator.getInstance();
      DatabaseTableCreator creator = new DatabaseTableCreator();
      creator.checkVersion();
   }

   @Override
   public void fillWithStartern(Wettkampf wk) {
      try {
         ResultSet rs = hsql.executeQuery("SELECT * from ergebnis where wettkampf='" + wk.getFilter() + "'");
         while (rs.next()) {
            int id = rs.getInt("id");
            String starterName = rs.getString("startername");
            int pflichtId = rs.getInt("pflicht");
            int kuerId = rs.getInt("kuer");
            
            Wertung pflicht = getWertung(pflichtId);
            Wertung kuer = getWertung(kuerId);

            Ergebnis e = new Ergebnis(id, starterName, wk, kuer, pflicht);
            e.addPropertyChangeListener(new PersisterErgebnisChangeListener(e));
            wk.add(e);
         }
         
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   private Wertung getWertung(int id) throws SQLException {

      ResultSet rs = hsql.executeQuery("SELECT * FROM wertung where id=" + id);
      rs.next();
      String durchgString = rs.getString("durchgang");
      Durchgang durchgang = Durchgang.valueOf(durchgString);
      Wertung w = new Wertung(rs.getInt("id"), durchgang);

      double kari1 = rs.getDouble("kari1");
      if (!rs.wasNull())
         w.setKari1(kari1);

      double kari2 = rs.getDouble("kari2");
      if (!rs.wasNull())
         w.setKari2(kari2);

      double kari3 = rs.getDouble("kari3");
      if (!rs.wasNull())
         w.setKari3(kari3);

      double kari4 = rs.getDouble("kari4");
      if (!rs.wasNull())
         w.setKari4(kari4);

      double kari5 = rs.getDouble("kari5");
      if (!rs.wasNull())
         w.setKari5(kari5);

      double schw = rs.getDouble("schwierigkeit");
      
      if (!rs.wasNull())
         w.setSchwierigkeit(schw);
      
      w.addPropertyChangeListener(new PeristerWertungChangeListener(w));
      
      return w;
   }

   @Override
   public Ergebnis createErgebnis(String starterName, Wettkampf wettkampf) {

      Ergebnis result = null;
      try {

         Wertung pflicht = null;
         Wertung kuer = null;

         hsql.executeUpdate("INSERT INTO wertung (durchgang) VALUES('" + Durchgang.PFLICHT + "')", Statement.RETURN_GENERATED_KEYS);
         ResultSet generatedKeys = hsql.getGeneratedKeys();
         if (generatedKeys.next()) {
            int id = generatedKeys.getInt(1);
            pflicht = new Wertung(id, Durchgang.PFLICHT);
            pflicht.addPropertyChangeListener(new PeristerWertungChangeListener(pflicht));
         }

         hsql.executeUpdate("INSERT INTO wertung (durchgang) VALUES('" + Durchgang.KUER + "')", Statement.RETURN_GENERATED_KEYS);
         generatedKeys = hsql.getGeneratedKeys();
         
         if (generatedKeys.next()) {
            int id = generatedKeys.getInt(1);
            kuer = new Wertung(id, Durchgang.KUER);
            kuer.addPropertyChangeListener(new PeristerWertungChangeListener(kuer));
         }

         if (pflicht == null || kuer == null)
            throw new IllegalStateException("Db-Fehler! Konnte Pflicht oder Kür nicht anlegen! Pflicht=" + pflicht + " Kür=" + kuer);

         hsql.executeUpdate("INSERT INTO ergebnis (startername, wettkampf, pflicht, kuer) " + "VALUES('" + starterName + "', '" + wettkampf.getFilter() + "', " + pflicht.getId()
               + ", " + kuer.getId() + ")", Statement.RETURN_GENERATED_KEYS);
         generatedKeys = hsql.getGeneratedKeys();

         if (generatedKeys.next()) {
            int id = generatedKeys.getInt(1);
            result = new Ergebnis(id, starterName, wettkampf, kuer, pflicht);
            result.addPropertyChangeListener(new PersisterErgebnisChangeListener(result));
         }

      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return result;
   }

   private class PersisterErgebnisChangeListener implements PropertyChangeListener {
      
      private Ergebnis ergebnis;
      private PreparedStatement stmPunkteUpdate;
      private PreparedStatement stmPlatzUpdate;
      private Connection connection;
      private PreparedStatement stmNameUpdate;
      
      public PersisterErgebnisChangeListener(Ergebnis ergebnis) {
         super();
         this.ergebnis = ergebnis;
         try {
            this.connection = hsql.getConnection();
            stmPunkteUpdate = connection.prepareStatement("UPDATE ergebnis SET ERGEBNIS=? WHERE ID=" + ergebnis.getId());
            stmPlatzUpdate = connection.prepareStatement("UPDATE ergebnis SET PLATZ=? WHERE ID=" + ergebnis.getId());
            stmNameUpdate = connection.prepareStatement("UPDATE ergebnis SET startername=? WHERE ID=" + ergebnis.getId());
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }


      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         
         if(evt.getPropertyName().matches(Ergebnis.ERGEBNIS_CHANGE_PROPERTY)){
            try {
               stmPunkteUpdate.setDouble(1, ergebnis.getErgebnis());
               int count = stmPunkteUpdate.executeUpdate();
               
               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Ergebnis ID=" + ergebnis.getId() + " wurden " + count + " Zeilen geändert!");
               }
               
               connection.commit();
            } catch (SQLException e) {
               e.printStackTrace();
            }
            
         } else if(evt.getPropertyName().matches(Ergebnis.PLATZ_CHANGE_PROPERTY)){
            try {
               stmPlatzUpdate.setInt(1, ergebnis.getPlatz());
               int count = stmPlatzUpdate.executeUpdate();
               
               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Platz ID=" + ergebnis.getId() + " wurden " + count + " Zeilen geändert!");
               }
               
               connection.commit();
            } catch (SQLException e) {
               e.printStackTrace();
            }
            
         } else if(evt.getPropertyName().matches(Ergebnis.STARTERNAME_CHANGE_PROPERTY)){
            try {
               
               stmNameUpdate.setString(1, ergebnis.getStarterName());
               int count = stmNameUpdate.executeUpdate();
               
               if (count != 1) {
                  throw new IllegalStateException("Beim Update von StarterName ID=" + ergebnis.getId() + " wurden " + count + " Zeilen geändert(" + ergebnis.getStarterName() 
                        + ") !");
               }
               
               connection.commit();
            } catch (SQLException e) {
               e.printStackTrace();
            }
            
         }
      }
      
   }
   private class PeristerWertungChangeListener implements PropertyChangeListener {

      private Wertung wertung;
      private PreparedStatement stmKari1Update;
      private PreparedStatement stmKari2Update;
      private PreparedStatement stmKari3Update;
      private PreparedStatement stmKari4Update;
      private PreparedStatement stmKari5Update;

      private PreparedStatement stmDiffUpdate;
      private PreparedStatement stmResultUpdate;
      private Connection connection;

      public PeristerWertungChangeListener(Wertung wertung) {
         super();
         this.wertung = wertung;
         try {
            this.connection = hsql.getConnection();
            stmKari1Update = connection.prepareStatement("UPDATE wertung SET KARI1=? WHERE ID=" + wertung.getId());
            stmKari2Update = connection.prepareStatement("UPDATE wertung SET KARI2=? WHERE ID=" + wertung.getId());
            stmKari3Update = connection.prepareStatement("UPDATE wertung SET KARI3=? WHERE ID=" + wertung.getId());
            stmKari4Update = connection.prepareStatement("UPDATE wertung SET KARI4=? WHERE ID=" + wertung.getId());
            stmKari5Update = connection.prepareStatement("UPDATE wertung SET KARI5=? WHERE ID=" + wertung.getId());

            stmDiffUpdate = connection.prepareStatement("UPDATE wertung SET SCHWIERIGKEIT=? WHERE ID=" + wertung.getId());
            stmResultUpdate = connection.prepareStatement("UPDATE wertung SET ERGEBNIS=? WHERE ID=" + wertung.getId());
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         try {
            int count;
            if (evt.getPropertyName().matches(Wertung.KARI1_CHANGE_PROPERTY)) {
               stmKari1Update.setDouble(1, wertung.getKari1());
               count = stmKari1Update.executeUpdate();
               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Kari1 wurden " + count + " Zeilen geändert!");
               }
               connection.commit();
            }

            if (evt.getPropertyName().matches(Wertung.KARI2_CHANGE_PROPERTY)) {
               stmKari2Update.setDouble(1, wertung.getKari2());

               count = stmKari2Update.executeUpdate();
               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Kari2 wurden " + count + " Zeilen geändert!");
               }
               connection.commit();
            }

            if (evt.getPropertyName().matches(Wertung.KARI3_CHANGE_PROPERTY)) {

               stmKari3Update.setDouble(1, wertung.getKari3());

               count = stmKari3Update.executeUpdate();

               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Kari3 wurden " + count + " Zeilen geändert!");
               }
               connection.commit();
            }

            if (evt.getPropertyName().matches(Wertung.KARI4_CHANGE_PROPERTY)) {

               stmKari4Update.setDouble(1, wertung.getKari4());

               count = stmKari4Update.executeUpdate();

               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Kari4 wurden " + count + " Zeilen geändert!");
               }
               connection.commit();
            }

            if (evt.getPropertyName().matches(Wertung.KARI5_CHANGE_PROPERTY)) {

               stmKari5Update.setDouble(1, wertung.getKari5());

               count = stmKari5Update.executeUpdate();

               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Kari5 wurden " + count + " Zeilen geändert!");
               }
               connection.commit();
            }

            if (evt.getPropertyName().matches(Wertung.DIFF_CHANGE_PROPERTY)) {

               stmDiffUpdate.setDouble(1, wertung.getKari4());

               count = stmDiffUpdate.executeUpdate();

               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Schwierigkeit wurden " + count + " Zeilen geändert!");
               }
               connection.commit();
            }

            if (evt.getPropertyName().matches(Wertung.ERGEBNIS_CHANGE_PROPERTY)) {

               stmResultUpdate.setDouble(1, wertung.getErgebnis());

               count = stmResultUpdate.executeUpdate();

               if (count != 1) {
                  throw new IllegalStateException("Beim Update von Ergebnis wurden " + count + " Zeilen geändert!");
               }
               connection.commit();
            }
            
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }

   }
   
}
