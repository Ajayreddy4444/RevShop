package Util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;

public class DBConnectionTest {

    @Test
    public void testGetConnection() {

        Connection con = DBConnection.getConnection();
        assertNotNull(con);
    }
}
