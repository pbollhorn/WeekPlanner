package app.persistence;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import app.entities.Credentials;
import org.junit.jupiter.api.BeforeEach;

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

            UserMapper.createUser(new Credentials("testuser1", "1111"), connectionPool);
            UserMapper.createUser(new Credentials("testuser2", "2222"), connectionPool);
            UserMapper.createUser(new Credentials("testuser3", "3333"), connectionPool);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}