package tpinvop;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Nodo implements Iterable<Materia> {
    List<Materia> Materias;
    
    public Nodo(){
        
        Materias= new ArrayList<>();
      
    }
    
    public void add(Materia m){
        Materias.add(m);
    }
    
    public Nodo clone(){
        Nodo salida=new Nodo();
        Materia materiaNew;
        for(Materia m:Materias){
            materiaNew=new Materia(m.getCod(),m.getNombre(),m.getCorrelatividades());
            salida.add(materiaNew);
        }
        return salida;
    }
    
    public boolean contieneMateria(Materia m){
        for (Materia mat:Materias){
            if (mat.equals(m))
                return true;
        }
        return false;
    }
    
    public String toString(){
        String contenido="";
        for(Materia m:Materias){
            contenido+=m.getCod();
            contenido+=" ";
        }
        
        return contenido;
    }
    
    public Iterator<Materia> iterator() {    
        Iterator it = new IteratorMateria();
        return it;         
    }

    public int getCantMaterias(){
            return Materias.size();
    }
    
    protected class IteratorMateria implements Iterator<Materia>
    {
        protected int posicionarray;
        public IteratorMateria(){ 
            posicionarray = 0; 
        }
        @Override
        public boolean hasNext(){
            boolean result;
            if (posicionarray < Materias.size())
                result = true; 
            else 
                result = false; 
            return result;
        }

        @Override
        public Materia next(){
            posicionarray++;
            return Materias.get(posicionarray-1);
        }

        @Override
        public void remove(){
            
            throw new UnsupportedOperationException("No soportado.");

        }
        
    }
}