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
//throws DAOException;

    public void crearUniEnu(int id, int id0) throws DAOException;

    public void crearEnunciado(Enunciado enu) throws DAOException;

    public List<UnidadDidactica> mostrarUnidades() throws DAOException;

    public List<Enunciado> buscarEnunciadosPorUnidad(int unidadId) throws DAOException;

    public void insertarUnidadDidactica(UnidadDidactica unidad) throws DAOException;

    public List<Enunciado> buscarEnunciadosPorUnidadDidactica(int id) throws DAOException;

    public Enunciado buscarEnunciadoPorId(int id) throws DAOException;

    public int ultimoIdEnu()throws DAOException;

    public List<Enunciado> mostrarEnunciados() throws DAOException;
}
