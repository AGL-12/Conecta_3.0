/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controlador.Controlador;

/**
 *
 * @author precu
 */
public class Main {
        public static void main(String[] args) {
        System.out.println("🚀 Iniciando Sistema de Gestión de Exámenes (Singleton Pattern)");

        // Obtener la única instancia del controlador
        Controlador controlador = Controlador.getInstance();

        // Iniciar la aplicación
        controlador.iniciarAplicacion();
    }
}
