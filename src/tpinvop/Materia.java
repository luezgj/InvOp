package tpinvop;

import java.util.List;

public class Materia implements Comparable<Materia> {
    int cod;
    String nombre;
    List<Integer> correlatividades;
    public Materia(int cod,String nombre,List<Integer> cs){
        this.cod=cod;
        this.nombre=nombre;
        correlatividades=cs;
    }

    public int getCod() {
        return cod;
    }
    
    // Generalizar 
    public int getAnio() {
        return ( (Integer) cod ).toString().charAt(1);
    }
    
    public int getCuatrimestre() {
        return ( (Integer) cod ).toString().charAt(2);        
    }

    public String getNombre() {
        return nombre;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public boolean EsCorrelativa(Integer cod){
        return (correlatividades.contains(cod));
    }
    
    public boolean tieneCorrelativas(){
        return !(correlatividades.isEmpty());
    }

    public String mostrarCorrelatividades() {
        String salida="";
        for(Integer i: correlatividades){
            salida+=Integer.toString(i);
        }
        return salida;
    }

    public List<Integer> getCorrelatividades() {
        return correlatividades;
    }
    
    public boolean equals(Materia m){
        return cod == m.getCod();
    }
    
   public boolean mismasCorrelativas(Materia m){
       for (Integer cod:m.getCorrelatividades())
           if (!EsCorrelativa(cod))
               return false;
       return true;
   }

    @Override
    public int compareTo(Materia o) {
        
        if(this.getAnio() < o.getAnio())
            return 1;
        else if(this.getAnio() > o.getAnio())
            return -1;
        else
            if(this.getCuatrimestre() < o.getCuatrimestre())
                return 1;
            else if(this.getCuatrimestre() > o.getCuatrimestre())
                return -1;
            else
                return 0;
    }

}
