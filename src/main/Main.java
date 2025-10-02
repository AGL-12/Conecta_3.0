package main;

import controlador.Controlador;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Gestion de Examenes...\n");
        
        // Obtener instancia Singleton del Controlador
        Controlador controlador = Controlador.getInstance();
        
        // Iniciar aplicación
        controlador.iniciar();
    }
}

