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
public class EntityNotFoundException extends ExamenException {
    public EntityNotFoundException(String entity, Object id) {
        super("No se encontró " + entity + " con ID: " + id);
    }
}
