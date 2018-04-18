package InvOp;

import java.util.ArrayList;
import java.util.List;


public class Linea {
    int id;
    List<List<Materia>> Materias;
    //List<Nodo> nodos;
    
    public Linea(int id){
        this.id=id;
        Materias = new ArrayList<>();
    }
    
    public Linea(int id,List<List<Materia>> Materias){
        this.id=id;
        this.Materias = Materias;
    } 
    
    public void addNodo(List<Materia> n){
        Materias.add(n);
    };
    
    public void addMateria(Materia m){
        List<Materia> lm=new ArrayList<>();
        lm.add(m);
        Materias.add(lm);
    }
    
    public int getCantMaterias(){
        return Materias.size();
    }
    
    public Linea clone(){
        Linea salida=new Linea(this.id);
        List<List<Materia>> m=new ArrayList<>();
        for(List<Materia> lm : Materias){
            m.add(lm);
        }
        salida.setMaterias(m);
        return salida;
    }
    
    public boolean contiene(Materia m){
        for(List<Materia> lm : Materias)
            for(Materia mat:lm)
                if (mat.equals(m)){
                   return true;
            }
        return false;
    }

    public int getId() {
        return id;
    }
    
    public void MostrarNodos(){
        int i=1;
        for(List<Materia> lm : Materias){
            System.out.print("Nodo "+i+":  ");
            for(Materia m:lm){
                System.out.print(m.getCod()+" ");
            }
            i++;
        }
    }

    public void setMaterias(List<List<Materia>> Materias) {
        this.Materias = Materias;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void eliminarUltimoNodo(){
        Materias.remove(Materias.size()-1);
    }
    
}
