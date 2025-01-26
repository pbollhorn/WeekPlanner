package app.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import app.entities.Credentials;
import app.entities.User;
import app.exceptions.MapperException;

public class DataMapperTest extends AbstractMapperTest {

    @Test
    void loadData() throws MapperException {

        // Valid user
        Credentials credentials = new Credentials("testuser2", "2222");
        User user = UserMapper.login(credentials, connectionPool);
        assertEquals("Test data for testuser2", DataMapper.loadData(user, connectionPool));

        // Null user
        assertThrows(MapperException.class, () -> DataMapper.loadData(null, connectionPool));

    }

    @Test
    void saveData() throws MapperException {

        // Valid user
        Credentials credentials = new Credentials("testuser2", "2222");
        User user = UserMapper.login(credentials, connectionPool);
        DataMapper.saveData(user, "new data written to database", connectionPool);
        assertEquals("new data written to database", DataMapper.loadData(user, connectionPool));

        // Null user
        assertThrows(MapperException.class, () -> DataMapper.saveData(null, "new data written to database", connectionPool));

    }

}