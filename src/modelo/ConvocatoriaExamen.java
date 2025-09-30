/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanm
 */
public class ConvocatoriaExamen implements Serializable {

    private int idEnunciado;
    private String convocatoria;
    private String descripcion;
    private LocalDate fecha;
    private String curso;
    private List<Enunciado> enunciados;

    public ConvocatoriaExamen(String convocatoria, String descripcion, LocalDate fecha, String curso) {
        this.convocatoria = convocatoria;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.curso = curso;
    }

    // Getters y Setters
    public int getIdEnunciado() {
        return idEnunciado;
    }

    public void setIdEnunciado(int idEnunciado) {
        this.idEnunciado = idEnunciado;
    }

    public String getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(String convocatoria) {
        this.convocatoria = convocatoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<Enunciado> getEnunciados() {
        return enunciados;
    }

    public void setEnunciados(List<Enunciado> enunciados) {
        this.enunciados = enunciados;
    }

    @Override
    public String toString() {
        return "ConvocatoriaExamen{"
                + "convocatoria='" + convocatoria + '\''
                + ", descripcion='" + descripcion + '\''
                + ", fecha=" + fecha
                + ", curso='" + curso + '\''
                + '}';
    }

}
