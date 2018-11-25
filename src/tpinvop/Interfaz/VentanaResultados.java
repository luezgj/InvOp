package tpinvop.Interfaz;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import tpinvop.Materia;
import tpinvop.Nodo;
import tpinvop.Cadena;
import tpinvop.Carrera;
import tpinvop.Cohorte;
import tpinvop.GeneradorCadena;
import tpinvop.GeneradorInformacion;

public class VentanaResultados extends javax.swing.JFrame {

    private mxGraph graph;
    private mxGraphComponent graphComponent;
    final static private int DISTANCIAX_ENTRE_BLOQUES = 280;
    final static private int DISTANCIAY_ENTRE_BLOQUES = 150;
    final static private int ANCHO_BLOQUE = 220;
    final static private int ALTO_BLOQUE = 100;   
    
    final static private int DISTANCIAY_ENTRE_BLOQUES_CHICOS = 20;
    
    final static private int ANCHO_BLOQUE_CHICO = 50;
    final static private int ALTO_BLOQUE_CHICO = 20;   
    
    JPanel panelArriba;
    JComboBox<String> Año;
    GeneradorCadena generadorCadena;
    GeneradorInformacion generadorInformacion;
    JButton botonSimular;
    
    int añoSeleccionado;
    
    Object parent;
 
    public VentanaResultados(GeneradorCadena genCadenas,Carrera carrera) {
        super("Cadenas");
        setSize(800,600);
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
        panelArriba = new JPanel();
        editarPanelArriba();
        
        JTextArea textArea = new JTextArea(10, 5);
        textArea.setEditable(false);
        
        add(panelArriba);
        add(textArea);
        añoSeleccionado = Integer.parseInt((String)Año.getSelectedItem());
        List<Cadena> cadenas= genCadenas.getCadenas(carrera,añoSeleccionado);
        crearCadenas(cadenas);
        for (Cadena c : cadenas){
            textArea.append(GeneradorInformacion.tiempoEsperadoRama(c)+"\n");
        }
        
        Año.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        getContentPane().remove(2);
                        añoSeleccionado = Integer.parseInt((String)Año.getSelectedItem());
                        crearCadenas(genCadenas.getCadenas(carrera,añoSeleccionado));
                        textArea.setText("");
                        for (Cadena c : cadenas)
                            textArea.append(GeneradorInformacion.tiempoEsperadoRama(c)+"\n");
                }
        });
        
        botonSimular.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Cohorte cohorte= new Cohorte(cadenas, añoSeleccionado);
                new VentanaSimulacion(cohorte,añoSeleccionado).setVisible(true);
            }
        });
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void editarPanelArriba(){
        botonSimular = new JButton("Simular");
        Año = new JComboBox();
        JLabel label = new JLabel("Cohote");
        Año.addItem("2011");
        Año.addItem("2012");
        Año.addItem("2013");
        Año.addItem("2014");
        Año.addItem("2015");
        Año.addItem("2016");
        Año.addItem("2017");
        Año.addItem("2018");
        panelArriba.add(label);
        panelArriba.add(Año);
        panelArriba.add(botonSimular);
    }

    void crearCadenas(List<Cadena> cadenas){
        graph = new mxGraph(); 
        parent = graph.getDefaultParent(); 
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
        add(graphComponent);
    }
    
    void crearVertices(Cadena c,int posY){
        boolean primerNodo = true;
        Object v1 = null;
        Object v2 = null;

        Iterator<Nodo> itLinea = c.getLinea().iterator();
        int posX=5;
        int i=0;
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
            graph.foldCells(false, true, new Object[]{v2});
            
            if (!primerNodo)
                //              (parent, null, ACA VA LA PROBABILIDAD, v1, v2);
                graph.insertEdge(parent, null,c.getProbAprobar(i) , v1, v2);
            else
                primerNodo=false;
            v1 = v2;
            posX+=DISTANCIAX_ENTRE_BLOQUES;
            i++;

        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(216, 216, 216)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
