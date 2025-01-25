package app.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import app.entities.Credentials;
import app.entities.User;
import app.exceptions.DatabaseException;

public class DataMapperTest extends AbstractMapperTest {

    @Test
    void loadData() throws DatabaseException {

        // Valid user
        Credentials credentials = new Credentials("testuser2", "2222");
        User user = UserMapper.login(credentials, connectionPool);
        assertEquals("Test data for testuser2", DataMapper.loadData(user, connectionPool));

        // Null user
        assertThrows(DatabaseException.class, () -> DataMapper.loadData(null, connectionPool));

    }

    @Test
    void saveData() throws DatabaseException {

        // Valid user
        Credentials credentials = new Credentials("testuser2", "2222");
        User user = UserMapper.login(credentials, connectionPool);
        DataMapper.saveData(user, "new data written to database", connectionPool);
        assertEquals("new data written to database", DataMapper.loadData(user, connectionPool));

        // Null user
        assertThrows(DatabaseException.class, () -> DataMapper.saveData(null, "new data written to database", connectionPool));

    }

}