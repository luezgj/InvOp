/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpinvop.Interfaz;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import tpinvop.Cadena;
import tpinvop.Cohorte;
import tpinvop.Simulador;

/**
 *
 * @author lucho
 */
public class VentanaSimulacion extends javax.swing.JFrame {
    private ArrayList<mxGraph> graphs= new ArrayList();
    private List<Object> verticesAnteriores= new ArrayList();
    boolean[] grafoCreado;
    private int alumnosInicio=120;
    
    final static private int DISTANCIAX_ENTRE_BLOQUES = 150;
    final static private int DISTANCIAY_ENTRE_BLOQUES = 70;
    final static private int ANCHO_BLOQUE = 100;
    final static private int ALTO_BLOQUE = 35;
    
    final static private int CANTIDAD_AÑOS = 9;
    private int añoInicio;
    
    private Simulador simulador;
    
    /**
     * Creates new form VentanaSimulacion
     * @param cohorte
     * @param añoInicio
     */
    public VentanaSimulacion(Cohorte cohorte,int añoInicio, int cantAlumnos) {
        initComponents();
        
        setSize(800,600);
        this.setLocationRelativeTo(null);
        
        this.alumnosInicio = cantAlumnos;
        
        simulador= new Simulador(cohorte.getCadenas(),alumnosInicio);
        
        grafoCreado=new boolean[cohorte.getCadenas().size()];
        this.añoInicio=añoInicio;
        
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
        int i=1;
        for (Cadena cadena: cohorte.getCadenas()){
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
        mostrarSimulacion(graphs.get(0), 0, cohorte.getCadenas());
        
        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                mostrarSimulacion(graphs.get(tabs.getSelectedIndex()), tabs.getSelectedIndex(), cohorte.getCadenas());
            }
        });
        
    }
    
    //Hacer que se genere(mostrarSimulacion) cuando cambias de pestaña
    
    private void mostrarSimulacion(mxGraph graph,int nLinea,List<Cadena> cadenas){
        if (!grafoCreado[nLinea]){
            grafoCreado[nLinea]=true;
            System.out.println("Llamo a simular");
            simulador.simular(nLinea);
            graph.getModel().beginUpdate();
            System.out.println(cadenas.get(nLinea).getLinea().getCantMaterias());
            int posX=15,posY=0;
            Object parent=graph.getDefaultParent();
            Object vertice;
            boolean primero=false;
            for (int añoRelativo=0;añoRelativo<CANTIDAD_AÑOS;añoRelativo++){
                posY=15;
                primero=true;
                for(int nodo=añoRelativo;nodo>=0;nodo--){
                    if (nodo<=cadenas.get(nLinea).getLinea().getCantMaterias()){
                        String textoVertice;
                                
                        if(nodo == cadenas.get(nLinea).getLinea().getCantMaterias()){
                            textoVertice="Finalizado";
                        }
                        else {
                            int añoActual=añoInicio+añoRelativo;
                            textoVertice="Nodo "+nodo+" - "+añoActual+"\n"+simulador.getCantidad(nodo, añoRelativo);
                        }
                            
                        vertice = graph.insertVertex(parent,null,textoVertice,posX,posY,ANCHO_BLOQUE,ALTO_BLOQUE);
                        verticesAnteriores.add(vertice);

                        if (primero && añoRelativo!=0 && añoRelativo+1<=cadenas.get(nLinea).getLinea().getCantMaterias()){
                            graph.insertEdge(parent, null, simulador.getAprobados(nodo-1, añoRelativo-1), verticesAnteriores.get(0), vertice);
                            primero=false;
                        }else if (añoRelativo!=0){
                            graph.insertEdge(parent, null, simulador.getDesaprobados(nodo, añoRelativo-1), verticesAnteriores.get(0), vertice);
                            if (nodo!=0)
                                graph.insertEdge(parent, null, simulador.getAprobados(nodo-1, añoRelativo-1), verticesAnteriores.get(1), vertice);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
