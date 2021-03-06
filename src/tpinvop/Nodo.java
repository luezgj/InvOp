package tpinvop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

public class Nodo implements Iterable<Materia>, Comparable<Nodo>{
    List<Materia> Materias;
    int DifCuatrimestres;
    boolean sorted;
    
    public Nodo(){ 
        Materias= new ArrayList<>();
        sorted=false;
        DifCuatrimestres = 1;
    }
    
    public void add(Materia m){
        Materias.add(m);
        sorted=false;
    }
    
    public String getNombre(){
        if (Materias.size()==1){
            return Materias.get(0).getNombre();
        }else{
            StringBuilder nombre=new StringBuilder();

            Collections.sort(Materias);
            sorted=true;

            for (Materia mat : Materias) {
                nombre.append(mat.getNombre());
                nombre.append(";");
            }

            return nombre.toString();
        }
    }

    public void setDifCuatrimestres(int DifCuatrimestres) {
        this.DifCuatrimestres = DifCuatrimestres;
    }
    
    public void setDifCuatrimestres(){
        for (Materia m : Materias)
            for (Materia m2 : Materias)
                if (!m.equals(m2))
                    if (m.getAnio()!=m2.getAnio() || m.getCuatrimestre()!= m2.getCuatrimestre()){
                        DifCuatrimestres = 2;
                    }
    }

    public int getDifCuatrimestres() {
        return DifCuatrimestres;
    }
    
    @Override
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
    
    @Override
    public String toString(){
        String contenido="";
        for(Materia m:Materias){
            contenido+=m.getNombre();
            contenido+=" ";
        }
        
        return contenido;
    }
    
    @Override
    public Iterator<Materia> iterator() {    
        Iterator it = new IteratorMateria();
        return it;         
    }

    public int getCantMaterias(){
            return Materias.size();
    }
    
    public void eliminarMateria(Materia m){
        int pos=0;
        while ( pos < Materias.size() ){
            if (Materias.get(pos).getCod() == m.getCod())
                Materias.remove(pos);
            pos++;
        }
    }

    @Override
    public int compareTo(Nodo o) {
        return getNombre().compareTo(o.getNombre());
    }
    
    public boolean equals (Nodo o){
        return this.compareTo(o)==0;
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
        
    }
    
    public void ordenar(){
        Collections.sort(Materias);
    }
            
}
