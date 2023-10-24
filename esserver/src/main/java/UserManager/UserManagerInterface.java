package UserManager;

import pkg.User;

/**
 *
 * @author lorenzo
 */
public interface UserManagerInterface {

    User access();

    void registration(User u);
    
}
