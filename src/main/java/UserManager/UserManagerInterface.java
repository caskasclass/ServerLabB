package UserManager;

import java.util.ArrayList;

import jars.User;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */
public interface UserManagerInterface {

    User access();

    void registration(User u);

    ArrayList<String> findexExistingUsers();
    
}
