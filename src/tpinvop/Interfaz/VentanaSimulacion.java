/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpinvop.Interfaz;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import tpinvop.Cadena;
import tpinvop.Simulador;

/**
 *
 * @author lucho
 */
public class VentanaSimulacion extends javax.swing.JFrame {
    private ArrayList<mxGraph> graphs= new ArrayList();
    private List<Object> verticesAnteriores= new ArrayList();
    
    final static private int DISTANCIAX_ENTRE_BLOQUES = 150;
    final static private int DISTANCIAY_ENTRE_BLOQUES = 70;
    final static private int ANCHO_BLOQUE = 100;
    final static private int ALTO_BLOQUE = 35;
    
    final static private int CANTIDAD_AÑOS = 9;
    private int añoInicio;
    
    private Simulador simulador= new Simulador();
    
    /**
     * Creates new form VentanaSimulacion
     * @param cadenas
     */
    public VentanaSimulacion(List<Cadena> cadenas,int añoInicio) {
        initComponents();
        
        setSize(800,600);
        
        this.añoInicio=añoInicio;
        
        int i=1;
        for (Cadena cadena: cadenas){
            JPanel tab = new JPanel();
            mxGraph graph= new mxGraph();
            mxGraphComponent graphComponent = new mxGraphComponent(graph);
            
            tab.setOpaque(false);
            tab.add(graphComponent);
            
            JScrollPane scrollPanel=new JScrollPane(tab);
            
            tabs.addTab("Linea "+i, scrollPanel);
            graphs.add(graph);
            
            i++;
        }
        
        mostrarSimulacion(graphs.get(0), 1, cadenas);
        
        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                mostrarSimulacion(graphs.get(tabs.getSelectedIndex()), tabs.getSelectedIndex(), cadenas);
            }
        });
        
    }
    
    //Hacer que se genere(mostrarSimulacion) cuando cambias de pestaña
    
    private void mostrarSimulacion(mxGraph graph,int nLinea,List<Cadena> cadenas){
        graph.getModel().beginUpdate();
        System.out.println(cadenas.get(nLinea).getLinea().getCantMaterias());
        int posX=15,posY=0;
        Object parent=graph.getDefaultParent();
        Object vertice;
        boolean primero=false;
        for (int añoRelativo=0;añoRelativo<CANTIDAD_AÑOS;añoRelativo++){
            posY=15;
            primero=true;
            for(int nodo=añoRelativo+1;nodo>0;nodo--){
                if (nodo<=cadenas.get(nLinea).getLinea().getCantMaterias()){
                    int añoActual=añoInicio+añoRelativo;
                    String textoVertice="Nodo "+nodo+" - "+añoActual+"\n"+simulador.getCantidad(nLinea, nodo, añoRelativo);
                    vertice = graph.insertVertex(parent,null,textoVertice,posX,posY,ANCHO_BLOQUE,ALTO_BLOQUE);
                    verticesAnteriores.add(vertice);

                    if (primero && añoRelativo!=0 && añoRelativo+1<=cadenas.get(nLinea).getLinea().getCantMaterias()){
                        graph.insertEdge(parent, null, "#Pasan", verticesAnteriores.get(0), vertice);
                        primero=false;
                    }else if (añoRelativo!=0){
                        graph.insertEdge(parent, null, "#Pasan", verticesAnteriores.get(0), vertice);
                        if (nodo!=1)
                            graph.insertEdge(parent, null, "#Pasan", verticesAnteriores.get(1), vertice);
                        verticesAnteriores.remove(0);
                    }
                }
                posY+=DISTANCIAY_ENTRE_BLOQUES;
            }
            posX+=DISTANCIAX_ENTRE_BLOQUES;
        }
        graph.getModel().endUpdate();
        verticesAnteriores.clear();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulación");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
