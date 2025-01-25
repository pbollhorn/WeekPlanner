package app.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import app.entities.Credentials;
import app.entities.User;
import app.exceptions.DatabaseException;

public class UserMapperTest extends AbstractMapperTest {


    @Test
    void login() throws DatabaseException {

        // Valid login
        Credentials credentials = new Credentials("testuser2", "2222");
        User user = UserMapper.login(credentials, connectionPool);
        assertNotNull(user);
        assertEquals(2, user.userId);

        // Another valid login
        credentials = new Credentials("testuser3", "3333");
        user = UserMapper.login(credentials, connectionPool);
        assertNotNull(user);
        assertEquals(3, user.userId);

        // Valid username and invalid password
        credentials = new Credentials("testuser2", "WrongPassword");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

        // Invalid username and valid password
        credentials = new Credentials("dontexist", "3333");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

        // Invalid username and invalid password
        credentials = new Credentials("dontexist", "WrongPassword");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

    }

}
