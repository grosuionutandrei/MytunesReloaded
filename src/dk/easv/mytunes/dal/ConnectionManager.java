package dk.easv.mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.ExceptionsMessages;


import java.sql.Connection;

public class ConnectionManager {
    private final SQLServerDataSource ds;

    public ConnectionManager() {
        ds = new SQLServerDataSource();
        ds.setDatabaseName("CSe2023b_e_10_MyTunes");
        ds.setUser("CSe2023b_e_10");
        ds.setPassword("CSe2023bE10#23");
        ds.setServerName("EASV-DB4");
        ds.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws MyTunesException {
        try {
            return ds.getConnection();
        } catch (SQLServerException e) {
            throw new MyTunesException(ExceptionsMessages.NO_DATABASE_CONNECTION,e.getCause());
        }
    }
}

