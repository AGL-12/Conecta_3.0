/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import excepciones.DAOException;
import java.util.List;
import modelo.Enunciado;
import modelo.UnidadDidactica;

/**
 *
 * @author juanm
 */
public interface Dao {
    public List<Enunciado> obtenerTodosEnunciados()throws DAOException;

    public List<Enunciado> buscarEnunciadosPorUnidad(int unidadId)throws DAOException;

    public void asociarEnunciadoUnidad(int id, Integer unidadId)throws DAOException;

    public void insertarEnunciado(Enunciado enunciado)throws DAOException;

    public List<UnidadDidactica> obtenerTodasUnidades()throws DAOException;

    public void insertarUnidadDidactica(UnidadDidactica unidad)throws DAOException;

    public void cerrarRecursos()throws DAOException;
}
