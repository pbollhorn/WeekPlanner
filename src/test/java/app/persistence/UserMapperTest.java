package app.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import app.entities.Credentials;
import app.entities.User;
import app.exceptions.DatabaseException;

public class UserMapperTest extends AbstractMapperTest {


    @Test
    void login() throws DatabaseException {

        // Valid username and valid password
        Credentials credentials = new Credentials("testuser3", "3333");
        User user = UserMapper.login(credentials, connectionPool);
        assertNotNull(user);

        // Valid username and invalid password
        credentials = new Credentials("testuser3", "0000");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

        // Invalid username and valid password
        credentials = new Credentials("dontexist", "3333");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

    }

}
