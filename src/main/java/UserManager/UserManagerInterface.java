package UserManager;

import jars.User;

/**
 *
 * @author lorenzo
 */
public interface UserManagerInterface {

    User access();

    void registration(User u);
    
}
