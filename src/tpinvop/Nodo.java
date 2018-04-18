package tpinvop;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    List<Materia> Materias;
    
    public Nodo(){
        Materias= new ArrayList<>();
    }
    
    public void add(Materia m){
        Materias.add(m);
    }
    
    public int getCantMaterias(){
        return Materias.size();
    }
    
    public Materia getMateria(int pos){
        return Materias.get(pos);
    }
}
