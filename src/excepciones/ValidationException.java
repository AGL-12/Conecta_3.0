/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excepciones;

/**
 *
 * @author juanm
 */
public class ValidationException extends ExamenException{
    public ValidationException(String message) {
        super("Error de validación: " + message);
    }
}
