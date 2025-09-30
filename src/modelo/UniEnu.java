/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author ander
 */
public class UniEnu {

    private int idUni;
    private int idEnu; 

    public UniEnu(int idUnidadDidactica, int idEnunciado) {
        this.idUni = idUnidadDidactica;
        this.idEnu = idEnunciado;
    }

    public int getIdUni() {
        return idUni;
    }

    public void setIdUni(int idUni) {
        this.idUni = idUni;
    }

    public int getIdEnu() {
        return idEnu;
    }

    public void setIdEnu(int idEnu) {
        this.idEnu = idEnu;
    }

   

}
