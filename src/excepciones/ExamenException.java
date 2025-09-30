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
public class ExamenException extends Exception{
    public ExamenException(String message) {
        super(message);
    }
    public ExamenException(String message, Throwable cause) {
        super(message, cause);
    }
}
