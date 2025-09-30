/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import excepciones.DAOException;
import java.util.List;
import modelo.ConvocatoriaExamen;
import modelo.Enunciado;
import modelo.UnidadDidactica;

/**
 *
 * @author juanm
 */
public interface Dao {

    // =================== OPERACIONES UNIDAD DIDÁCTICA ===================
    /**
     * Caso de uso 1: Crear una unidad didáctica
     *
     * @param unidad La unidad didáctica a insertar
     * @throws DAOException Si ocurre un error en la operación
     */
    void insertarUnidadDidactica(UnidadDidactica unidad) throws DAOException;

    /**
     * Actualizar una unidad didáctica existente
     *
     * @param unidad La unidad didáctica con los nuevos datos
     * @throws DAOException Si no se encuentra la unidad o hay error
     */
    void actualizarUnidadDidactica(UnidadDidactica unidad) throws DAOException;

    /**
     * Eliminar una unidad didáctica por ID
     *
     * @param id ID de la unidad didáctica a eliminar
     * @throws DAOException Si no se encuentra la unidad o hay error
     */
    void eliminarUnidadDidactica(int id) throws DAOException;

    /**
     * Buscar una unidad didáctica por su ID
     *
     * @param id ID de la unidad didáctica
     * @return La unidad didáctica encontrada o null si no existe
     * @throws DAOException Si ocurre un error en la búsqueda
     */
    UnidadDidactica buscarUnidadDidacticaPorId(int id) throws DAOException;

    /**
     * Buscar una unidad didáctica por su acrónimo
     *
     * @param acronimo Acrónimo de la unidad didáctica
     * @return La unidad didáctica encontrada o null si no existe
     * @throws DAOException Si ocurre un error en la búsqueda
     */
    UnidadDidactica buscarUnidadDidacticaPorAcronimo(String acronimo) throws DAOException;

    /**
     * Obtener todas las unidades didácticas
     *
     * @return Lista con todas las unidades didácticas
     * @throws DAOException Si ocurre un error en la consulta
     */
    List<UnidadDidactica> buscarTodasLasUnidadesDidacticas() throws DAOException;

    // =================== OPERACIONES ENUNCIADO ===================
    /**
     * Caso de uso 2: Crear un enunciado de examen
     *
     * @param enunciado El enunciado a insertar
     * @throws DAOException Si ocurre un error en la operación
     */
    void insertarEnunciado(Enunciado enunciado) throws DAOException;

    /**
     * Actualizar un enunciado existente
     *
     * @param enunciado El enunciado con los nuevos datos
     * @throws DAOException Si no se encuentra el enunciado o hay error
     */
    void actualizarEnunciado(Enunciado enunciado) throws DAOException;

    /**
     * Eliminar un enunciado por ID
     *
     * @param id ID del enunciado a eliminar
     * @throws DAOException Si no se encuentra el enunciado o hay error
     */
    void eliminarEnunciado(int id) throws DAOException;

    /**
     * Buscar un enunciado por su ID
     *
     * @param id ID del enunciado
     * @return El enunciado encontrado o null si no existe
     * @throws DAOException Si ocurre un error en la búsqueda
     */
    Enunciado buscarEnunciadoPorId(int id) throws DAOException;

    /**
     * Obtener todos los enunciados
     *
     * @return Lista con todos los enunciados
     * @throws DAOException Si ocurre un error en la consulta
     */
    List<Enunciado> buscarTodosLosEnunciados() throws DAOException;

    /**
     * Caso de uso 3: Consultar los enunciados en los que se trata una unidad
     * didáctica concreta
     *
     * @param unidadDidacticaId ID de la unidad didáctica
     * @return Lista de enunciados que incluyen esa unidad didáctica
     * @throws DAOException Si ocurre un error en la consulta
     */
    List<Enunciado> buscarEnunciadosPorUnidadDidactica(int unidadDidacticaId) throws DAOException;

    /**
     * Caso de uso 2 (parte 2): Asociar un enunciado con una unidad didáctica
     *
     * @param enunciadoId ID del enunciado
     * @param unidadDidacticaId ID de la unidad didáctica
     * @throws DAOException Si ocurre un error en la asociación
     */
    void asociarEnunciadoConUnidadDidactica(int enunciadoId, int unidadDidacticaId) throws DAOException;

    /**
     * Desasociar un enunciado de una unidad didáctica
     *
     * @param enunciadoId ID del enunciado
     * @param unidadDidacticaId ID de la unidad didáctica
     * @throws DAOException Si ocurre un error en la desasociación
     */
    void desasociarEnunciadoDeUnidadDidactica(int enunciadoId, int unidadDidacticaId) throws DAOException;

    /**
     * Obtener todas las unidades didácticas asociadas a un enunciado
     *
     * @param enunciadoId ID del enunciado
     * @return Lista de unidades didácticas asociadas
     * @throws DAOException Si ocurre un error en la consulta
     */
    List<UnidadDidactica> buscarUnidadesDidacticasPorEnunciado(int enunciadoId) throws DAOException;

    // =================== OPERACIONES CONVOCATORIA EXAMEN ===================
    /**
     * Caso de uso 1: Crear una convocatoria de examen
     *
     * @param convocatoria La convocatoria a insertar
     * @throws DAOException Si ocurre un error en la operación
     */
    void insertarConvocatoriaExamen(ConvocatoriaExamen convocatoria) throws DAOException;

    /**
     * Actualizar una convocatoria existente
     *
     * @param convocatoria La convocatoria con los nuevos datos
     * @throws DAOException Si no se encuentra la convocatoria o hay error
     */
    void actualizarConvocatoriaExamen(ConvocatoriaExamen convocatoria) throws DAOException;

    /**
     * Eliminar una convocatoria por nombre
     *
     * @param convocatoria Nombre de la convocatoria a eliminar
     * @throws DAOException Si no se encuentra la convocatoria o hay error
     */
    void eliminarConvocatoriaExamen(String convocatoria) throws DAOException;

    /**
     * Buscar una convocatoria por su nombre
     *
     * @param convocatoria Nombre de la convocatoria
     * @return La convocatoria encontrada o null si no existe
     * @throws DAOException Si ocurre un error en la búsqueda
     */
    ConvocatoriaExamen buscarConvocatoriaPorNombre(String convocatoria) throws DAOException;

    /**
     * Obtener todas las convocatorias
     *
     * @return Lista con todas las convocatorias
     * @throws DAOException Si ocurre un error en la consulta
     */
    List<ConvocatoriaExamen> buscarTodasLasConvocatorias() throws DAOException;

    /**
     * Caso de uso 5: Asignar un enunciado a una convocatoria
     *
     * @param convocatoria Nombre de la convocatoria
     * @param enunciadoId ID del enunciado a asignar
     * @throws DAOException Si ocurre un error en la asignación
     */
    void asignarEnunciadoAConvocatoria(String convocatoria, int enunciadoId) throws DAOException;

    /**
     * Desasignar un enunciado de una convocatoria
     *
     * @param convocatoria Nombre de la convocatoria
     * @param enunciadoId ID del enunciado a desasignar
     * @throws DAOException Si ocurre un error en la desasignación
     */
    void desasignarEnunciadoDeConvocatoria(String convocatoria, int enunciadoId) throws DAOException;

    /**
     * Caso de uso 4: Consultar en qué convocatorias se ha utilizado un
     * enunciado concreto
     *
     * @param enunciadoId ID del enunciado
     * @return Lista de convocatorias que utilizan ese enunciado
     * @throws DAOException Si ocurre un error en la consulta
     */
    List<ConvocatoriaExamen> buscarConvocatoriasPorEnunciado(int enunciadoId) throws DAOException;

    // =================== OPERACIONES DE GESTIÓN ===================
    /**
     * Probar la conexión a la base de datos
     *
     * @throws DAOException Si no se puede establecer conexión
     */
    void probarConexion() throws DAOException;

    /**
     * Cerrar todos los recursos de conexión Importante para el patrón Singleton
     * al finalizar la aplicación
     *
     * @throws DAOException Si ocurre un error al cerrar recursos
     */
    void cerrarRecursos() throws DAOException;

    /**
     * Verificar si hay una conexión activa
     *
     * @return true si hay conexión activa, false en caso contrario
     */
    boolean isConnected();

    /**
     * Obtener estadísticas del DAO (para debugging y monitoreo)
     *
     * @return String con información del estado del DAO
     */
    default String obtenerEstadisticas() {
        return "DAO Singleton - Estado: " + (isConnected() ? "Conectado" : "Desconectado");
    }

    // =================== OPERACIONES TRANSACCIONALES ===================
    /**
     * Iniciar una transacción de base de datos Útil para operaciones que
     * requieren consistencia (ej: crear enunciado + asociaciones)
     *
     * @throws DAOException Si no se puede iniciar la transacción
     */
    default void iniciarTransaccion() throws DAOException {
        // Implementación por defecto vacía
        // Las implementaciones concretas pueden sobrescribir
    }

    /**
     * Confirmar (commit) una transacción
     *
     * @throws DAOException Si no se puede confirmar la transacción
     */
    default void confirmarTransaccion() throws DAOException {
        // Implementación por defecto vacía
    }

    /**
     * Deshacer (rollback) una transacción
     *
     * @throws DAOException Si no se puede deshacer la transacción
     */
    default void deshacerTransaccion() throws DAOException {
        // Implementación por defecto vacía
    }
}
