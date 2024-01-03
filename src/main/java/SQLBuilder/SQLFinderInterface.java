package SQLBuilder;

import java.sql.ResultSet;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */

public interface SQLFinderInterface {

    ResultSet executeQuery();

    void renewQuery();

    void renewResultSet();

    void setFrom(String from);

    void setQuery(String select, String from);

    void setQuery(String select, String from, String where);

    void setSelect(String select);

    void setWhere(String where);

    ResultSet getRes(); 

    String getQuery();
}
