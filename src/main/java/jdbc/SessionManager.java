package jdbc;

import java.sql.Connection;

public interface SessionManager {
    void beginSession();

    void commitSession();

    void rollbackSession();

    void close();

    Connection getCurrentSession();

}
