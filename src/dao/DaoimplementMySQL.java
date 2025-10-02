/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.logging.Logger;
import excepciones.DAOException;
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

    // =================== ENUNCIADO ===================
    @Override
    public void crearUniEnu(int id, int id0) throws DAOException {
        String sql = "INSERT INTO enunciadounidaddidactica (unidad_didactica_id, enunciado_id) VALUES (?, ?)";
        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setInt(2, id0);
            stmt.executeUpdate();
        } catch (Exception e) {
        }
    }

    @Override
    public void crearEnunciado(Enunciado enu) throws DAOException {
        String sql = "INSERT INTO Enunciado (descripcion, nivel_dificultad, disponible, ruta) VALUES (?, ?, ?, ?)";

        try {
            openConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, enu.getDescripcion());
            stmt.setString(2, enu.getNivel().name());
            stmt.setBoolean(3, enu.isDisponible());
            stmt.setString(4, enu.getRuta());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("Error al insertar el enunciado");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                enu.setId(generatedKeys.getInt(1));
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
    public List<Enunciado> buscarEnunciadosPorUnidad(int unidadId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Enunciado> buscarEnunciadosPorUnidadDidactica(int unidadDidacticaId) throws DAOException {
        String sql = "SELECT e.* \n"
                + "FROM Enunciado e\n"
                + "JOIN EnunciadoUnidadDidactica eu ON e.id = eu.enunciado_id\n"
                + "WHERE eu.unidad_didactica_id = ?";

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

        } catch (SQLException e) {
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
        return null;
    }

    @Override
    public List<UnidadDidactica> mostrarUnidades() throws DAOException {
        String sql = "select * from unidaddidactica";
        List<UnidadDidactica> unidades = new ArrayList<>();

        try {
            openConnection();
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                unidades.add(mapearUnidadDidactica(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar enunciados por unidad didáctica: " + e.getMessage(), e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }

        return unidades;
    }

    @Override
    public int ultimoIdEnu() throws DAOException {
        String sql = "SELECT max(id) FROM enunciado;";
        int id = 0;
        try {
            openConnection();
            stmt = con.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {

        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                /* ignorar */ }
        }
        return id;
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

}
