package controlador;

import dao.Dao;
import dao.DaoimplementMySQL;
import modelo.*;
import excepciones.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import utilidades.Utilidades;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador principal con patrón Singleton Gestiona el menú y coordina DaoDB
 */
public class Controlador {

    // SINGLETON
    private static Controlador instance;
    private static final Object lock = new Object();

    // DAOs
    private final Dao daoDB;      // Para MySQL (UnidadDidactica, Enunciado)

    // Archivo para convocatorias
    private static final String ARCHIVO_CONVOCATORIAS = "src/data/convocatorias.dat";

    /**
     * Constructor privado - Singleton
     */
    private Controlador() {
        this.daoDB = DaoimplementMySQL.getInstance();
        inicializarDatos();
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
     * Inicializar archivo de datos con precarga
     */
    private void inicializarDatos() {
        File archivo = new File(ARCHIVO_CONVOCATORIAS);

        // Crear directorio si no existe
        File directorio = archivo.getParentFile();
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Si no existe el archivo, crear con precarga
        if (!archivo.exists()) {
            System.out.println("Creando archivo convocatorias.dat con datos iniciales...");
            List<ConvocatoriaExamen> precarga = new ArrayList<ConvocatoriaExamen>();

            // Precarga de ejemplo
            precarga.add(new ConvocatoriaExamen("Ordinaria_2024", "Primera convocatoria ordinaria",
                    LocalDate.of(2024, 6, 15), "2023/2024"));
            precarga.add(new ConvocatoriaExamen("Extraordinaria_2024", "Convocatoria extraordinaria",
                    LocalDate.of(2024, 9, 10), "2023/2024"));

            guardarConvocatorias(precarga);
            System.out.println("Archivo creado con " + precarga.size() + " convocatorias de ejemplo.");
        }
    }

    // ========== MÉTODOS PRIVADOS PARA MANEJO DE ARCHIVO ==========
    /**
     * Leer todas las convocatorias del archivo
     */
    private List<ConvocatoriaExamen> leerConvocatorias() {
        File archivo = new File(ARCHIVO_CONVOCATORIAS);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<ConvocatoriaExamen>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<ConvocatoriaExamen>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer convocatorias: " + e.getMessage());
            return new ArrayList<ConvocatoriaExamen>();
        }
    }

    /**
     * Guardar todas las convocatorias en archivo temporal y renombrar
     */
    private void guardarConvocatorias(List<ConvocatoriaExamen> convocatorias) {
        File archivoOriginal = new File(ARCHIVO_CONVOCATORIAS);
        File archivoTemporal = new File(ARCHIVO_CONVOCATORIAS + ".tmp");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoTemporal))) {
            oos.writeObject(convocatorias);

            // Si existe el original, borrarlo
            if (archivoOriginal.exists()) {
                archivoOriginal.delete();
            }

            // Renombrar temporal a original
            archivoTemporal.renameTo(archivoOriginal);

        } catch (IOException e) {
            System.err.println("Error al guardar convocatorias: " + e.getMessage());
            // Si falla, intentar borrar temporal
            if (archivoTemporal.exists()) {
                archivoTemporal.delete();
            }
        }
    }

    /**
     * Buscar convocatoria por nombre
     */
    private ConvocatoriaExamen buscarConvocatoriaPorNombre(String nombre) {
        List<ConvocatoriaExamen> convocatorias = leerConvocatorias();
        for (ConvocatoriaExamen c : convocatorias) {
            if (c.getConvocatoria().equals(nombre)) {
                return c;
            }
        }
        return null;
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

    private void crearEnunciado() {
        System.out.println("\n--- CREAR ENUNCIADO ---");

        try {
            String descripcion = Utilidades.leerString("Descripcion: ");

            System.out.println("Dificultad: 1.BAJA 2.MEDIA 3.ALTA");
            int nivelOpc = Utilidades.leerInt("Opcion: ");
            Dificultad nivel;
            switch (nivelOpc) {
                case 1:
                    nivel = Dificultad.BAJA;
                    break;
                case 2:
                    nivel = Dificultad.MEDIA;
                    break;
                case 3:
                    nivel = Dificultad.ALTA;
                    break;
                default:
                    nivel = Dificultad.MEDIA;
                    break;
            }

            String ruta = Utilidades.leerString("Ruta documento (opcional): ");
            if (ruta.trim().isEmpty()) {
                ruta = null;
            }

            // Mostrar unidades disponibles
            List<UnidadDidactica> unidades = daoDB.obtenerTodasUnidades();
            if (unidades.isEmpty()) {
                System.out.println("No hay unidades didacticas. Cree primero una unidad.");
                return;
            }

            System.out.println("\nUnidades disponibles:");
            for (int i = 0; i < unidades.size(); i++) {
                System.out.println((i + 1) + ". " + unidades.get(i).getAcronimo() + " - " + unidades.get(i).getTitulo());
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

    private void visualizarTextoAsociado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void mostrarEstadoSingleton() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void crearConvocatoria() {
        System.out.println("\n--- CREAR CONVOCATORIA ---");

        try {
            String nombre = Utilidades.leerString("Nombre convocatoria: ");

            // Verificar si ya existe
            if (buscarConvocatoriaPorNombre(nombre) != null) {
                System.err.println("Error: Ya existe una convocatoria con ese nombre.");
                return;
            }

            String descripcion = Utilidades.leerString("Descripcion: ");
            LocalDate fecha = Utilidades.leerFecha("Fecha (yyyy-MM-dd): ");
            String curso = Utilidades.leerString("Curso: ");

            // Leer lista actual
            List<ConvocatoriaExamen> convocatorias = leerConvocatorias();

            // Agregar nueva
            ConvocatoriaExamen nuevaConv = new ConvocatoriaExamen(nombre, descripcion, fecha, curso);
            convocatorias.add(nuevaConv);

            // Guardar con método temporal
            guardarConvocatorias(convocatorias);

            System.out.println("Convocatoria creada exitosamente!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void consultarConvocatoriasPorEnunciado() {
        System.out.println("\n--- CONSULTAR CONVOCATORIAS POR ENUNCIADO ---");

        try {
            int enunciadoId = Utilidades.leerInt("ID del enunciado: ");

            List<ConvocatoriaExamen> todasConvocatorias = leerConvocatorias();
            List<ConvocatoriaExamen> resultado = new ArrayList<ConvocatoriaExamen>();

            // Buscar convocatorias que contienen el enunciado
            for (ConvocatoriaExamen conv : todasConvocatorias) {
                for (Enunciado e : conv.getEnunciados()) {
                    if (e.getId() == enunciadoId) {
                        resultado.add(conv);
                        break;
                    }
                }
            }

            if (resultado.isEmpty()) {
                System.out.println("No hay convocatorias para este enunciado.");
            } else {
                System.out.println("\nConvocatorias encontradas:");
                for (ConvocatoriaExamen c : resultado) {
                    System.out.println("- " + c.getConvocatoria() + " (" + c.getFecha() + ")");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void asignarEnunciadoConvocatoria() {
        System.out.println("\n--- ASIGNAR ENUNCIADO A CONVOCATORIA ---");

        try {
            // Mostrar enunciados disponibles
            List<Enunciado> enunciados = daoDB.obtenerTodosEnunciados();
            if (enunciados.isEmpty()) {
                System.out.println("No hay enunciados disponibles.");
                return;
            }

            System.out.println("\nEnunciados disponibles:");
            for (Enunciado e : enunciados) {
                System.out.println("ID: " + e.getId() + " - [" + e.getNivel() + "] " + e.getDescripcion());
            }

            int enunciadoId = Utilidades.leerInt("\nID del enunciado a asignar: ");

            // Mostrar convocatorias disponibles
            mostrarTodasConvocatorias();

            String nombreConv = Utilidades.leerString("\nNombre de la convocatoria: ");

            // Leer convocatorias y buscar
            List<ConvocatoriaExamen> convocatorias = leerConvocatorias();
            boolean encontrada = false;

            for (ConvocatoriaExamen conv : convocatorias) {
                if (conv.getConvocatoria().equals(nombreConv)) {
                    encontrada = true;

                    // Verificar si ya está asignado
                    boolean yaAsignado = false;
                    for (Enunciado e : conv.getEnunciados()) {
                        if (e.getId() == enunciadoId) {
                            yaAsignado = true;
                            break;
                        }
                    }

                    if (yaAsignado) {
                        System.err.println("El enunciado ya esta asignado a esta convocatoria.");
                        return;
                    }

                    // Crear enunciado temporal con solo el ID
                    Enunciado enunciado = new Enunciado();
                    enunciado.setId(enunciadoId);
                    conv.getEnunciados().add(enunciado);

                    break;
                }
            }

            if (!encontrada) {
                System.err.println("No se encontro la convocatoria.");
                return;
            }

            guardarConvocatorias(convocatorias);
            System.out.println("Enunciado asignado exitosamente!");

        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void mostrarTodasConvocatorias() {
        System.out.println("\n--- TODAS LAS CONVOCATORIAS ---");
        List<ConvocatoriaExamen> convocatorias = leerConvocatorias();

        if (convocatorias.isEmpty()) {
            System.out.println("No hay convocatorias.");
        } else {
            for (int i = 0; i < convocatorias.size(); i++) {
                ConvocatoriaExamen c = convocatorias.get(i);
                System.out.println((i + 1) + ". " + c.getConvocatoria());
                System.out.println("   Fecha: " + c.getFecha());
                System.out.println("   Curso: " + c.getCurso());
                System.out.println("   Enunciados asignados: " + c.getEnunciados().size());
            }
        }
    }
    private void cerrarRecursos() {
        try {
            daoDB.cerrarRecursos();
            System.out.println("Recursos cerrados correctamente.");
        } catch (DAOException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
