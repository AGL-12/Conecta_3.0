/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.Dao;
import controlador.DaoimplementMySQL;
import excepciones.DAOException;
import excepciones.ExamenException;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.ValidationException;
import modelo.ConvocatoriaExamen;
import modelo.Dificultad;
import modelo.Enunciado;
import modelo.UnidadDidactica;
import service.ExamenService;
import utilidades.Utilidades;
import utilidades.Utilidades.MyObjectOutputStream;

/**
 *
 * @author juanm
 */
public class Controlador {

    private static Controlador instance;                     // Única instancia
    private static final Object lock = new Object(); // Para thread-safety

    // DEPENDENCIAS - También usando Singleton
    private final Dao dao;
    private final ExamenService examenService;

    /**
     * Constructor privado - CLAVE DEL SINGLETON No se puede instanciar desde
     * fuera de la clase
     */
    private Controlador() {
        // Inicializar dependencias (también pueden ser Singleton)
        this.dao = DaoimplementMySQL.getInstance();
        this.examenService = ExamenService.getInstance();
    }

    /**
     * Método para obtener la única instancia
     */
    public static Controlador getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Controlador();        // Crear la única instancia
                }
            }
        }
        return instance;
    }

    public void iniciarAplicacion() {
        System.out.println("📋 Controlador Singleton inicializado: " + this.hashCode());

        int opcion = 1;

        do {
            try {
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
                        consultarEnunciado();
                        break;
                    case 5:
                        consultarConvocatoria();
                        break;
                    case 6:
                        visualizarTextoAsociado();
                        break;
                    case 7:
                        asignarEnunciado();
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

            } catch (Exception e) {
                System.err.println("💥 Error: " + e.getMessage());
                System.out.println("Presione Enter para continuar...");
            }

        } while (opcion != 0);

        cerrarRecursos();
    }

    private void mostrarMenu() {
        System.out.println(Utilidades.repetir("=", 50));
        System.out.println("            📚 MENÚ PRINCIPAL 📚");
        System.out.println(Utilidades.repetir("=", 50));
        System.out.println("1. 🏗️ Crear Unidad Didáctica");
        System.out.println("2. 🏗️ Crear Convocatoria");
        System.out.println("3. 📝 Crear un Enunciado");
        System.out.println("4. 🔍 Consultar Enunciados");
        System.out.println("5. 📅 Consultar Convocatoria");
        System.out.println("6. 👁️  Visualizar texto asociado a un Enunciado");
        System.out.println("7. ➡️  Asignar un Enunciado a una Convocatoria");
        System.out.println("8. 🔧 Mostrar Estado Singleton (Demo)");
        System.out.println("0. 🚪 Salir");
        System.out.println(Utilidades.repetir("=", 50));
    }

    private void crearUnidadDidactica() throws ValidationException {
        System.out.println("\n🏗️ CREAR UNIDAD DIDÁCTICA");
        System.out.println(Utilidades.repetir("-", 30));

        try {
            String acronimo = Utilidades.leerString("📌 Acrónimo: ");
            String titulo = Utilidades.leerString("📋 Título: ");
            String evaluacion = Utilidades.leerString("📊 Tipo de evaluación (Continua/Final/Mixta): ");
            String descripcion = Utilidades.leerString("📄 Descripción: ");

            examenService.crearUnidadDidactica(acronimo, titulo, evaluacion, descripcion);
            System.out.println("✅ Unidad didáctica creada exitosamente!");

        } catch (ExamenException e) {
            System.err.println("❌ Error al crear unidad didáctica: " + e.getMessage());
        }
    }

    private void crearConvocatoria() {

    }

    private void crearEnunciado() throws DAOException {
        System.out.println("\n📝 CREAR ENUNCIADO");
        System.out.println(Utilidades.repetir("-", 20));

        String descripcion = Utilidades.leerString("📄 Descripción del enunciado: ");
        // Seleccionar dificultad
        System.out.println("\n🎯 Seleccione nivel de dificultad:");
        System.out.println("1. BAJA");
        System.out.println("2. MEDIA");
        System.out.println("3. ALTA");
        int nivelOpc = Utilidades.leerInt("Opción: ");
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
        ;
        String ruta = Utilidades.leerString("📁 Ruta del documento (opcional): ");
        if (ruta.trim().isEmpty()) {
            ruta = null;
        }
        // List<UnidadDidactica> unidades = examenService.obtenerTodasLasUnidadesDidacticas();
        Enunciado enu = new Enunciado(descripcion, nivel, ruta);
        dao.crearEnunciado(enu);
        System.out.println("✅ Enunciado creado exitosamente!");

        List<UnidadDidactica> uni = dao.mostrarUnidades();
        System.out.println("\n📚 Unidades Didácticas disponibles:");
        for (UnidadDidactica u : uni) {
            System.out.println("ID: "+u.getId()+" Nombre:"+u.getTitulo());
        }
        int id = Utilidades.leerInt("Introduce el id de la unidad didactica: ");
        dao.crearUniEnu (id, enu.getId());
        
    }

    private void consultarEnunciado() {
        int id;
        List<Enunciado> enunciados;
        id = Utilidades.leerInt("Introduce el id del enunciado");
        try {
            enunciados = dao.buscarEnunciadosPorUnidadDidactica(id);
            System.out.println(enunciados);
        } catch (DAOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void consultarConvocatoria() {
        File fichero = new File("src/data/convocatorias.dat");

        if (!fichero.exists()) {
            System.out.println("El fichero de convocatorias no existe.");
            return;
        }

        int idBuscado = Utilidades.leerInt("Introduce el ID del enunciado a buscar");
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(fichero));
            boolean encontrado = false;

            while (true) {
                try {
                    ConvocatoriaExamen c = (ConvocatoriaExamen) ois.readObject();

                    for (Enunciado e : c.getEnunciados()) {
                        if (e.getId() == idBuscado) {
                            System.out.println("Convocatoria: " + c.getConvocatoria());
                            System.out.println("Descripción: " + c.getDescripcion());
                            System.out.println("Fecha: " + c.getFecha());
                            System.out.println("Curso: " + c.getCurso());
                            System.out.println("-------------------------------");
                            encontrado = true;
                        }
                    }

                } catch (IOException eof) {
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("No se encontraron convocatorias con ese enunciado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al leer el fichero.");
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                /* ignorar */
            }
        }
    }

    private void visualizarTextoAsociado() {
        int id = Utilidades.leerInt("Introduce el ID del enunciado a visualizar");

        try {
            // Obtener el enunciado desde la base de datos
            Enunciado enunciado = dao.buscarEnunciadoPorId(id);

            if (enunciado == null) {
                System.out.println("No se encontró ningún enunciado con ese ID.");
                return;
            }

            String rutaArchivo = enunciado.getRuta();
            File archivo = new File(rutaArchivo);

            if (!archivo.exists()) {
                System.out.println("El archivo de texto asociado no existe: " + rutaArchivo);
                return;
            }

            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(archivo));
                String contenido = (String) ois.readObject();
                System.out.println("Contenido del enunciado ID " + id + ":");
                System.out.println("------------------------------------------------");
                System.out.println(contenido);
                System.out.println("------------------------------------------------");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al leer el archivo del enunciado: " + e.getMessage());
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        /* ignorar */
                    }
                }
            }

        } catch (DAOException e) {
            System.out.println("Error al buscar el enunciado: " + e.getMessage());
        }
    }

    private void asignarEnunciado() {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        String nombre;
        int id;
        File fichero = new File("src/data/convocatorias.dat");
        List<ConvocatoriaExamen> convocatorias = new ArrayList<>();
        ConvocatoriaExamen encontrada = null;

        nombre = Utilidades.introducirCadena("Introduce el nombre de la convocatoria");
        id = Utilidades.leerInt("Introduce el id que quieras añadir");

        try {
            ois = new ObjectInputStream(new FileInputStream(fichero));
            convocatorias = (List<ConvocatoriaExamen>) ois.readObject();
            try {
                for (ConvocatoriaExamen c : convocatorias) {
                    if (c.getConvocatoria().equalsIgnoreCase(nombre)) {
                        encontrada = c;
                        break;
                    }
                }
                if (encontrada == null) {
                    System.out.println("No se encontró la convocatoria: " + nombre);
                    return;
                }
                Enunciado enunciado = dao.buscarEnunciadoPorId(id);
                if (enunciado == null) {
                    System.out.println("No se encontró el enunciado con ID: " + id);
                    return;
                }
                if (!encontrada.getEnunciados().contains(enunciado)) {
                    encontrada.getEnunciados().add(enunciado);
                    System.out.println("Enunciado asignado correctamente a la convocatoria.");
                } else {
                    System.out.println("El enunciado ya estaba asignado a esta convocatoria.");
                }
                oos = new ObjectOutputStream(new FileOutputStream(fichero));
                oos.writeObject(convocatorias);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DAOException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
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

    /**
     * Método para demostrar el patrón Singleton
     */
    private void mostrarEstadoSingleton() {
        /*System.out.println("\n🔧 DEMOSTRACIÓN PATRÓN SINGLETON");
        System.out.println(Utilidades.repetir("-", 35));

        // Obtener otra "instancia" (será la misma)
        Main otraInstancia = Main.getInstance();
        System.out.println("📊 Hash de esta instancia: " + this.hashCode());
        System.out.println("📊 Hash de 'otra' instancia: " + otraInstancia.hashCode());
        System.out.println("🔍 ¿Son la misma instancia? " + (this == otraInstancia ? "✅ SÍ" : "❌ NO"));
        System.out.println("💡 Esto demuestra que Singleton garantiza UNA SOLA INSTANCIA");*/
        File fichero = new File("src/data/convocatorias.dat");

        try {
            // 1️⃣ Pedimos datos de la convocatoria
            String convocatoria = Utilidades.introducirCadena("Introduce el nombre de la convocatoria");
            String descripcion = Utilidades.introducirCadena("Introduce la descripción de la convocatoria");
            LocalDate fecha = LocalDate.parse(Utilidades.introducirCadena("Introduce la fecha (YYYY-MM-DD)"));
            String curso = Utilidades.introducirCadena("Introduce el curso");

            // 2️⃣ Pedimos IDs de enunciados
            int idEnunciado1 = Integer.parseInt(Utilidades.introducirCadena("Introduce el ID del primer enunciado"));
            int idEnunciado2 = Integer.parseInt(Utilidades.introducirCadena("Introduce el ID del segundo enunciado"));

            // 3️⃣ Obtenemos los enunciados desde la BD
            Enunciado e1 = dao.buscarEnunciadoPorId(idEnunciado1);
            Enunciado e2 = dao.buscarEnunciadoPorId(idEnunciado2);

            if (e1 == null || e2 == null) {
                System.out.println("No se encontraron uno o ambos enunciados.");
                return;
            }

            // 4️⃣ Creamos lista de enunciados
            List<Enunciado> enunciados = new ArrayList<>();
            enunciados.add(e1);
            enunciados.add(e2);

            // 5️⃣ Creamos la convocatoria
            ConvocatoriaExamen c = new ConvocatoriaExamen(convocatoria, descripcion, fecha, curso);
            c.setEnunciados(enunciados);

            // 6️⃣ Guardamos la convocatoria en el fichero
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
                oos.writeObject(c);
                System.out.println("Convocatoria guardada correctamente con 2 enunciados.");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (DAOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpieza de recursos al cerrar la aplicación
     */
    private void cerrarRecursos() {
        try {
            if (dao != null) {
                dao.cerrarRecursos();
            }
            System.out.println("🔄 Recursos cerrados correctamente.");
        } catch (Exception e) {
            System.err.println("⚠️ Error al cerrar recursos: " + e.getMessage());
        }
    }

    // Getters para acceder a los servicios desde otras clases
    public Dao getDao() {
        return dao;

    }

    public ExamenService getExamenService() {
        return examenService;
    }

}
