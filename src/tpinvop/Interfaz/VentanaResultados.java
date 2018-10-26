package tpinvop.Interfaz;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import tpinvop.Materia;
import tpinvop.Nodo;
import tpinvop.Cadena;
import static tpinvop.main._AÑO_PRIMER_COHORTE;

public class VentanaResultados extends javax.swing.JFrame {

    private mxGraph graph;
    private mxGraphComponent graphComponent;
    final static private int DISTANCIAX_ENTRE_BLOQUES = 250;
    final static private int DISTANCIAY_ENTRE_BLOQUES = 150;
    final static private int ANCHO_BLOQUE = 220;
    final static private int ALTO_BLOQUE = 100;   
    
    final static private int DISTANCIAY_ENTRE_BLOQUES_CHICOS = 20;
    
    final static private int ANCHO_BLOQUE_CHICO = 50;
    final static private int ALTO_BLOQUE_CHICO = 20;   
    
    Object parent;
    
    public VentanaResultados(List<Cadena> cadenas) {
        super("Cadenas");
        setSize(800,600);
        this.setLocationRelativeTo(null);
        crearCadenas(cadenas);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void crearCadenas(List<Cadena> cadenas){
        graph = new mxGraph();
        
        graph.getModel().beginUpdate();
        
        parent = graph.getDefaultParent();
        try{
            int distanciaYEntreBloques = 5;

            for (Cadena c : cadenas){
                crearVertices(c,distanciaYEntreBloques);
                distanciaYEntreBloques+=DISTANCIAY_ENTRE_BLOQUES;
            }  
        } finally {
		graph.getModel().endUpdate();
	}
        
        graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
        
    }
    
    void crearVertices(Cadena c,int posY){
        boolean primerNodo = true;
        Object v1 = null;
        Object v2 = null;

        Iterator<Nodo> itLinea = c.getLinea().iterator();
        int posX=5;
        
        while ( itLinea.hasNext()){
            Nodo nodo=itLinea.next();
            Iterator<Materia> itMaterias = nodo.iterator();
            
            v2 = graph.insertVertex(parent,null,""/*nodo.getNombre()*/,posX,posY,ANCHO_BLOQUE,ALTO_BLOQUE);
            double posXchico=10d;
            double posYchico=10d;
            for (Materia m: nodo){
                graph.insertVertex(v2,null,m.getNombre(),posXchico,posYchico,ANCHO_BLOQUE,ALTO_BLOQUE_CHICO);
                posYchico+=DISTANCIAY_ENTRE_BLOQUES_CHICOS;
            }
            graph.foldCells(true, true, new Object[]{v2});
            
            if (!primerNodo)
                //              (parent, null, ACA VA LA PROBABILIDAD, v1, v2);
                graph.insertEdge(parent, null, "", v1, v2);
            else
                primerNodo=false;
            v1 = v2;
            posX+=DISTANCIAX_ENTRE_BLOQUES;

        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jScrollPane2.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
    //public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //try {
            /*
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }*/
            /*javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaResultados().setVisible(true);
            }
        });
    }*/
    
    public void setTextPan(String contenido){
        jTextPane1.setText( contenido );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
