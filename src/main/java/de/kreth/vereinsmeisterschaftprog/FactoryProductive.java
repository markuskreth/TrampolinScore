package de.kreth.vereinsmeisterschaftprog;

import de.kreth.vereinsmeisterschaftprog.db.Persister;


public class FactoryProductive extends Factory {

   private PersisterProductive persister;

   public FactoryProductive() {
      instance = this;
      persister = new PersisterProductive();
   }
   
   @Override
   public Persister getPersister() {
      return persister;
   }

}
