package SQLBuilder;

import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public interface SQLInserterInterface {

    void executeQuery();

    void setQuery(String tablename);
    
    void renewQuery();

    void setColums(ArrayList<String> ar);

    void setValues(ArrayList<String> ar);

    void updateTrackPopularity(String trackId);
}
