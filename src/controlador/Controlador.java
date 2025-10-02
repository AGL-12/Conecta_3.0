package controlador;

import dao.Dao;
import dao.DaoimplementMySQL;
import modelo.*;
import excepciones.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import utilidades.Utilidades;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador principal con patrón Singleton Gestiona el menú y coordina DaoDB
 */
public class Controlador {

    // SINGLETON
    private static Controlador instance;

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
            instance = new Controlador();
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
            //enunciado
            // Precarga de ejemplo
            precarga.add(new ConvocatoriaExamen(101, "Ordinaria_2024", "Primera convocatoria ordinaria",
                    LocalDate.of(2024, 6, 15), "2023/2024"));
            precarga.add(new ConvocatoriaExamen(102, "Extraordinaria_2024", "Convocatoria extraordinaria",
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

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoOriginal))) {
            oos.writeObject(convocatorias);
        } catch (IOException e) {
            System.err.println("Error al guardar convocatorias: " + e.getMessage());
        }
    }

    /**
     * Buscar convocatoria por nombre
     */
    private ConvocatoriaExamen buscarConvocatoriaPorNombre(String nombre) {
        List<ConvocatoriaExamen> convocatorias = leerConvocatorias();
        for (ConvocatoriaExamen c : convocatorias) {
            if (c.getConvocatoria().equalsIgnoreCase(nombre)) {
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
        System.out.println("0. Salir");
        System.out.println(Utilidades.repetir("=", 50));
    }

    private void crearUnidadDidactica() {
        int option;
        String evaluacion = null;
        System.out.println("\n--- CREAR UNIDAD DIDACTICA ---");

        try {
            String acronimo = Utilidades.leerString("Acronimo: ");
            String titulo = Utilidades.leerString("Titulo: ");
            do {
                switch (option = Utilidades.leerInt("Elige una Evaluacion (1.Continua/2.Final/3.Mixta) ")) {
                    case 1:
                        evaluacion = "continua";
                        break;
                    case 2:
                        evaluacion = "Final";
                        break;
                    case 3:
                        evaluacion = "Mixta";
                        break;
                    default:
                        System.out.println("⚠️ Opción inválida.");
                        break;
                }
            } while (option < 1 || option > 3);

            String descripcion = Utilidades.leerString("Descripcion: ");

            UnidadDidactica unidad = new UnidadDidactica(acronimo, titulo, evaluacion, descripcion);
            daoDB.insertarUnidadDidactica(unidad);

            System.out.println("Unidad didactica creada exitosamente!");

        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void crearEnunciado() {
        System.out.println("\n--- 📝 CREAR ENUNCIADO  ---");

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
                    System.out.println("⚠️ Opción inválida, se seleccionará MEDIA por defecto");
                    nivel = Dificultad.MEDIA;
                    break;
            }

            String ruta = Utilidades.leerString("Ruta documento: ");
            if (ruta.trim().isEmpty()) {
                ruta = null;
            }

            // Crear enunciado
            Enunciado enu = new Enunciado(descripcion, nivel, ruta);
            daoDB.crearEnunciado(enu);

            // Asociar con unidades
            List<UnidadDidactica> uni = daoDB.mostrarUnidades();
            System.out.println("\n📚 Unidades Didácticas disponibles:");

            int id = selectIdUnidadDidactica(uni);
            int ultimoId = daoDB.ultimoIdEnu();
            daoDB.crearUniEnu(id, ultimoId);
            // asociar enunciado a convocatoria
            mostrarTodasConvocatorias();
            List<ConvocatoriaExamen> convocatorias = leerConvocatorias();
            ConvocatoriaExamen convocatoriaSeleccionada = null;
            while (convocatoriaSeleccionada == null) {
                String nombreCon = Utilidades.introducirCadena("Introduce el nombre de la convocatoria que desea: ");

                for (ConvocatoriaExamen conv : convocatorias) {
                    if (conv.getConvocatoria().equalsIgnoreCase(nombreCon)) {
                        convocatoriaSeleccionada = conv;
                        break;
                    }
                }

                if (convocatoriaSeleccionada == null) {
                    System.out.println("❌ No existe ninguna convocatoria con ese nombre. Intenta de nuevo.");
                }
            }
            guardarConvocatorias(convocatorias);

            System.out.println("Enunciado creado exitosamente!");

        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void consultarEnunciadosPorUnidad() {
        System.out.println("\n--- CONSULTAR ENUNCIADOS POR UNIDAD ---");
        List<Enunciado> enunciados;

        try {
            List<UnidadDidactica> uni = daoDB.mostrarUnidades();
            int unidadId = selectIdUnidadDidactica(uni);
            enunciados = daoDB.buscarEnunciadosPorUnidadDidactica(unidadId);
            if (!enunciados.isEmpty() && enunciados != null) {
                for (Enunciado e : enunciados) {
                    System.out.println(e);
                }
            } else {
                System.out.println("No hay enunciado asignados a la convocatoria: " + unidadId);
            }

        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
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

    private void mostrarTodasConvocatorias() {
        System.out.println("\n--- TODAS LAS CONVOCATORIAS ---");
        List<ConvocatoriaExamen> convocatorias = leerConvocatorias();

        if (convocatorias.isEmpty()) {
            System.out.println("No hay convocatorias.");
        } else {
            for (int i = 0; i < convocatorias.size(); i++) {
                ConvocatoriaExamen c = convocatorias.get(i);
                //System.out.println((i + 1) + ". " + convocatorias.get(i));
                System.out.println((i + 1) + ". " + convocatorias.get(i).getConvocatoria());
            }
        }
    }

    private void visualizarTextoAsociado() {
        int id = Utilidades.leerInt("Introduce el ID del enunciado a visualizar: ");

        try {
            Enunciado enunciado = daoDB.buscarEnunciadoPorId(id);
            if (enunciado == null) {
                System.out.println("❌ No se encontró ningún enunciado con ese ID.");
                return;
            }

            String rutaArchivo = enunciado.getRuta();

            // Validar que hay ruta
            if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
                System.out.println("❌ Este enunciado no tiene archivo asociado.");
                return;
            }

            File archivo = new File(rutaArchivo);

            if (!archivo.exists()) {
                System.out.println("❌ El archivo no existe: " + rutaArchivo);
                return;
            }

            // Validar Desktop
            if (!Desktop.isDesktopSupported()) {
                System.out.println("❌ La apertura de archivos no está soportada en este sistema.");
                System.out.println("   Ruta del archivo: " + archivo.getAbsolutePath());
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            desktop.open(archivo);
            System.out.println("✅ Abriendo archivo: " + archivo.getName());

        } catch (DAOException e) {
            System.out.println("❌ Error al buscar el enunciado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ Error al abrir el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void asignarEnunciadoConvocatoria() {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        String nombre;
        int id = 0;
        File fichero = new File(ARCHIVO_CONVOCATORIAS);
        List<ConvocatoriaExamen> convocatorias = new ArrayList<>();
        List<Enunciado> listaEnunciados;
        ConvocatoriaExamen encontrada = null;
        ConvocatoriaExamen busca = null;

        mostrarTodasConvocatorias();
        do {
            nombre = Utilidades.introducirCadena("Introduce el nombre de la convocatoria: ");
            encontrada = buscarConvocatoriaPorNombre(nombre);
            if (encontrada == null) {
                System.out.println("⚠️ Convocatoria no encontrada, inténtelo de nuevo.");
            }
        } while (encontrada == null);
        try {
            listaEnunciados = daoDB.mostrarEnunciados();
            System.out.println("\n--- TODAS LOS ENUNCIADOS ---");
            for (Enunciado e : listaEnunciados) {
                System.out.println(e);
            }
            boolean idValido;

            do {
                id = Utilidades.leerInt("Introduce el id del enunciado que quieras añadir: ");
                idValido = false;

                // Comprobar si el id existe en la lista
                for (Enunciado enu : listaEnunciados) {
                    if (enu.getId() == id) {
                        idValido = true;
                        break;
                    }
                }

                if (!idValido) {
                    System.out.println("⚠️ Id inválido, inténtelo de nuevo.");
                }

            } while (!idValido);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }

        try {
            ois = new ObjectInputStream(new FileInputStream(fichero));
            convocatorias = (List<ConvocatoriaExamen>) ois.readObject();
            for (ConvocatoriaExamen c : convocatorias) {
                if (c.getConvocatoria().equalsIgnoreCase(encontrada.getConvocatoria())) {
                    c.setIdEnunciado(id);
                    break;
                }
            }

            oos = new ObjectOutputStream(new FileOutputStream(fichero));
            oos.writeObject(convocatorias);
            System.out.println("Se ha introducido el enunciado exitosamente");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    private void consultarConvocatoriasPorEnunciado() {
        System.out.println("\n--- CONSULTAR CONVOCATORIAS POR ENUNCIADO ---");
        int id = Utilidades.leerInt("Introduce el ID del enunciado: ");

        List<ConvocatoriaExamen> convocatorias = leerConvocatorias();
        boolean encontrada = false;
        System.out.println("\n📝 Convocatoria de Examen\n");
        for (ConvocatoriaExamen c : convocatorias) {
            if (c.getIdEnunciado() == id) {
                System.out.println(c);
                encontrada = true;
            }
        }

        if (!encontrada) {
            System.out.println("⚠️ No hay convocatorias asociadas al enunciado con ID " + id);
        }
    }

    private int selectIdUnidadDidactica(List<UnidadDidactica> uni) {
        int id;
        for (UnidadDidactica u : uni) {
            System.out.println("ID: " + u.getId() + " Nombre:" + u.getTitulo());
        }
        // Pedir id válido
        while (true) {
            id = Utilidades.leerInt("Introduce el id de la unidad didactica: ");

            boolean valido = false;
            for (UnidadDidactica u : uni) {
                if (u.getId() == id) {
                    valido = true;
                    break;
                }
            }

            if (valido) {
                break; // id correcto
            } else {
                System.out.println("❌ ID no válido. Intenta de nuevo.");
            }
        }
        return id;
    }

}
