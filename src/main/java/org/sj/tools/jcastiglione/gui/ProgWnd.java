package org.sj.tools.jcastiglione.gui;


import guiutil.VentanaUtil;
import java.awt.Frame;
import java.awt.Container;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;
//import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.FileDialog;
import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.sj.tools.jcastiglione.JCastiglioneDoc;
import org.sj.tools.jcastiglione.Layer;
import org.sj.tools.jcastiglione.LayerManager;
import org.sj.tools.jcastiglione.LayerView;
import org.sj.tools.jcastiglione.io.JCgDocLoader;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;

import java.util.logging.Logger;
import java.awt.Button;
import java.awt.Scrollbar;
import java.awt.Image;
//import java.awt.Toolkit;
//import javax.imageio.ImageReader;
//import java.applet.Applet;
   import java.awt.event.ActionListener;
   import java.awt.event.AdjustmentListener;
   import java.awt.event.AdjustmentEvent;
   import java.awt.event.ActionEvent;
//import java.util.Iterator;
   import java.awt.BorderLayout;


	public class ProgWnd extends VentanaUtil {
		
		
		private static final int DEFAULT_WIDTH = 750;
      private static final int DEFAULT_HEIGHT = 500;
      private static final int BTNS_ALN_X = 640;
   
      private static final String MENU_ARC_NUEVO = "Nuevo";
      private static final String MENU_ARC_ABRIR = "Abrir...";
      private static final String MENU_ARC_GUARDAR = "Guardar...";
      private static final String MENU_ARC_GUARDARX = "Guardar XML...";
      private static final String MENU_ARC_LOADX = "Cargar XML...";
      private static final String MENU_ARC_EXPSVG = "Exportar a SVG";
      private static final String MENU_ARC_EXPVID = "Exportar a video";
      private static final String MENU_ARC_SALIR = "Salir";
   
      private static final String MENU_DOC_AJFONDO = "Ajustar al Fondo";

      private static final String MENU_VER_MALLA_NO = "Sin malla";
      private static final String MENU_VER_MALLA_CUAD = "Malla cuadrada";
      private static final String MENU_VER_MALLA_TRI = "Malla triangular";
      
      private Logger log = Logger.getLogger("ProgVent");
      JCastiglioneView view;
      JCastiglioneDoc doc;
      GeneralToolBar gtb;
      FigureToolBar ftb;
      LayerView lyVw;
   
      private BtnLoadImage btnLdImg;
   
      private Scrollbar hbar, vbar;
   
      private MenuBar barraMenu;
   
   
      public ProgWnd(String s, JCastiglioneDoc _doc, JCastiglioneView _view) {
        super(s, false);
         doc = _doc;
         view = _view;
         setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      
      /* Construir componentes */
         Container centro = new Container();
         gtb = new GeneralToolBar(doc, view, this);
         ftb = new FigureToolBar(doc, view);
         view.addSelectionListener(ftb);
      
         lyVw = new LayerView(doc.getLayerManager(), view);
      
      
         hbar = new Barra(Scrollbar.HORIZONTAL,0, 10, 0, 100,view);
         vbar = new Barra(Scrollbar.VERTICAL,0, 10, 0, 100,view);
         view.addScroll(hbar, vbar);
      
         generarMenu();
         setMenuBar(barraMenu);
      
      /* AÃ±adir componentes para que se muetren */
         BorderLayout bl = new BorderLayout();
         setLayout(bl);
      
         centro.add(view);
         centro.add(hbar);
         centro.add(vbar);
         add(BorderLayout.CENTER, centro);
         add(BorderLayout.SOUTH, gtb);
         add(BorderLayout.EAST, ftb);
         add(BorderLayout.WEST, lyVw);
         
         log.fine("ProgVent created");
      
      }
   
       private void generarMenu() {
         barraMenu = new MenuBar();
      // Menus
         Menu mnArch = new Menu("Archivo");
         Menu mnDoc = new Menu("Documento");
			Menu mnVer = new Menu("Ver");
      // Items
      // -Archivo

         Vector<MenuItem> miArch = new Vector<MenuItem>();
         String archItems[] = {MENU_ARC_NUEVO,  MENU_ARC_ABRIR,
              MENU_ARC_GUARDAR, MENU_ARC_GUARDARX, MENU_ARC_LOADX,
              MENU_ARC_EXPSVG,
              MENU_ARC_EXPVID,	MENU_ARC_SALIR};
      // -Documento
         MenuItem miDoc = new MenuItem(MENU_DOC_AJFONDO);
			
		// -Ver
         MenuItem miVer[] = new MenuItem[3];
         miVer[0] = new MenuItem(MENU_VER_MALLA_NO);
         miVer[1] = new MenuItem(MENU_VER_MALLA_CUAD);
         miVer[2] = new MenuItem(MENU_VER_MALLA_TRI);
         MenuListener menuLis = new MenuListener(this);
         mnDoc.add(miDoc);
         miDoc.addActionListener(menuLis);
        //for(int i=0; i < miArch.size(); i++) {
         for(String name : archItems) {
        	 MenuItem mi = new MenuItem(name);
            mi.addActionListener(menuLis);
            mnArch.add(mi);
         }
         for(int i=0; i < 3; i++) {
            mnVer.add(miVer[i]);
            miVer[i].addActionListener(menuLis);
         }
      
         barraMenu.add(mnArch);
         barraMenu.add(mnDoc);
         barraMenu.add(mnVer);
      }
   
       JCastiglioneView getGraf3View() {
         return view;
      }
   
       JCastiglioneDoc getGraf3Doc() {
         return doc;
      }
   
   
   void exportVideo(Frame padre)
   {
       FileDialog dlg = new FileDialog(padre, "Exportar", FileDialog.SAVE);
       dlg.setVisible(true);
       String total;
       if(dlg.getFile() == null) 
          return;
       total = dlg.getDirectory() + dlg.getFile();
       try
       {
          doc.exportVideo(total);
       }
           catch(Exception e)
          {
             System.out.println("ERROR:"+ e.getMessage());
          }   
   }

   public void loadXML(String filename) throws XMLStreamException, IOException {
	   
	   JCgDocLoader docl = new JCgDocLoader();
	   
	   docl.loadXML(filename);

	   //TODO:set new doc
   }
   
   public void saveXML(String filename) throws XMLStreamException, IOException
   {

	   XMLGraf3Doc xd = new XMLGraf3Doc(doc);
	   xd.saveXML(filename);
   }

   
   private void doFileLoad() {
       FileDialog dlg = new FileDialog(this, "Abrir Documento", FileDialog.LOAD);
       dlg.setVisible(true);
       String total;
       System.out.println("Cargando:");
       if(dlg.getFile() == null) 
          return;
       total = dlg.getDirectory() + dlg.getFile();
       try
       {
          doc.load(total);
       }
           catch(Exception e)
          {
             System.out.println("ERROR:"+ e.getMessage());
          }
   }
   
   private void doFileSave() {
       FileDialog dlg = new FileDialog(this, "Guardar Documento", FileDialog.SAVE);
       dlg.setVisible(true);
       String total;
       if(dlg.getFile() == null) 
          return;
       total = dlg.getDirectory() + dlg.getFile();
       try
       {
          doc.save(total);
        }
           catch(Exception e)
          {
             System.out.println("ERROR:"+ e.getMessage());
          }

   }
   
   /**
    * Esta clase atiende a los mensajes de comandos de menÃº
    */
       private class MenuListener implements ActionListener {
         Frame padre = null;
      
          public MenuListener(Frame _prt)
         {
            padre = _prt;
         }
      
          public void actionPerformed(ActionEvent a) {
            Object obj = a.getSource();
            if(!(obj instanceof MenuItem)) 
               return;
            String s = a.getActionCommand();
         
            if(s.equals(MENU_ARC_NUEVO)) {
            
            } 
            else if(s.equals(MENU_ARC_ABRIR)) {
            	doFileLoad();
            } 
            else if(s.equals(MENU_ARC_GUARDAR)) {
            	doFileSave();
            } 
            else if(s.equals(MENU_ARC_GUARDARX)) {
                FileDialog dlg = new FileDialog(padre, "Guardar Documento", FileDialog.SAVE);
                dlg.setVisible(true);
                String total;
                if(dlg.getFile() == null) 
                   return;
                total = dlg.getDirectory() + dlg.getFile();
                try
                {
                   //doc.save(total);
             	   saveXML(total);
                }
                    catch(Exception e)
                   {
                      System.out.println("ERROR:"+ e.getMessage());
                   }
             } 
            else if(s.equals(MENU_ARC_LOADX)) {
                FileDialog dlg = new FileDialog(padre, "Abrir Documento", FileDialog.LOAD);
                dlg.setVisible(true);
                String total;
                System.out.println("Cargando:");
                if(dlg.getFile() == null) 
                   return;
                total = dlg.getDirectory() + dlg.getFile();
                try
                {
                	log.info("Loading XML");
                   JCgDocLoader docl = new JCgDocLoader();
                   docl.loadXML(total);
                   log.info("copy doc");
                   doc.copy(docl);
                   LayerManager lm = doc.getLayerManager();
                   log.info("doc layers:"+lm.numLayers());
                   Layer ly = lm.getCurrent();
                   log.info("figures:"+ly.numFiguras());
                   view.repaint();
                }
                    catch(Exception e)
                   {
                      //System.out.println("ERROR:"+ e.getMessage());
                    	e.printStackTrace();
                   }
             

          }
            else if(s.equals(MENU_ARC_EXPSVG)) {
               FileDialog dlg = new FileDialog(padre, "Exportar", FileDialog.SAVE);
               dlg.setVisible(true);
               String total;
               if(dlg.getFile() == null) 
                  return;
               total = dlg.getDirectory() + dlg.getFile();
               try
               {
                  doc.exportSVG(total);
               }
                   catch(Exception e)
                  {
                     System.out.println("ERROR:"+ e.getMessage());
                  }
            } 
       
               else if(s.equals(MENU_ARC_EXPVID)) {
            	  exportVideo(padre);

            } 
            else if(s.equals(MENU_ARC_SALIR)) {
               dispose();
               System.exit(1);
            } 
            else if(s.equals(MENU_DOC_AJFONDO)) {
               view.sendMessage(JCastiglioneView.MSG_AJUSTAR_FONDO, null);
            }
            else if(s.equals(MENU_VER_MALLA_NO)) {
					/* desactivar malla */
					view.setMesh(JCastiglioneView.MALLA_NO, false);
            }
            else if(s.equals(MENU_VER_MALLA_CUAD)) {
					/* malla cuadrada */
					view.setMesh(JCastiglioneView.MALLA_CUAD, true);
            }
            else if(s.equals(MENU_VER_MALLA_TRI)) {
					/* malla triangular */
					view.setMesh(JCastiglioneView.MALLA_TRI, true);
            }
         }
      }
   
   //-----------------------------------------------------------
   // Las clases Btn_____ Representan a cada boton de la ventana
   //-----------------------------------------------------------
   
   
   // Boton Cargar Imagen
       private class BtnLoadImage extends Button implements ActionListener {
         public static final String title = "Cargar Fondo";
         private View view;
         private Frame parent;
      
          public BtnLoadImage(int px, int py, int w, int h, View _vw, Frame _prt) {
            super(title);
            addActionListener(this);
            setLocation(px, py);
            setSize(w, h);
            view = _vw;
            parent = _prt;
         }
      
          public void actionPerformed(ActionEvent e) {
            FileDialog dlg = new FileDialog(parent, "Cargar Imagen", FileDialog.LOAD);
            dlg.show();
            String total;
            total = dlg.getDirectory() + dlg.getFile();
            System.out.println(total);
            try {
               File imgFile = new File(total);
               Image img = ImageIO.read(imgFile);
            
               if(img == null) {
                  System.out.println("img NULL");
                  return;
               }
               System.out.println("view");
               if(view == null) System.out.println("view null");
               view.sendMessage(JCastiglioneView.SEND_BACKGROUND, img);
            } 
                catch(Exception ex) {
                  System.out.println("Fallo cargando imagen: \n"+ex.toString());
               }
         }
      }
       
   
   // Barra de Scroll
   
       private class Barra extends Scrollbar implements AdjustmentListener {
         View view;
      
          public Barra(int pos, int val, int vis, int min, int max, View _vw) {
            super(pos, val, vis, min, max);
            view = _vw;
            addAdjustmentListener(this);
         }
      
          public void adjustmentValueChanged(AdjustmentEvent e) {
            view.sendMessage(JCastiglioneView.MSG_CHG_SCROLL, null);
         /*			switch(getOrientation()) {
         case HORIZONTAL:
         	break;
         case VERTICAL:
         	break;
         }*/
         
         }
      
      }
   
   
   }
	
