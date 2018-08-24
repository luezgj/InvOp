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

    public String MOstrarCorrelatividades() {
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
        return nombre.compareTo(o.getNombre());
    }

}
