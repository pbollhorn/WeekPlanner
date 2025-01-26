package app.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import app.entities.Credentials;
import app.entities.User;
import app.exceptions.MapperException;

public class UserMapperTest extends AbstractMapperTest {

    @Test
    void login() throws MapperException {

        // Valid login
        Credentials credentials = new Credentials("testuser1", "1111");
        User user = UserMapper.login(credentials, connectionPool);
        assertNotNull(user);
        assertEquals(1, user.userId());

        // Another valid login
        credentials = new Credentials("testuser2", "2222");
        user = UserMapper.login(credentials, connectionPool);
        assertNotNull(user);
        assertEquals(2, user.userId());

        // Valid username and invalid password
        credentials = new Credentials("testuser2", "WrongPassword");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

        // Invalid username and valid password
        credentials = new Credentials("dontexist", "2222");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

        // Invalid username and invalid password
        credentials = new Credentials("dontexist", "WrongPassword");
        user = UserMapper.login(credentials, connectionPool);
        assertNull(user);

    }

    @Test
    void createUser() throws MapperException {

        // Valid new user
        Credentials credentials = new Credentials("testuser3", "3333");
        UserMapper.createUser(credentials, connectionPool);
        User user = UserMapper.login(credentials, connectionPool);
        assertNotNull(user);
        assertEquals(3, user.userId());

        // Invalid new user because username already exists
        assertThrows(MapperException.class, () -> UserMapper.createUser(new Credentials("testuser1", "0000"), connectionPool));

    }

//    @Test
//    void deleteUser() throws DatabaseException {
//
//        // Delete valid user
//        Credentials credentials = new Credentials("testuser1", "1111");
//        User user = UserMapper.login(credentials, connectionPool);
//        assertNotNull(user);
//        UserMapper.deleteUser(user, connectionPool);
//        user = UserMapper.login(credentials, connectionPool);
//        assertNull(user);
//
//        // Delete null user
//        assertThrows(DatabaseException.class, () -> UserMapper.deleteUser(null, connectionPool));
//
//    }

}