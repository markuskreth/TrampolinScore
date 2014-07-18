package de.kreth.vereinsmeisterschaftprog;

import java.awt.EventQueue;

import de.kreth.vereinsmeisterschaftprog.gui.MainFrame;



public class Application {

   public void run() {
      @SuppressWarnings("unused")
      FactoryProductive factoryProductive = new FactoryProductive();

      EventQueue.invokeLater(new Runnable() {

         @Override
         public void run() {
            try {
               MainFrame frame = new MainFrame();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }
   
   public static void main(String[] args) {
      Application app = new Application();
      app.run();
   }

}
