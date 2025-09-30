package utilidades;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Clase Utilidades mejorada Compatible con patrón Singleton y Main corregido
 */
public class Utilidades {

    // Scanner único para toda la clase (mejor práctica)
    private static final Scanner scanner = new Scanner(System.in);

    // =================== MÉTODOS NECESARIOS PARA EL MAIN ===================
    /**
     * Leer String simple (método requerido por Main.java)
     *
     * @param message Mensaje a mostrar al usuario
     * @return String ingresado por el usuario
     */
    public static String leerString(String message) {
        System.out.print(message);
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("Error al leer datos");
            return "";
        }
    }

    /**
     * Leer entero simple (método requerido por Main.java)
     *
     * @param message Mensaje a mostrar al usuario
     * @return Entero ingresado por el usuario
     */
    public static int leerInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Por favor, ingrese un número válido.");
            } catch (NoSuchElementException e) {
                System.out.println("❌ Error al leer datos");
                return 0;
            }
        }
    }

    // =================== MÉTODOS ORIGINALES (MANTENIDOS) ===================
    /**
     * Método original para introducir cadena
     */
    public static String introducirCadena(String mensaje) {
        System.out.println(mensaje);
        try {
            return scanner.next();
        } catch (NoSuchElementException er) {
            System.out.println("Error al introducir datos");
            System.exit(0);
            return "";
        }
    }

    /**
     * Leer String con longitud máxima (método original)
     */
    public static String leerString(int x, String message) {
        String cadena = null;
        boolean ok;
        do {
            ok = true;
            System.out.print(message);
            try {
                cadena = scanner.nextLine().trim();
                if (cadena.length() > x) {
                    System.out.println("❌ Error: máximo " + x + " caracteres permitidos.");
                    ok = false;
                }
            } catch (Exception e) {
                System.out.println("❌ Error al leer datos.");
                ok = false;
            }
        } while (!ok);
        return cadena;
    }

    /**
     * Leer float (método original mejorado)
     */
    public static float leerFloat(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Error: ingrese un número decimal válido.");
            } catch (NoSuchElementException e) {
                System.out.println("❌ Error al leer datos");
                return 0.0f;
            }
        }
    }

    /**
     * Leer respuesta booleana (método original)
     */
    public static boolean leerRespuesta(String message) {
        String respuesta;
        do {
            System.out.print(message + " (si/no, s/n, 1/0, true/false): ");
            respuesta = scanner.nextLine().toLowerCase().trim();
        } while (!respuesta.equals("0") && !respuesta.equals("1")
                && !respuesta.equals("si") && !respuesta.equals("no")
                && !respuesta.equals("s") && !respuesta.equals("n")
                && !respuesta.equals("true") && !respuesta.equals("false"));

        return respuesta.equals("1") || respuesta.equals("si")
                || respuesta.equals("s") || respuesta.equals("true");
    }

    /**
     * Leer entero en rango (método original)
     */
    public static int leerInt(int min, int max, String message) {
        int num;
        boolean ok;
        do {
            try {
                ok = true;
                System.out.print(message + " (entre " + min + " y " + max + "): ");
                String input = scanner.nextLine().trim();
                num = Integer.parseInt(input);

                if (num < min || num > max) {
                    System.out.println("⚠️ Dato fuera de rango. Debe estar entre " + min + " y " + max);
                    ok = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe introducir un número entero.");
                ok = false;
                num = min; // Valor por defecto para continuar el bucle
            }
        } while (!ok);
        return num;
    }

    /**
     * Leer float en rango (método original)
     */
    public static float leerFloat(float min, float max, String message) {
        float numero;
        boolean ok;
        do {
            try {
                ok = true;
                System.out.print(message + " (entre " + min + " y " + max + "): ");
                String input = scanner.nextLine().trim();
                numero = Float.parseFloat(input);

                if (numero < min || numero > max) {
                    System.out.println("⚠️ Dato fuera de rango. Debe estar entre " + min + " y " + max);
                    ok = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe introducir un número decimal.");
                ok = false;
                numero = min; // Valor por defecto para continuar el bucle
            }
        } while (!ok);
        return numero;
    }

    /**
     * Leer un carácter (método original)
     */
    public static char leerChar(String message) {
        boolean error;
        String letra;
        do {
            error = false;
            System.out.print(message);
            letra = scanner.nextLine().trim();
            if (letra.length() != 1) {
                System.out.println("❌ Error: debe introducir exactamente un carácter.");
                error = true;
            }
        } while (error);
        return letra.charAt(0);
    }

    /**
     * Leer fecha en formato dd-MM-yyyy (método original mejorado)
     */
    public static LocalDate pidoFechaDMA(String message) {
        String fechaStr;
        boolean valida;
        LocalDate fecha = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        do {
            valida = true;
            System.out.print(message + " (formato dd-MM-yyyy): ");
            fechaStr = scanner.nextLine().trim();
            try {
                fecha = LocalDate.parse(fechaStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Error: formato de fecha incorrecto. Use dd-MM-yyyy (ejemplo: 25-12-2024)");
                valida = false;
            }
        } while (!valida);
        return fecha;
    }

    /**
     * Leer fecha en formato yyyy-MM-dd (para el Main que espera este formato)
     */
    public static LocalDate leerFecha(String message) {
        String fechaStr;
        boolean valida;
        LocalDate fecha = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        do {
            valida = true;
            System.out.print(message);
            fechaStr = scanner.nextLine().trim();
            try {
                fecha = LocalDate.parse(fechaStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Error: formato de fecha incorrecto. Use yyyy-MM-dd (ejemplo: 2024-12-25)");
                valida = false;
            }
        } while (!valida);
        return fecha;
    }

    /**
     * Leer carácter de un array válido (método original)
     */
    public static char leerCharArray(char[] caracteres, String message) {
        boolean error;
        String letra;
        char resultado = 0;

        do {
            error = false;
            System.out.print(message + " (opciones válidas: ");
            for (int i = 0; i < caracteres.length; i++) {
                System.out.print(caracteres[i]);
                if (i < caracteres.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("): ");

            letra = scanner.nextLine().trim();
            if (letra.length() != 1) {
                System.out.println("❌ Error: debe introducir exactamente un carácter.");
                error = true;
            } else {
                resultado = letra.toUpperCase().charAt(0);
                boolean encontrado = false;
                for (char c : caracteres) {
                    if (Character.toUpperCase(c) == resultado) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    error = true;
                    System.out.println("❌ Error: el carácter introducido no es válido.");
                }
            }
        } while (error);
        return resultado;
    }

    /**
     * Calcular número de objetos en un fichero (método original mejorado)
     */
    public static int calculoFichero(File archivo) {
        int contador = 0;
        if (archivo.exists()) {
            try (FileInputStream fis = new FileInputStream(archivo);
                    ObjectInputStream ois = new ObjectInputStream(fis)) {

                while (true) {
                    try {
                        Object objeto = ois.readObject();
                        contador++;
                    } catch (EOFException e) {
                        // Final del archivo alcanzado
                        break;
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("⚠️ Error al leer el archivo: " + e.getMessage());
            }
        }
        return contador;
    }

    // =================== MÉTODOS DE UTILIDAD ADICIONALES ===================
    /**
     * Pausar hasta que el usuario presione Enter
     */
    public static void pausa() {
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Pausar con mensaje personalizado
     */
    public static void pausa(String message) {
        System.out.print(message);
        scanner.nextLine();
    }

    /**
     * Validar si una cadena no está vacía
     */
    public static boolean validarCadenaNoVacia(String cadena) {
        return cadena != null && !cadena.trim().isEmpty();
    }

    /**
     * Limpiar pantalla (simulado)
     */
    public static void limpiarPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Mostrar mensaje de error estándar
     */
    public static void mostrarError(String mensaje) {
        System.err.println("❌ ERROR: " + mensaje);
    }

    /**
     * Mostrar mensaje de éxito estándar
     */
    public static void mostrarExito(String mensaje) {
        System.out.println("✅ ÉXITO: " + mensaje);
    }

    /**
     * Mostrar mensaje de advertencia estándar
     */
    public static void mostrarAdvertencia(String mensaje) {
        System.out.println("⚠️ ADVERTENCIA: " + mensaje);
    }

    /**
     * Repetir un carácter o string n veces (compatible con Java 8+)
     *
     * @param str String a repetir
     * @param count Número de repeticiones
     * @return String repetido
     */
    public static String repetir(String str, int count) {
        if (count <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * Repetir un carácter n veces (versión optimizada para caracteres)
     *
     * @param ch Carácter a repetir
     * @param count Número de repeticiones
     * @return String con el carácter repetido
     */
    public static String repetir(char ch, int count) {
        if (count <= 0) {
            return "";
        }

        char[] array = new char[count];
        for (int i = 0; i < count; i++) {
            array[i] = ch;
        }
        return new String(array);
    }

}
