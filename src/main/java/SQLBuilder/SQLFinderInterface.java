package SQLBuilder;

import java.sql.ResultSet;

/**
 *
 * @author lorenzo
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
