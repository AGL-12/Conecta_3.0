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
    public void cerrarRecursos() throws DAOException {
        try {
            closeConnection();
            LOGGER.info("Recursos DAO cerrados correctamente");
        } catch (SQLException e) {
            throw new DAOException("Error al cerrar recursos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Enunciado> obtenerTodosEnunciados() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Enunciado> buscarEnunciadosPorUnidad(int unidadId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void asociarEnunciadoUnidad(int id, Integer unidadId) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UnidadDidactica> obtenerTodasUnidades() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
