package de.kreth.vereinsmeisterschaftprog.data;


public class Gruppe {
   public static final Gruppe INVALID = new Gruppe(-1, "INVALID", "INVALID");
   private long id;
   private String name;
   private String beschreibung;
   
   /**
    * 
    * @param id
    * @param name
    * @param beschreibung  darf null sein
    */
   public Gruppe(long id, String name, String beschreibung) {
      super();
      this.id = id;
      this.name = name;
      this.beschreibung = beschreibung;
   }

   
   public long getId() {
      return id;
   }

   
   public String getName() {
      return name;
   }

   
   public String getBeschreibung() {
      return beschreibung;
   }

   @Override
   public String toString() {
      return name;
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((beschreibung == null) ? 0 : beschreibung.hashCode());
      result = prime * result + (int) (id ^ (id >>> 32));
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Gruppe other = (Gruppe) obj;
      if (beschreibung == null) {
         if (other.beschreibung != null)
            return false;
      } else if (!beschreibung.equals(other.beschreibung))
         return false;
      if (id != other.id)
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }
   
}
