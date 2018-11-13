package tpinvop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Carrera{
    
    List<Linea> Lineas;
    List<Materia> Materias;
    HashMap<Integer,Boolean> materiaUsada;
    
    //Constructor carrera
    public Carrera(String ruta){
        GenerarLineas(ruta);
    }
    
    //Genera las lineas de la carrera, primero obtiene la lista de materias
    // y luego genera las lineas correspondientes segun las correlatividades
    // de cada una. Se realiza desde las materias finales a las iniciales
    private void GenerarLineas(String ruta){
        Materias=new ArrayList<>();
        getMaterias(ruta);
        Lineas=getRamas();
             
    }
        
    private void inicializarHash(){
        if(this.materiaUsada == null)
            materiaUsada = new HashMap();
        for (Materia m : Materias)
            materiaUsada.put(m.getCod(), false);
    }
    
    
    
    
    //Metodo para obtener desde un archivo la lista de materias para generar las lineas a analizar
    private void getMaterias(String ruta) {
        File archivo;
        FileInputStream is;
        BufferedReader br = null;
 
        try {
		archivo = new File (ruta);
                is= new FileInputStream(archivo);
                InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
		br = new BufferedReader(isr);
                
        String linea;
        
        // Se obtiene los datos de la linea del archivo
		while((linea=br.readLine())!=null){
                    getInfoMateria(linea,Materias);
                }
        }
        catch(Exception e){
           e.printStackTrace();
        }finally{
           try{
               //Si el archivo existe y se leyo cierra el buffer 
              if( null != br ){
                 br.close();
              }
           }catch (Exception e2){
              e2.printStackTrace();
           }
        }
        Collections.reverse(Materias);
    }
    
    //Metodo que obtiene de cada linea del archivo los datos de cada materia
    //Formato? : [CodMateria\tab\NomMateria\tab\{Correlativas\,\}]
    private void getInfoMateria(String linea,List<Materia> Materias){
        //String lineaMod = "	" + linea;
        String [] partes = linea.split(";");
       
        int cod=Integer.parseInt(partes[0].trim());
        String nombre=new String(partes[1].trim());
        List<Integer> cs=getListCorrelatividades(partes[2].trim());
        
        Materia m= new Materia(cod,nombre,cs);
        Materias.add(m);
    }
    
    // Obtiene la lista de correlatividades...
    private List<Integer> getListCorrelatividades(String correlatividades){
        List<Integer> salida=new ArrayList<>();
        String lineaMod = ","+correlatividades;
        String [] partes = lineaMod.split(",");
        if (partes[1].equals("-"))
            return salida;
        for (int i=1; i < partes.length ; i++){
            Integer cod=Integer.parseInt(partes[i].trim());
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
    
    //Comprueba si la materia pasada por parametro esta incluida en alguna linea
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
    
    private void CompletarLinea(Linea l,Materia m,int idLinea,Nodo nodo){
        
        Nodo nodoInicial = new Nodo();
        nodoInicial.add(m);
        Nodo nuevoNodo;
        Nodo nodoSig = nodoInicial;
        List<Integer> codMat;
        while (nodoSig.getCantMaterias() > 0) {
            nuevoNodo = new Nodo();
            for (Materia mat : nodoSig){
                codMat=mat.getCorrelatividades();
                //Inserta en el nodo las correlatividades correspondientes
                for(Integer codCorrelativa:codMat){
                    if (materiaUsada.get(codCorrelativa)){
                        nuevoNodo.eliminarMateria(getMateriaXcod(codCorrelativa,Materias));
                        l.eliminarMateria(getMateriaXcod(codCorrelativa,Materias));
                    }
                    nuevoNodo.add(getMateriaXcod(codCorrelativa,Materias));
                    materiaUsada.put(codCorrelativa, true);
                    
                }
            }
            if (nuevoNodo.getCantMaterias() > 0)
                l.addNodo(nuevoNodo);
            nuevoNodo.ordenar();
            nodoSig = nuevoNodo;
        }
    }
        
    
    //Crea todas las lineas *****OK*******
    private List<Linea> getRamas(){
        Lineas= new ArrayList<>();
        int idLinea=1;
        Linea l;
        Nodo nodo;
        for(Materia m:Materias){
             if (!Pertenece(m)){
                inicializarHash();
                l=new Linea(idLinea);
                l.addMateria(m);
                if (!m.tieneCorrelativas()){
                    Lineas.add(l);
                }
                else{
                    nodo = new Nodo();
                    CompletarLinea(l,m,idLinea,nodo);
                    Lineas.add(l);
                }
                idLinea++;
                
             } 
        }
        
        return Lineas;
    }
    
    public List<Linea> getLineas() {
        return Lineas;
    }
    
    //Devuelve el conjunto(sin repeticiones) de nodos que contiene la carrera
    public List<Nodo> getNodos(){
        List<Nodo> outNodos= new LinkedList<>();
        for (Linea linea: Lineas){
            for(Nodo nodo: linea.getNodos()){
                if(nodo.getCantMaterias()!=1 && !outNodos.contains(nodo)){
                    outNodos.add(nodo);
                }
            }
        } 
        return outNodos;
    }

}
