package controlador;

import dao.Dao;
import dao.DaoimplementMySQL;
import dao.DaoImplementsFile;
import modelo.*;
import excepciones.*;
import utilidades.Utilidades;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador principal con patrón Singleton
 * Gestiona el menú y coordina DaoDB y DaoFile
 */
public class Controlador {
    
    // SINGLETON
    private static Controlador instance;
    private static final Object lock = new Object();
    
    // DAOs
    private final Dao daoDB;      // Para MySQL (UnidadDidactica, Enunciado)
    private final Dao daoFile;    // Para archivo (ConvocatoriaExamen)
    
    /**
     * Constructor privado - Singleton
     */
    private Controlador() {
        this.daoDB = DaoimplementMySQL.getInstance();
        this.daoFile = DaoImplementsFile.getInstance();
    }
    
    /**
     * Obtener instancia Singleton
     */
    public static Controlador getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Controlador();
                }
            }
        }
        return instance;
    }
    
    /**
     * Iniciar aplicación
     */
    public void iniciar() {
        int opcion;
        
        do {
            mostrarMenu();
            opcion = Utilidades.leerInt("🔹 Escoge la opción deseada: ");
            
            switch (opcion) {
                case 1:
                    crearUnidadDidactica();
                    break;
                case 2:
                    crearConvocatoria();
                    break;
                case 3:
                    crearEnunciado();
                    break;
                case 4:
                    consultarEnunciadosPorUnidad();
                    break;
                case 5:
                    consultarConvocatoriasPorEnunciado();
                    break;
                case 6:
                    visualizarTextoAsociado();
                    break;
                case 7:
                    asignarEnunciadoConvocatoria();
                    break;
                case 8:
                    mostrarEstadoSingleton();
                    break;
                case 0:
                    System.out.println("👋 Saliendo del programa...");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Seleccione una opción válida.");
                    break;
            }
            
        } while (opcion != 0);
        
        cerrarRecursos();
    }
    
    private void mostrarMenu() {
        System.out.println("\n" + Utilidades.repetir("=", 50));
        System.out.println("          📚 MENU PRINCIPAL 📚");
        System.out.println(Utilidades.repetir("=", 50));
        System.out.println("1. 🏗️ Crear Unidad Didactica");
        System.out.println("2. 🏗️ Crear Convocatoria");
        System.out.println("3. 📝 Crear Enunciado");
        System.out.println("4. 🔍 Consultar Enunciados por Unidad");
        System.out.println("5. 📅  Consultar Convocatorias por Enunciado");
        System.out.println("6. 👁️  Visualizar texto asociado a un Enunciado");
        System.out.println("7. ➡️ Asignar Enunciado a Convocatoria");
        System.out.println("8. 🔧 Mostrar Estado Singleton (Demo)");
        System.out.println("0. Salir");
        System.out.println(Utilidades.repetir("=", 50));
    }
    
    private void crearUnidadDidactica() {
        System.out.println("\n--- CREAR UNIDAD DIDACTICA ---");
        
        try {
            String acronimo = Utilidades.leerString("Acronimo: ");
            String titulo = Utilidades.leerString("Titulo: ");
            String evaluacion = Utilidades.leerString("Evaluacion (Continua/Final/Mixta): ");
            String descripcion = Utilidades.leerString("Descripcion: ");
            
            UnidadDidactica unidad = new UnidadDidactica(acronimo, titulo, evaluacion, descripcion);
            daoDB.insertarUnidadDidactica(unidad);
            
            System.out.println("Unidad didactica creada exitosamente!");
            
        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void crearConvocatoria() {
        System.out.println("\n--- CREAR CONVOCATORIA ---");
        
        try {
            String nombre = Utilidades.leerString("Nombre convocatoria: ");
            String descripcion = Utilidades.leerString("Descripcion: ");
            LocalDate fecha = Utilidades.leerFecha("Fecha (yyyy-MM-dd): ");
            String curso = Utilidades.leerString("Curso: ");
            
            ConvocatoriaExamen convocatoria = new ConvocatoriaExamen(nombre, descripcion, fecha, curso);
            daoFile.insertarConvocatoria(convocatoria);
            
            System.out.println("Convocatoria creada exitosamente!");
            
        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void crearEnunciado() {
        System.out.println("\n--- CREAR ENUNCIADO ---");
        
        try {
            String descripcion = Utilidades.leerString("Descripcion: ");
            
            System.out.println("Dificultad: 1.BAJA 2.MEDIA 3.ALTA");
            int nivelOpc = Utilidades.leerInt("Opcion: ");
            Dificultad nivel;
            switch (nivelOpc) {
                case 1: nivel = Dificultad.BAJA; break;
                case 2: nivel = Dificultad.MEDIA; break;
                case 3: nivel = Dificultad.ALTA; break;
                default: nivel = Dificultad.MEDIA; break;
            }
            
            String ruta = Utilidades.leerString("Ruta documento (opcional): ");
            if (ruta.trim().isEmpty()) ruta = null;
            
            // Mostrar unidades disponibles
            List<UnidadDidactica> unidades = daoDB.obtenerTodasUnidades();
            if (unidades.isEmpty()) {
                System.out.println("No hay unidades didacticas. Cree primero una unidad.");
                return;
            }
            
            System.out.println("\nUnidades disponibles:");
            for (int i = 0; i < unidades.size(); i++) {
                System.out.println((i+1) + ". " + unidades.get(i).getAcronimo() + " - " + unidades.get(i).getTitulo());
            }
            
            String seleccion = Utilidades.leerString("Seleccione unidades (ej: 1,3): ");
            List<Integer> unidadesIds = new ArrayList<Integer>();
            
            String[] indices = seleccion.split(",");
            for (String idx : indices) {
                try {
                    int i = Integer.parseInt(idx.trim()) - 1;
                    if (i >= 0 && i < unidades.size()) {
                        unidadesIds.add(unidades.get(i).getId());
                    }
                } catch (NumberFormatException e) {
                    // ignorar indices invalidos
                }
            }
            
            if (unidadesIds.isEmpty()) {
                System.out.println("Debe seleccionar al menos una unidad.");
                return;
            }
            
            // Crear enunciado
            Enunciado enunciado = new Enunciado(descripcion, nivel, ruta);
            daoDB.insertarEnunciado(enunciado);
            
            // Asociar con unidades
            for (Integer unidadId : unidadesIds) {
                daoDB.asociarEnunciadoUnidad(enunciado.getId(), unidadId);
            }
            
            System.out.println("Enunciado creado exitosamente!");
            
        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void consultarEnunciadosPorUnidad() {
        System.out.println("\n--- CONSULTAR ENUNCIADOS POR UNIDAD ---");
        
        try {
            int unidadId = Utilidades.leerInt("ID de la unidad: ");
            
            List<Enunciado> enunciados = daoDB.buscarEnunciadosPorUnidad(unidadId);
            
            if (enunciados.isEmpty()) {
                System.out.println("No hay enunciados para esta unidad.");
            } else {
                System.out.println("\nEnunciados encontrados:");
                for (Enunciado e : enunciados) {
                    System.out.println("- [" + e.getNivel() + "] " + e.getDescripcion());
                }
            }
            
        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void consultarConvocatoriasPorEnunciado() {
        System.out.println("\n--- CONSULTAR CONVOCATORIAS POR ENUNCIADO ---");
        
        try {
            int enunciadoId = Utilidades.leerInt("ID del enunciado: ");
            
            List<ConvocatoriaExamen> convocatorias = daoFile.buscarConvocatoriasPorEnunciado(enunciadoId);
            
            if (convocatorias.isEmpty()) {
                System.out.println("No hay convocatorias para este enunciado.");
            } else {
                System.out.println("\nConvocatorias encontradas:");
                for (ConvocatoriaExamen c : convocatorias) {
                    System.out.println("- " + c.getConvocatoria() + " (" + c.getFecha() + ")");
                }
            }
            
        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void asignarEnunciadoConvocatoria() {
        System.out.println("\n--- ASIGNAR ENUNCIADO A CONVOCATORIA ---");
        
        try {
            int enunciadoId = Utilidades.leerInt("ID del enunciado: ");
            String convocatoria = Utilidades.leerString("Nombre convocatoria: ");
            
            daoFile.asignarEnunciadoConvocatoria(convocatoria, enunciadoId);
            
            System.out.println("Enunciado asignado exitosamente!");
            
        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    private void visualizarTextoAsociado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void mostrarEstadoSingleton() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void cerrarRecursos() {
        try {
            daoDB.cerrarRecursos();
            daoFile.cerrarRecursos();
            System.out.println("Recursos cerrados correctamente.");
        } catch (DAOException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }

    
}