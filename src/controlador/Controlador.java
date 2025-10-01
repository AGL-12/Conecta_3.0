/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.Dao;
import dao.DaoimplementMySQL;
import excepciones.ExamenException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.ValidationException;
import modelo.Dificultad;
import modelo.UnidadDidactica;
import service.ExamenService;
import utilidades.Utilidades;

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

            // Usar el servicio Singleton para crear la unidad
            examenService.crearUnidadDidactica(acronimo, titulo, evaluacion, descripcion);
            System.out.println("✅ Unidad didáctica creada exitosamente!");

        } catch (ExamenException e) {
            System.err.println("❌ Error al crear unidad didáctica: " + e.getMessage());
        }
    }

    private void crearConvocatoria() {
        
    }

    private void crearEnunciado() throws ExamenException, ValidationException {
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
        List<UnidadDidactica> unidades = examenService.obtenerTodasLasUnidadesDidacticas();
        if (unidades.isEmpty()) {
            System.out.println("❌ No hay unidades didácticas disponibles. Cree primero una unidad.");
            return;
        }
        System.out.println("\n📚 Unidades didácticas disponibles:");
        for (int i = 0; i < unidades.size(); i++) {
            UnidadDidactica u = unidades.get(i);
            System.out.println((i + 1) + ". " + u.getAcronimo() + " - " + u.getTitulo());
        }
        String seleccionStr = Utilidades.leerString("Seleccione unidades (ej: 1,3,5): ");
        List<Integer> unidadesIds = new ArrayList<>();
        String[] indices = seleccionStr.split(",");
        for (String indiceStr : indices) {
            try {
                int indice = Integer.parseInt(indiceStr.trim()) - 1;
                if (indice >= 0 && indice < unidades.size()) {
                    unidadesIds.add(unidades.get(indice).getId());
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Índice inválido ignorado: " + indiceStr);
            }
        }
        if (unidadesIds.isEmpty()) {
            System.out.println("❌ Debe seleccionar al menos una unidad didáctica.");
            return;
        }
        String convocatoria = Utilidades.leerString("📅 Convocatoria (opcional): ");
        if (convocatoria.trim().isEmpty()) {
            convocatoria = null;
        }
        // Crear el enunciado usando el servicio Singleton
        examenService.crearEnunciado(descripcion, nivel, ruta, unidadesIds, convocatoria);
        System.out.println("✅ Enunciado creado exitosamente!");

    }

    private void consultarConvocatoria() {

    }

    private void consultarEnunciado() {
        
    }

    private void visualizarTextoAsociado() {

    }

    private void asignarEnunciado() {

    }

    /**
     * Método para demostrar el patrón Singleton
     */
    private void mostrarEstadoSingleton() {
        System.out.println("\n🔧 DEMOSTRACIÓN PATRÓN SINGLETON");
        System.out.println(Utilidades.repetir("-", 35));

        // Obtener otra "instancia" (será la misma)
        Controlador otraInstancia = Controlador.getInstance();
        System.out.println("📊 Hash de esta instancia: " + this.hashCode());
        System.out.println("📊 Hash de 'otra' instancia: " + otraInstancia.hashCode());
        System.out.println("🔍 ¿Son la misma instancia? " + (this == otraInstancia ? "✅ SÍ" : "❌ NO"));
        System.out.println("💡 Esto demuestra que Singleton garantiza UNA SOLA INSTANCIA");
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
