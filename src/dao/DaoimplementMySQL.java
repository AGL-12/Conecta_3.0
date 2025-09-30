/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.logging.Logger;
import java.util.logging.Level;
import excepciones.DAOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import modelo.ConvocatoriaExamen;
import modelo.Dificultad;
import modelo.Enunciado;
import modelo.UnidadDidactica;

/**
 *
 * @author Alexander
 */
public class DaoimplementMySQL implements Dao {
    private static final Logger LOGGER = Logger.getLogger(DaoimplementMySQL.class.getName());
    // SINGLETON PATTERN
    private static DaoimplementMySQL instance;
    private static final Object lock = new Object();
    // DATABASE CONNECTION
    private Connection con;
    private PreparedStatement stmt;

    // CONFIGURATION
    private ResourceBundle configFile;
    private String urlDB;
    private String userBD;
    private String passwordDB;

    // ARCHIVO PARA CONVOCATORIAS
    private static final String ARCHIVO_CONVOCATORIAS = "././resorces/convocatorias.dat";

    /**
     * Constructor privado - SINGLETON
     */
    private DaoimplementMySQL() {
        try {
            // Cargar configuración
            this.configFile = ResourceBundle.getBundle("config.database");
            this.urlDB = this.configFile.getString("Conn");
            this.userBD = this.configFile.getString("DBUser");
            this.passwordDB = this.configFile.getString("DBPass");

            LOGGER.info("DaoImplementacion Singleton inicializado");

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al cargar configuración, usando valores por defecto", e);
            // Valores por defecto
            this.urlDB = "jdbc:mysql://localhost:3306/examendb";
            this.userBD = "root";
            this.passwordDB = "abcd*1234";
        }
    }

    /**
     * Obtener instancia Singleton
     *
     * @return instancia cargada
     */
    public static DaoimplementMySQL getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DaoimplementMySQL();
                }
            }
        }
        return instance;
    }

    // =================== GESTIÓN DE CONEXIONES ===================
    private void openConnection() throws SQLException {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String connectionUrl = urlDB;
                con = DriverManager.getConnection(connectionUrl, userBD, passwordDB);
                LOGGER.fine("Conexión establecida");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }

    private void closeConnection() throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
    }

    public Connection getConnection() throws SQLException {
        openConnection();
        return con;
    }

    @Override
    public boolean isConnected() {
        try {
            return con != null && !con.isClosed() && con.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    // =================== UNIDAD DIDÁCTICA ===================
    @Override
    public void insertarUnidadDidactica(UnidadDidactica unidad) throws DAOException {
        String sql = "INSERT INTO UnidadDidactica (acronimo, titulo, evaluacion, descripcion) VALUES (?, ?, ?, ?)";

        try {
            openConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, unidad.getAcronimo());
            stmt.setString(2, unidad.getTitulo());
            stmt.setString(3, unidad.getEvaluacion());
            stmt.setString(4, unidad.getDescripcion());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("Error al insertar la unidad didáctica");
            }

            // Obtener ID generado
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                unidad.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al insertar unidad didáctica: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public void actualizarUnidadDidactica(UnidadDidactica unidad) throws DAOException {
        String sql = "UPDATE UnidadDidactica SET acronimo = ?, titulo = ?, evaluacion = ?, descripcion = ? WHERE id = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);

            stmt.setString(1, unidad.getAcronimo());
            stmt.setString(2, unidad.getTitulo());
            stmt.setString(3, unidad.getEvaluacion());
            stmt.setString(4, unidad.getDescripcion());
            stmt.setInt(5, unidad.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("No se encontró la unidad didáctica con ID: " + unidad.getId());
            }

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar unidad didáctica: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public void eliminarUnidadDidactica(int id) throws DAOException {
        String sql = "DELETE FROM UnidadDidactica WHERE id = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("No se encontró la unidad didáctica con ID: " + id);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar unidad didáctica: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public UnidadDidactica buscarUnidadDidacticaPorId(int id) throws DAOException {
        String sql = "SELECT * FROM UnidadDidactica WHERE id = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUnidadDidactica(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error al buscar unidad didáctica por ID: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public UnidadDidactica buscarUnidadDidacticaPorAcronimo(String acronimo) throws DAOException {
        String sql = "SELECT * FROM UnidadDidactica WHERE acronimo = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, acronimo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUnidadDidactica(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error al buscar unidad didáctica por acrónimo: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public List<UnidadDidactica> buscarTodasLasUnidadesDidacticas() throws DAOException {
        String sql = "SELECT * FROM UnidadDidactica ORDER BY acronimo";
        List<UnidadDidactica> unidades = new ArrayList<UnidadDidactica>();

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                unidades.add(mapearUnidadDidactica(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar todas las unidades didácticas: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }

        return unidades;
    }

    // =================== ENUNCIADO ===================
    @Override
    public void insertarEnunciado(Enunciado enunciado) throws DAOException {
        String sql = "INSERT INTO Enunciado (descripcion, nivel_dificultad, disponible, ruta) VALUES (?, ?, ?, ?)";

        try {
            openConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, enunciado.getDescripcion());
            stmt.setString(2, enunciado.getNivel().name());
            stmt.setBoolean(3, enunciado.isDisponible());
            stmt.setString(4, enunciado.getRuta());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("Error al insertar el enunciado");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                enunciado.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al insertar enunciado: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public void actualizarEnunciado(Enunciado enunciado) throws DAOException {
        String sql = "UPDATE Enunciado SET descripcion = ?, nivel_dificultad = ?, disponible = ?, ruta = ? WHERE id = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);

            stmt.setString(1, enunciado.getDescripcion());
            stmt.setString(2, enunciado.getNivel().name());
            stmt.setBoolean(3, enunciado.isDisponible());
            stmt.setString(4, enunciado.getRuta());
            stmt.setInt(5, enunciado.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("No se encontró el enunciado con ID: " + enunciado.getId());
            }

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar enunciado: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public void eliminarEnunciado(int id) throws DAOException {
        String sql = "DELETE FROM Enunciado WHERE id = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("No se encontró el enunciado con ID: " + id);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar enunciado: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public Enunciado buscarEnunciadoPorId(int id) throws DAOException {
        String sql = "SELECT * FROM Enunciado WHERE id = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearEnunciado(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error al buscar enunciado por ID: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public List<Enunciado> buscarTodosLosEnunciados() throws DAOException {
        String sql = "SELECT * FROM Enunciado ORDER BY id";
        List<Enunciado> enunciados = new ArrayList<Enunciado>();

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                enunciados.add(mapearEnunciado(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar todos los enunciados: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }

        return enunciados;
    }

    @Override
    public List<Enunciado> buscarEnunciadosPorUnidadDidactica(int unidadDidacticaId) throws DAOException {
        String sql = "SELECT e.* FROM Enunciado e "
                + "INNER JOIN EnunciadoUnidadDidactica eud ON e.id = eud.enunciado_id "
                + "WHERE eud.unidad_didactica_id = ? ORDER BY e.id";

        List<Enunciado> enunciados = new ArrayList<Enunciado>();

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, unidadDidacticaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                enunciados.add(mapearEnunciado(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar enunciados por unidad didáctica: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }

        return enunciados;
    }

    @Override
    public void asociarEnunciadoConUnidadDidactica(int enunciadoId, int unidadDidacticaId) throws DAOException {
        String sql = "INSERT IGNORE INTO EnunciadoUnidadDidactica (enunciado_id, unidad_didactica_id) VALUES (?, ?)";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, enunciadoId);
            stmt.setInt(2, unidadDidacticaId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al asociar enunciado con unidad didáctica: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public void desasociarEnunciadoDeUnidadDidactica(int enunciadoId, int unidadDidacticaId) throws DAOException {
        String sql = "DELETE FROM EnunciadoUnidadDidactica WHERE enunciado_id = ? AND unidad_didactica_id = ?";

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, enunciadoId);
            stmt.setInt(2, unidadDidacticaId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al desasociar enunciado de unidad didáctica: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
    }

    @Override
    public List<UnidadDidactica> buscarUnidadesDidacticasPorEnunciado(int enunciadoId) throws DAOException {
        String sql = "SELECT ud.* FROM UnidadDidactica ud "
                + "INNER JOIN EnunciadoUnidadDidactica eud ON ud.id = eud.unidad_didactica_id "
                + "WHERE eud.enunciado_id = ? ORDER BY ud.acronimo";

        List<UnidadDidactica> unidades = new ArrayList<UnidadDidactica>();

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, enunciadoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                unidades.add(mapearUnidadDidactica(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar unidades didácticas por enunciado: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }

        return unidades;
    }

    // =================== CONVOCATORIA EXAMEN (ARCHIVO) ===================
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
        List<ConvocatoriaExamen> convocatorias = buscarTodasLasConvocatorias();
        boolean encontrada = false;

        for (int i = 0; i < convocatorias.size(); i++) {
            if (convocatorias.get(i).getConvocatoria().equals(convocatoria.getConvocatoria())) {
                convocatorias.set(i, convocatoria);
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            throw new DAOException("No se encontró la convocatoria: " + convocatoria.getConvocatoria());
        }

        guardarTodasLasConvocatorias(convocatorias);
    }

    @Override
    public void eliminarConvocatoriaExamen(String convocatoria) throws DAOException {
        List<ConvocatoriaExamen> convocatorias = buscarTodasLasConvocatorias();
        boolean eliminada = false;

        for (int i = 0; i < convocatorias.size(); i++) {
            if (convocatorias.get(i).getConvocatoria().equals(convocatoria)) {
                convocatorias.remove(i);
                eliminada = true;
                break;
            }
        }

        if (!eliminada) {
            throw new DAOException("No se encontró la convocatoria: " + convocatoria);
        }

        guardarTodasLasConvocatorias(convocatorias);
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
        File archivo = new File(ARCHIVO_CONVOCATORIAS);
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
        ConvocatoriaExamen conv = buscarConvocatoriaPorNombre(convocatoria);
        if (conv == null) {
            throw new DAOException("No se encontró la convocatoria: " + convocatoria);
        }

        // Verificar si ya está asignado
        for (Enunciado e : conv.getEnunciados()) {
            if (e.getId() == enunciadoId) {
                throw new DAOException("El enunciado ya está asignado a esta convocatoria");
            }
        }

        // Crear enunciado temporal con solo el ID
        Enunciado enunciado = new Enunciado();
        enunciado.setId(enunciadoId);
        conv.getEnunciados().add(enunciado);

        actualizarConvocatoriaExamen(conv);
    }

    @Override
    public void desasignarEnunciadoDeConvocatoria(String convocatoria, int enunciadoId) throws DAOException {
        ConvocatoriaExamen conv = buscarConvocatoriaPorNombre(convocatoria);
        if (conv == null) {
            throw new DAOException("No se encontró la convocatoria: " + convocatoria);
        }

        boolean eliminado = false;
        for (int i = 0; i < conv.getEnunciados().size(); i++) {
            if (conv.getEnunciados().get(i).getId() == enunciadoId) {
                conv.getEnunciados().remove(i);
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new DAOException("El enunciado no estaba asignado a esta convocatoria");
        }

        actualizarConvocatoriaExamen(conv);
    }

    @Override
    public List<ConvocatoriaExamen> buscarConvocatoriasPorEnunciado(int enunciadoId) throws DAOException {
        List<ConvocatoriaExamen> todasConvocatorias = buscarTodasLasConvocatorias();
        List<ConvocatoriaExamen> resultado = new ArrayList<ConvocatoriaExamen>();

        for (ConvocatoriaExamen conv : todasConvocatorias) {
            for (Enunciado e : conv.getEnunciados()) {
                if (e.getId() == enunciadoId) {
                    resultado.add(conv);
                    break;
                }
            }
        }

        return resultado;
    }

    // =================== MÉTODOS AUXILIARES ===================
    private void guardarTodasLasConvocatorias(List<ConvocatoriaExamen> convocatorias) throws DAOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CONVOCATORIAS))) {
            oos.writeObject(convocatorias);
        } catch (IOException e) {
            throw new DAOException("Error al guardar archivo de convocatorias: " + e.getMessage(), e);
        }
    }

    private UnidadDidactica mapearUnidadDidactica(ResultSet rs) throws SQLException {
        UnidadDidactica unidad = new UnidadDidactica();
        unidad.setId(rs.getInt("id"));
        unidad.setAcronimo(rs.getString("acronimo"));
        unidad.setTitulo(rs.getString("titulo"));
        unidad.setEvaluacion(rs.getString("evaluacion"));
        unidad.setDescripcion(rs.getString("descripcion"));
        return unidad;
    }

    private Enunciado mapearEnunciado(ResultSet rs) throws SQLException {
        Enunciado enunciado = new Enunciado();
        enunciado.setId(rs.getInt("id"));
        enunciado.setDescripcion(rs.getString("descripcion"));
        enunciado.setNivel(Dificultad.valueOf(rs.getString("nivel_dificultad")));
        enunciado.setDisponible(rs.getBoolean("disponible"));
        enunciado.setRuta(rs.getString("ruta"));
        return enunciado;
    }

    @Override
    public void probarConexion() throws DAOException {
        try {
            openConnection();
            if (isConnected()) {
                System.out.println("✅ Conexión DAO Singleton establecida correctamente");
                System.out.println("📊 Hash de instancia DAO: " + this.hashCode());
                System.out.println("🔗 URL: " + urlDB);
                System.out.println("👤 Usuario: " + userBD);
            }
            closeConnection();
        } catch (SQLException e) {
            throw new DAOException("Error al probar conexión: " + e.getMessage(), e);
        }
    }

    @Override
    public void cerrarRecursos() throws DAOException {
        try {
            closeConnection();
            LOGGER.info("Recursos DAO cerrados correctamente");
        } catch (SQLException e) {
            throw new DAOException("Error al cerrar recursos: " + e.getMessage(), e);
        }
    }

}
