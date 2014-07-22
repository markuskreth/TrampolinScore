package de.kreth.vereinsmeisterschaftprog.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.kreth.hsqldbcreator.HsqlCreator;


public class DatabaseTableCreator {

   HsqlCreator hsql = HsqlCreator.getInstance();
   
   public void checkVersion() {

      try {
         ResultSet rs = hsql.executeQuery("SELECT count(*) as Anzahl FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'");
         boolean first = rs.next();
         int anzahl = rs.getInt(1);
         
         if(!first || anzahl<1){
            executeFromVersion(0);
         } else {
            rs = hsql.executeQuery("SELECT value FROM version");
            rs.next();
            int version = rs.getInt(1);
            executeFromVersion(version);
         }
         
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
   }

   private void executeFromVersion(int version) throws SQLException {

      switch (version+1) {
         case 1:

            for(String sql : version1){
               hsql.execute(sql);
            }

         case 2:

            for(String sql : version2){
               hsql.execute(sql);
            }

         case 3:

            for(String sql : version3){
               hsql.execute(sql);
            }

         default:
            break;
      }
   }
   
   private String[] version1 = {
         "CREATE TABLE VERSION (value INTEGER)",
         "INSERT INTO VERSION VALUES(1)",
         "CREATE TABLE WERTUNG (id INTEGER IDENTITY, durchgang varchar(255) NOT NULL, kari1 REAL, kari2 REAL, kari3 REAL, kari4 REAL, kari5 REAL, schwierigkeit REAL, ergebnis REAL)",
         "CREATE TABLE ERGEBNIS (id INTEGER IDENTITY, startername VARCHAR(255) NOT NULL, pflicht INTEGER, kuer INTEGER, ergebnis REAL, platz INTEGER, FOREIGN KEY (pflicht) REFERENCES wertung(id), FOREIGN KEY (kuer) REFERENCES wertung(id))"
   };

   private String[] version2 = {
         "ALTER TABLE ERGEBNIS ADD wettkampf VARCHAR(25)",
         "UPDATE version set value=2"
   };
   
   private String[] version3 = {
         "CREATE TABLE GRUPPE (id INTEGER IDENTITY, name varchar(255) NOT NULL, beschreibung varchar(255) NULL)",
         "UPDATE version set value=3"
   };
}
