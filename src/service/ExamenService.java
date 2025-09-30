/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.Dao;
import dao.DaoimplementMySQL;
import excepciones.DAOException;
import excepciones.EntityNotFoundException;
import excepciones.ExamenException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.ValidationException;
import modelo.ConvocatoriaExamen;
import modelo.Dificultad;
import modelo.Enunciado;
import modelo.UnidadDidactica;


/**
 *
 * @author juanm
 */

public class ExamenService {

    private static final Logger LOGGER = Logger.getLogger(ExamenService.class.getName());

    // SINGLETON PATTERN IMPLEMENTATION
    private static ExamenService instance;
    private static final Object lock = new Object();

    // DEPENDENCIA CON LA CAPA DAO (también Singleton)
    private final Dao dao;

    /**
     * Constructor privado - CLAVE DEL SINGLETON Inicializa la dependencia con
     * el DAO
     */
    private ExamenService() {
        this.dao = DaoimplementMySQL.getInstance(); // DAO Singleton

        LOGGER.info("ExamenService Singleton inicializado");
    }

    /**
     * Método para obtener la única instancia (Thread-Safe) LAZY INITIALIZATION
     * + DOUBLE-CHECKED LOCKING
     */
    public static ExamenService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ExamenService();
                }
            }
        }
        return instance;
    }

    public void crearUnidadDidactica(String acronimo, String titulo, String evaluacion, String descripcion)
            throws ExamenException, ValidationException {

        LOGGER.info("Iniciando creación de unidad didáctica: " + acronimo);

        // VALIDACIONES DE NEGOCIO
        if (acronimo == null || acronimo.trim().isEmpty()) {
            throw new ValidationException("El acrónimo es obligatorio");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidationException("El título es obligatorio");
        }

        // NORMALIZACIÓN
        acronimo = acronimo.trim().toUpperCase();
        titulo = titulo.trim();

        // VERIFICAR UNICIDAD DEL ACRÓNIMO
        UnidadDidactica existente = null;
        try {
            existente = dao.buscarUnidadDidacticaPorAcronimo(acronimo);
        } catch (DAOException ex) {
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (existente != null) {
            throw new ValidationException("Ya existe una unidad didáctica con el acrónimo: " + acronimo);
        }

        // CREAR Y PERSISTIR
        UnidadDidactica unidad = new UnidadDidactica(acronimo, titulo, evaluacion, descripcion);
        try {
            dao.insertarUnidadDidactica(unidad);
        } catch (DAOException ex) {
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOGGER.info("Unidad didáctica creada exitosamente: " + acronimo + " - " + titulo);
    }

    public void crearEnunciado(String descripcion, Dificultad nivel, String ruta,
            List<Integer> unidadesDidacticasIds, String convocatoria)
            throws ExamenException, ValidationException {

        LOGGER.info("Iniciando creación de enunciado: " + descripcion.substring(0, Math.min(50, descripcion.length())));

        // VALIDACIONES DE NEGOCIO
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new ValidationException("La descripción es obligatoria");
        }
        if (nivel == null) {
            throw new ValidationException("El nivel de dificultad es obligatorio");
        }
        if (unidadesDidacticasIds == null || unidadesDidacticasIds.isEmpty()) {
            throw new ValidationException("Debe asociar al menos una unidad didáctica");
        }

        descripcion = descripcion.trim();

        // VERIFICAR QUE TODAS LAS UNIDADES DIDÁCTICAS EXISTAN
        for (Integer unidadId : unidadesDidacticasIds) {
            UnidadDidactica unidad = null;
            try {
                unidad = dao.buscarUnidadDidacticaPorId(unidadId);
            } catch (DAOException ex) {
                Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (unidad == null) {
                throw new EntityNotFoundException("UnidadDidactica", unidadId);
            }
        }

        // VERIFICAR QUE LA CONVOCATORIA EXISTA (SI SE PROPORCIONA)
        if (convocatoria != null && !convocatoria.trim().isEmpty()) {
            ConvocatoriaExamen conv = null;
            try {
                conv = dao.buscarConvocatoriaPorNombre(convocatoria.trim());
            } catch (DAOException ex) {
                Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (conv == null) {
                throw new EntityNotFoundException("ConvocatoriaExamen", convocatoria);
            }
        }

        // OPERACIÓN COMPLEJA: REQUIERE MÚLTIPLES OPERACIONES DAO
        try {
        // 1. Crear el enunciado
        Enunciado enunciado = new Enunciado(descripcion, nivel, ruta);
        dao.insertarEnunciado(enunciado);

        // 2. Asociar con las unidades didácticas
        for (Integer unidadId : unidadesDidacticasIds) {
            dao.asociarEnunciadoConUnidadDidactica(enunciado.getId(), unidadId);
        }

        // 3. Asociar con la convocatoria si se proporciona
        if (convocatoria != null && !convocatoria.trim().isEmpty()) {
            dao.asignarEnunciadoAConvocatoria(convocatoria.trim(), enunciado.getId());
        }

        LOGGER.info("Enunciado creado y asociado exitosamente: ID " + enunciado.getId());

        } catch (DAOException e) {
            LOGGER.log(Level.SEVERE, "Error en operación compleja de crear enunciado", e);
            throw new ExamenException("Error al crear enunciado: " + e.getMessage(), e);
    }
    }

    public void asignarEnunciadoAConvocatoria(int enunciadoId, String convocatoria)
            throws ExamenException, ValidationException {

        LOGGER.info("Asignando enunciado " + enunciadoId + " a convocatoria " + convocatoria);

        // VERIFICAR QUE EL ENUNCIADO EXISTA
        Enunciado enunciado = null;
        try {
            enunciado = dao.buscarEnunciadoPorId(enunciadoId);
        } catch (DAOException ex) {
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (enunciado == null) {
            throw new EntityNotFoundException("Enunciado", enunciadoId);
        }

        // VERIFICAR QUE LA CONVOCATORIA EXISTA
        ConvocatoriaExamen conv = null;
        try {
            conv = dao.buscarConvocatoriaPorNombre(convocatoria);
        } catch (DAOException ex) {
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (conv == null) {
            throw new EntityNotFoundException("ConvocatoriaExamen", convocatoria);
        }

        // VERIFICAR REGLAS DE NEGOCIO
        if (!enunciado.isDisponible()) {
            throw new ValidationException("El enunciado no está disponible para asignación");
        }

        try {
            // REALIZAR LA ASIGNACIÓN
            dao.asignarEnunciadoAConvocatoria(convocatoria, enunciadoId);
        } catch (DAOException ex) {
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOGGER.info("Enunciado " + enunciadoId + " asignado exitosamente a convocatoria " + convocatoria);
    }

    /**
     * Reset de la instancia Singleton (solo para testing)
     */
    public static void resetInstance() {
        synchronized (lock) {
            if (instance != null) {
                instance.cerrarRecursos();
                instance = null;
            }
        }
    }

    private void cerrarRecursos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
