package tpinvop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class Carrera{
    
    String nombre;
    List<Linea> Lineas;
    
    public Carrera(String nombre,String ruta){
        this.nombre=nombre;
        GenerarLineas(ruta);
    }
    
    private void GenerarLineas(String ruta){
        List<Materia> Materias=new ArrayList<>();
        getMaterias(ruta,Materias);
        Lineas=getRamas(Materias);
             
    }
    
    private void getMaterias(String ruta,List<Materia> Materias) {
        File archivo;
        FileInputStream is;
        BufferedReader br = null;
 
        try {
		archivo = new File (ruta);
                is= new FileInputStream(archivo);
                InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
		br = new BufferedReader(isr);
                
                String linea;
		while((linea=br.readLine())!=null){
                    getInfoMateria(linea,Materias);
                }
        }
        catch(Exception e){
           e.printStackTrace();
        }finally{
           try{
              if( null != br ){
                 br.close();
              }
           }catch (Exception e2){
              e2.printStackTrace();
           }
        }
        Collections.reverse(Materias);
    }
    
    private void getInfoMateria(String linea,List<Materia> Materias){
        String lineaMod = "    " + linea;
        String [] partes = lineaMod.split("    ");
       
        int cod=Integer.parseInt(partes[1]);
        String nombre=new String(partes[2]);
        List<Integer> cs=getListCorrelatividades(partes[3]);
        
        Materia m= new Materia(cod,nombre,cs);
        Materias.add(m);
    }
    
    private List<Integer> getListCorrelatividades(String correlatividades){
        List<Integer> salida=new ArrayList<>();
        String lineaMod = ","+correlatividades;
        String [] partes = lineaMod.split(",");
        if (partes[1].equals("-"))
            return salida;
        for (int i=1; i < partes.length ; i++){
            Integer cod=Integer.parseInt(partes[i]);
            salida.add(cod);
        }
        return salida;
    }
    
    
    public void mostrarLineas(){
        for (Linea l:Lineas){
            l.MostrarNodos();
            System.out.println("");
       }
    }
    
    private boolean Pertenece(Materia m){
        if (Lineas.size() > 0)
        for (Linea l : Lineas) {
            if (l.contieneMateria(m)){
                return true;
            }
        }
        return false;
    }

    //Devuelve la materia dado el codigo de la materia
    private Materia getMateriaXcod(int cod,List<Materia> Materias){
        for(Materia m:Materias){
            if (m.getCod() == cod)
               return m;
        }
        return null;
    }
    
    //Dado un nodo devuelve las materias que tengan correlativas
    //En caso de que dos materia tenga en comun una correlativa, solo se devuelve una sola
    //Si no tiene correlativa no se inserta
    private List<Materia> getMatSig(Nodo n){
        List<Materia> salida=new ArrayList<>();
        Iterator<Materia> it=n.iterator();
        Materia aux;
        while (it.hasNext()){
            aux= it.next();
            if (aux.tieneCorrelativas()){
                if (salida.isEmpty())
                    salida.add(aux);
                else{
                    boolean mismasCorrelativas=false;
                    for (Materia m: salida)
                        if (m.mismasCorrelativas(aux))
                            mismasCorrelativas=true;
                    if (!mismasCorrelativas)
                        salida.add(aux);
                }
            }         
        }
        return salida;
    }
    
    private void CompletarLinea(Linea l,Materia m,int idLinea,List<Materia> Materias){
        if (m.tieneCorrelativas()){
            List<Integer> codMat=m.getCorrelatividades();
            
            Nodo nodo= new Nodo();
            for(Integer i:codMat){
                nodo.add(getMateriaXcod(i,Materias));
            }
            l.addNodo(nodo);
            
            List<Materia> Msig=getMatSig(nodo);        
            if(Msig.isEmpty()){
                Linea newLinea=l.clone();
                newLinea.setId(idLinea);
                Lineas.add(newLinea);
            }
            else
                for(Materia mat:Msig){
                    CompletarLinea(l,mat,idLinea,Materias); 
                }
            l.eliminarUltimoNodo();
        }
    }    
    
    //Crea todas las lineas
    private List<Linea> getRamas(List<Materia> Materias){
        Lineas= new ArrayList<>();
        int idLinea=1;
        Linea l;
        for(Materia m:Materias){
             if (!Pertenece(m)){
                l=new Linea(idLinea);
                l.addMateria(m);
                if (!m.tieneCorrelativas()){
                    Lineas.add(l);
                }
                else{
                    CompletarLinea(l,m,idLinea,Materias);
                }
                idLinea++;
                
             } 
        }
        return Lineas;
    }

}
