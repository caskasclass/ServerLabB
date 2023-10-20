package SQLBuilder;


/**
 *
 * @author lorenzo
 */
public interface SQLInserterInterface {

    void executeQuery();

    void setQuery(String tablename);
    
    void renewQuery();
}
