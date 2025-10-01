/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import excepciones.DAOException;
import excepciones.ValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.ConvocatoriaExamen;
import modelo.Enunciado;
import modelo.UnidadDidactica;

/**
 *
 * @author precu
 */
public class DaoImplementsFile implements Dao {

    @Override
    public void insertarUnidadDidactica(UnidadDidactica unidad) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actualizarUnidadDidactica(UnidadDidactica unidad) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminarUnidadDidactica(int id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UnidadDidactica buscarUnidadDidacticaPorId(int id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UnidadDidactica buscarUnidadDidacticaPorAcronimo(String acronimo) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UnidadDidactica> buscarTodasLasUnidadesDidacticas() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertarEnunciado(Enunciado enunciado) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actualizarEnunciado(Enunciado enunciado) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminarEnunciado(int id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enunciado buscarEnunciadoPorId(int id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Enunciado> buscarTodosLosEnunciados() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Enunciado> buscarEnunciadosPorUnidadDidactica(int unidadDidacticaId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void asociarEnunciadoConUnidadDidactica(int enunciadoId, int unidadDidacticaId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void desasociarEnunciadoDeUnidadDidactica(int enunciadoId, int unidadDidacticaId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UnidadDidactica> buscarUnidadesDidacticasPorEnunciado(int enunciadoId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertarConvocatoriaExamen(ConvocatoriaExamen convocatoria) throws DAOException {
        List<ConvocatoriaExamen> convocatorias = buscarTodasLasConvocatorias();

        // Verificar si ya existe
        for (ConvocatoriaExamen c : convocatorias) {
            if (c.getConvocatoria().equals(convocatoria.getConvocatoria())) {
                throw new DAOException("Ya existe una convocatoria con el nombre: " + convocatoria.getConvocatoria());
            }
        }

        convocatorias.add(convocatoria);
        guardarTodasLasConvocatorias(convocatorias);

    }

    @Override
    public void actualizarConvocatoriaExamen(ConvocatoriaExamen convocatoria) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminarConvocatoriaExamen(String convocatoria) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConvocatoriaExamen buscarConvocatoriaPorNombre(String convocatoria) throws DAOException {
        List<ConvocatoriaExamen> convocatorias = buscarTodasLasConvocatorias();
        for (ConvocatoriaExamen c : convocatorias) {
            if (c.getConvocatoria().equals(convocatoria)) {
                return c;
            }
        }
        return null;

    }

    @Override
    public List<ConvocatoriaExamen> buscarTodasLasConvocatorias() throws DAOException {
        File archivo = new File("convocatorias.dat");
        if (!archivo.exists()) {
            return new ArrayList<ConvocatoriaExamen>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            @SuppressWarnings("unchecked")
            List<ConvocatoriaExamen> convocatorias = (List<ConvocatoriaExamen>) ois.readObject();
            return convocatorias;
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Error al leer archivo de convocatorias: " + e.getMessage(), e);
        }
    }

    @Override
    public void asignarEnunciadoAConvocatoria(String convocatoria, int enunciadoId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void desasignarEnunciadoDeConvocatoria(String convocatoria, int enunciadoId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ConvocatoriaExamen> buscarConvocatoriasPorEnunciado(int enunciadoId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void probarConexion() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cerrarRecursos() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void crearConvocatoriaExamen(String convocatoria, String descripcion, LocalDate fecha, String curso) {

        LOGGER.info("Iniciando creación de convocatoria: " + convocatoria);

        // VALIDACIONES DE NEGOCIO
        if (convocatoria == null || convocatoria.trim().isEmpty()) {
            try {
                throw new ValidationException("El nombre de la convocatoria es obligatorio");
            } catch (ValidationException ex) {
                Logger.getLogger(DaoImplementsFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (fecha == null) {
            try {
                throw new ValidationException("La fecha es obligatoria");
            } catch (ValidationException ex) {
                Logger.getLogger(DaoImplementsFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (curso == null || curso.trim().isEmpty()) {
            try {
                throw new ValidationException("El curso es obligatorio");
            } catch (ValidationException ex) {
                Logger.getLogger(DaoImplementsFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // VALIDACIÓN DE FECHA (regla de negocio)
        if (fecha.isBefore(LocalDate.now())) {
            try {
                throw new ValidationException("La fecha de la convocatoria no puede ser anterior a hoy");
            } catch (ValidationException ex) {
                Logger.getLogger(DaoImplementsFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // NORMALIZACIÓN
        convocatoria = convocatoria.trim();
        curso = curso.trim();

        // VERIFICAR UNICIDAD DEL NOMBRE
        ConvocatoriaExamen existente = null;
        try {
            existente = buscarConvocatoriaPorNombre(convocatoria);
        } catch (DAOException ex) {
            Logger.getLogger(DaoImplementsFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (existente != null) {
            try {
                throw new ValidationException("Ya existe una convocatoria con el nombre: " + convocatoria);
            } catch (ValidationException ex) {
                Logger.getLogger(DaoImplementsFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // CREAR Y PERSISTIR
        ConvocatoriaExamen conv = new ConvocatoriaExamen(convocatoria, descripcion, fecha, curso);
        try {
            insertarConvocatoriaExamen(conv);
        } catch (DAOException ex) {
            Logger.getLogger(DaoImplementsFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOGGER.info("Convocatoria creada exitosamente: " + convocatoria + " - " + fecha);
    }

    private void guardarTodasLasConvocatorias(List<ConvocatoriaExamen> convocatorias) throws DAOException {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("convocatorias.dat"))) {
            oos.writeObject(convocatorias);
        } catch (IOException e) {
            throw new DAOException("Error al guardar archivo de convocatorias: " + e.getMessage(), e);
        }
    }

}
