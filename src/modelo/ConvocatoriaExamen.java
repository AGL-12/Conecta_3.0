/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.time.LocalDate;

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

    public ConvocatoriaExamen(int idEnunciado, String convocatoria, String descripcion, LocalDate fecha, String curso) {
        this.idEnunciado = idEnunciado;
        this.convocatoria = convocatoria;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.curso = curso;
    }

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

    @Override
    public String toString() {
        return "Convocatoria: " + convocatoria + "\n"
                + "Descripción : " + descripcion + "\n"
                + "Fecha       : " + fecha + "\n"
                + "Curso       : " + curso + "\n"
                + "ID Enunciado: " + (idEnunciado > 0 ? idEnunciado : "No asignado") + "\n"
                + "----------------------------";
    }

}
