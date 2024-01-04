package UserManager;

import jars.User;

/**
 *
 * @author lorenzo
 */
public interface UserManagerInterface {

    User access();

    boolean registration(User u);
    
}
