package tpinvop;

import java.util.List;

/**
 *
 * @author lucho
 */
public class Cohorte {
    List<Cadena> cadenas;
    int año;

    public Cohorte(List<Cadena> cadenas, int año) {
        this.cadenas = cadenas;
        this.año = año;
    }

    public List<Cadena> getCadenas() {
        return cadenas;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }
    
}