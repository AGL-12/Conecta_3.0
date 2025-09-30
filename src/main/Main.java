/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dao.Dao;
import dao.DaoimplementMySQL;
import service.ExamenService;
import utilidades.Utilidades;

/**
 *
 * @author juanm
 */
public class Main {
    
    private static Main instance;                     // Única instancia
    private static final Object lock = new Object(); // Para thread-safety
    
    // DEPENDENCIAS - También usando Singleton
    private final Dao dao;
    private final ExamenService examenService;
    
    /**
     * Constructor privado - CLAVE DEL SINGLETON
     * No se puede instanciar desde fuera de la clase
     */
    private Main() {
        // Inicializar dependencias (también pueden ser Singleton)
        this.dao = DaoimplementMySQL.getInstance();
        this.examenService = ExamenService.getInstance();
    }
     /**
     * Método para obtener la única instancia
     */
    public static Main getInstance() {
        if (instance == null) {                    
            synchronized (lock) {                  
                if (instance == null) {           
                    instance = new Main();        // Crear la única instancia
                }
            }
        }
        return instance;
    }
    public static void main(String[] args) {
         System.out.println("🚀 Iniciando Sistema de Gestión de Exámenes (Singleton Pattern)");
        
        // Obtener la única instancia del controlador
        Main controlador = Main.getInstance();
        
        // Iniciar la aplicación
        controlador.iniciarAplicacion();
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


    private void crearUnidadDidactica() {

    }

    private void crearConvocatoria() {

    }

    private void crearEnunciado() {

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
        Main otraInstancia = Main.getInstance();
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
