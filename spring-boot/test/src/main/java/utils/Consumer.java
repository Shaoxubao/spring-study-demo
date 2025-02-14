package utils;

import java.sql.SQLException;

public interface Consumer<T> {

    void accept(T t) throws SQLException;

}
