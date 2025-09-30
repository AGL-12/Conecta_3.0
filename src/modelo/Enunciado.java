/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanm
 */

public class Enunciado implements Serializable {

    private int id;
    private String descripcion;
    private Dificultad nivel;
    private boolean disponible;
    private String ruta;
    private List<UnidadDidactica> unidadesDidacticas;
    private List<ConvocatoriaExamen> convocatorias;

    public Enunciado() {
        this.unidadesDidacticas = new ArrayList<>();
        this.convocatorias = new ArrayList<>();
        this.disponible = true;
    }

    public Enunciado(String descripcion, Dificultad nivel, String ruta) {
        this();
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.ruta = ruta;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Dificultad getNivel() {
        return nivel;
    }

    public void setNivel(Dificultad nivel) {
        this.nivel = nivel;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<UnidadDidactica> getUnidadesDidacticas() {
        return unidadesDidacticas;
    }

    public void setUnidadesDidacticas(List<UnidadDidactica> unidadesDidacticas) {
        this.unidadesDidacticas = unidadesDidacticas;
    }

    public List<ConvocatoriaExamen> getConvocatorias() {
        return convocatorias;
    }

    public void setConvocatorias(List<ConvocatoriaExamen> convocatorias) {
        this.convocatorias = convocatorias;
    }

    @Override
    public String toString() {
        
        return "Enunciado{"
                + "id=" + id
                + ", descripcion='" + descripcion + '\''
                + ", nivel=" + nivel
                + ", disponible=" + disponible
                + ", ruta='" + ruta + '\''
                + '}';
    }

}
