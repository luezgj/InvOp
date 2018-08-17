package tpinvop.Interfaz;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTextPane;
import tpinvop.Carrera;
import tpinvop.Linea;
import tpinvop.Materia;
import tpinvop.Nodo;

public class VentanaResultados extends javax.swing.JFrame {

    private mxGraph graph;
    private mxGraphComponent graphComponent;
    final static private int DISTANCIAX_ENTRE_BLOQUES = 230;
    final static private int DISTANCIAY_ENTRE_BLOQUES = 80;
    final static private int ANCHO_BLOQUE = 200;
    final static private int ALTO_BLOQUE = 50;   
    Object parent;
    
    VentanaPrincipal vp;
    //private VentanaPrincipal padre = (VentanaPrincipal)this.getParent();
    public VentanaResultados() {
        //initComponents();
        super("Cadenas");
        setSize(800,600);
        this.setLocationRelativeTo(null);
        graph = new mxGraph();
        graphComponent = new mxGraphComponent(graph);
        graphComponent.setPreferredSize(new Dimension(400,400));
        graphComponent.setEnabled(false);
        getContentPane().add(graphComponent);
       
        graph.getModel().beginUpdate();
        
        crearCadenas();
        
        graph.getModel().endUpdate();
    }

    void crearCadenas(){
        parent = graph.getDefaultParent();
        Carrera IngSist=new Carrera("Ingeniero en Sistemas","recursos/Correlatividades.txt");
        List<Linea> Lineas = IngSist.getLineas(); 
        int distanciaYEntreBloques = 5;
              
        for (Linea l : Lineas){
            crearVertices(l,distanciaYEntreBloques);
            distanciaYEntreBloques+=DISTANCIAY_ENTRE_BLOQUES;
       }  
    }
    
    void crearVertices(Linea l,int posY){
        boolean primerNodo = true;
        Object v1= null;
        Object v2= null;
        Iterator<Nodo> itLinea = l.iterator();
        int posX=5;
        
        while ( itLinea.hasNext()){
            Nodo nodo=itLinea.next();
            Iterator<Materia> itMaterias = nodo.iterator();
            String grupoMaterias = "";
            boolean primerMateria = true;
            while ( itMaterias.hasNext() ){
                if (primerMateria){
                    grupoMaterias+=itMaterias.next().getNombre();
                    primerMateria=false;
                }
                else
                    grupoMaterias+="\n"+itMaterias.next().getNombre();
            }
            //graph.insertVertex(parent,null,"TESTES",COORDENADA X,COORDENADA Y, ANCHO, ALTO);
            v2 = graph.insertVertex(parent,null,grupoMaterias,posX,posY,ANCHO_BLOQUE,ALTO_BLOQUE);
            if (!primerNodo)
                //graph.insertEdge(parent, null, ACA VA LA PROBABILIDAD, v1, v2);
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

        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(210, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        vp = new VentanaPrincipal();
        this.setVisible(false);
        vp.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaResultados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaResultados().setVisible(true);
            }
        });
    }
    
    public void setTextPan(String contenido){
        jTextPane1.setText( contenido );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}