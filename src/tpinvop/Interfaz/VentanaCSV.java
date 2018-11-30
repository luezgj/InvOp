/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpinvop.Interfaz;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import tpinvop.AdminBD;
import tpinvop.Cadena;
import tpinvop.Carrera;
import tpinvop.Cohorte;
import tpinvop.GeneradorCadena;

/**
 *
 * @author Administrador
 */
public class VentanaCSV extends javax.swing.JFrame {

    VentanaPrincipal vp;
    JFileChooser abrirArchivo;
    String pathCorrelativas;
    String pathCSV;
    
    public static final int _AÑO_PRIMER_COHORTE=2011;
    public static final int _AÑO_ULTIMA_COHORTE=2017;
    
    public VentanaCSV() {
        super("Analisis de carrera");
        pathCorrelativas = null;
        pathCSV = null;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        BotonContinuar = new javax.swing.JButton();
        BotonExaminarCSV = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        BotonExaminarCorrelativas = new javax.swing.JButton();
        BotonVolver = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2013", "2014", "2015", "2016", "2017", "2018" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Cohorte:");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BotonContinuar.setText("Cadenas");
        BotonContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonContinuarActionPerformed(evt);
            }
        });

        BotonExaminarCSV.setText("Examinar");
        BotonExaminarCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonExaminarCSVActionPerformed(evt);
            }
        });

        jLabel1.setText("Seleccionar Archivo de Correlativas");

        BotonExaminarCorrelativas.setText("Examinar");
        BotonExaminarCorrelativas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonExaminarCorrelativasActionPerformed(evt);
            }
        });

        BotonVolver.setText("Volver");
        BotonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonVolverActionPerformed(evt);
            }
        });

        jLabel2.setText("Selecionar Archivo CSV");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BotonVolver)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 24, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(BotonExaminarCorrelativas))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(BotonExaminarCSV)))
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(BotonContinuar)
                                .addGap(131, 131, 131))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(BotonExaminarCSV))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(BotonExaminarCorrelativas))
                .addGap(5, 5, 5)
                .addComponent(jLabel4)
                .addGap(56, 56, 56)
                .addComponent(BotonContinuar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(BotonVolver)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonContinuarActionPerformed
        if (pathCorrelativas != null){
            if (pathCSV != null ){
                AdminBD db = new AdminBD("cursadas","vista","alumnos", true);
        
                db.connectDatabase();
                db.dropTables();
                db.getData(pathCSV, "./recursos/alumnosbien.csv", 206, "2011"); 
                
                Carrera carrera=new Carrera(pathCorrelativas);
                GeneradorCadena genCadenas = new GeneradorCadena(db);
                
                Map<Integer, Cohorte> cohortes= new HashMap();
                for (int año=_AÑO_PRIMER_COHORTE;año<=_AÑO_ULTIMA_COHORTE;año++){
                    db.generatePassTable(carrera.getNodos(), año);
                    System.out.println("Llamo a getCadenas");
                    List<Cadena> cadenas= genCadenas.getCadenas(carrera,año);
                    Cohorte cohorte= new Cohorte(cadenas, año);
                    cohortes.put(año, cohorte);
                }
                
                this.setVisible(false);
                         
                new VentanaResultados(genCadenas.getCadenas(carrera),cohortes).setVisible(true);
                
            }
            else{
                VentanaError vr=new VentanaError("Archivo de Correlativas no seleccionado.");
                this.setVisible(false);
                vr.setVisible(true);
            }
        }
        else{
            VentanaError vr=new VentanaError("Archivo CSV no seleccionado.");
            this.setVisible(false);
            vr.setVisible(true);
      }
    }//GEN-LAST:event_BotonContinuarActionPerformed

    private void BotonExaminarCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonExaminarCSVActionPerformed
        JButton btn = (JButton)evt.getSource();
        if( btn.getText().equals( "Examinar" ) )
        {
            if( abrirArchivo == null ) abrirArchivo = new JFileChooser();
            //Con esto solamente podamos abrir archivos
            abrirArchivo.setFileSelectionMode( JFileChooser.FILES_ONLY );
 
            int seleccion = abrirArchivo.showOpenDialog( this );
 
            if( seleccion == JFileChooser.APPROVE_OPTION )
            {
                File f = abrirArchivo.getSelectedFile();
                try
                {
                    String nombre = f.getName();
                    pathCSV = f.getAbsolutePath();
                    //Colocamos en el titulo de la aplicacion el 
                    //nombre del archivo
                    this.setTitle( nombre );
 
                    //En el editor de texto colocamos su contenido
 
                }catch( Exception exp){}
            }
        }
    }//GEN-LAST:event_BotonExaminarCSVActionPerformed

    private void BotonExaminarCorrelativasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonExaminarCorrelativasActionPerformed
        JButton btn = (JButton)evt.getSource();
        if( btn.getText().equals( "Examinar" ) )
        {
            if( abrirArchivo == null ) abrirArchivo = new JFileChooser();
            //Con esto solamente podamos abrir archivos
            abrirArchivo.setFileSelectionMode( JFileChooser.FILES_ONLY );
 
            int seleccion = abrirArchivo.showOpenDialog( this );
 
            if( seleccion == JFileChooser.APPROVE_OPTION )
            {
                File f = abrirArchivo.getSelectedFile();
                try
                {
                    String nombre = f.getName();
                    pathCorrelativas = f.getAbsolutePath();
                    
                    //Colocamos en el titulo de la aplicacion el 
                    //nombre del archivo
                    this.setTitle( nombre );
 
                    //En el editor de texto colocamos su contenido
 
                }catch( Exception exp){}
            }
        }
    }//GEN-LAST:event_BotonExaminarCorrelativasActionPerformed

    private void BotonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonVolverActionPerformed
        vp=new VentanaPrincipal();
        this.setVisible(false);
        vp.setVisible(true);
    }//GEN-LAST:event_BotonVolverActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonContinuar;
    private javax.swing.JButton BotonExaminarCSV;
    private javax.swing.JButton BotonExaminarCorrelativas;
    private javax.swing.JButton BotonVolver;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
