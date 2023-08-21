package SQLBuilder;

import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public interface SQLInserterInterface {

    void executeQuery();

    void setQuery(String tablename, ArrayList<String> column, ArrayList<String> values);
    
    void renewQuery();
}
