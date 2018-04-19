package tpinvop;

import java.util.ArrayList;
import java.util.List;


public class Linea {
    int id;
    List<Nodo> Materias;
    
    public Linea(int id){
        this.id=id;
        Materias = new ArrayList<>();
    }
    
    public void addMateria(Materia m){
        Nodo aux=new Nodo();
        aux.add(m);
        Materias.add(aux);
    }
    
    public void addNodo(Nodo n){
        Materias.add(n);
    };
    
    public void setMaterias(List<Nodo> ms){
        Materias=ms;
    }
    
    public int getCantMaterias(){
        return Materias.size();
    }
    
    public Linea clone(){
        Linea salida=new Linea(this.id);
        List<Nodo> ms=new ArrayList<>();
        for(Nodo n : Materias){
            Nodo nodoNew=n.clone();
            ms.add(nodoNew);
        }
        salida.setMaterias(ms);
        return salida;
    }
    
    public boolean contieneMateria(Materia m){
        for(Nodo n : Materias)
            if (n.contieneMateria(m))
                return true;
        return false;
    }

    public int getId() {
        return id;
    }
    
    public void MostrarNodos(){
        for(Nodo n : Materias){
            System.out.print(" "+n.toString()+ " - ");
        }
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void eliminarUltimoNodo(){
        Materias.remove(Materias.size()-1);
    }
    
}