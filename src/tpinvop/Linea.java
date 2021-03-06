package tpinvop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class Linea implements Iterable<Nodo>{
    int id;
    private List<Nodo> Materias;
    
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
    
    public void eliminarMateria(Materia m){
        for (Nodo nodo : Materias){
            nodo.eliminarMateria(m);
        }
    }

    public void invertir(){
        Collections.reverse(Materias);
    }
    
    public void setDifCuatrimestresNodos(){
        for (Nodo n : Materias)
            n.setDifCuatrimestres();
    }
    
    public int getDifCuatrimestre(int nroNodo){
        int cont = 0;
        for (Nodo nodo : Materias){
            if (cont == nroNodo)
                return nodo.getDifCuatrimestres();
            cont++;
        }
        return 0;
    }
    
    @Override
    public Iterator<Nodo> iterator() {
        return Materias.iterator();
    }
    
    public List<Nodo>getNodos() {
        return Materias;
    }
    
}