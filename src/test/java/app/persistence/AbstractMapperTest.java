package app.persistence;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;

import app.entities.Credentials;
import app.entities.User;

public class AbstractMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=test";
    private static final String DB = "WeekPlannerDB";
    protected static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @BeforeEach
    public void setUpDatabase() {

        String pathToSqlFile = "src/main/resources/sql/WeekPlannerDB.sql";

        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("""
                    DROP SCHEMA IF EXISTS test CASCADE;
                    CREATE SCHEMA test;
                    """);

            String sql = new String(Files.readAllBytes(Paths.get(pathToSqlFile)));
            stmt.execute(sql);

            // Create testuser1
            Credentials credentials = new Credentials("testuser1", "1111");
            UserMapper.createUser(credentials, connectionPool);

            // Create testuser2
            credentials = new Credentials("testuser2", "2222");
            UserMapper.createUser(credentials, connectionPool);
            User user = UserMapper.login(credentials, connectionPool);
            DataMapper.saveData(user, "Test data for testuser2", connectionPool);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}