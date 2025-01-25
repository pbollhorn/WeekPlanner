package app.persistence;

import java.sql.Connection;
import java.sql.SQLException;
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

        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("""
                    DROP TABLE IF EXISTS user_data;
                    CREATE TABLE user_data
                    (
                        LIKE public.user_data INCLUDING DEFAULTS INCLUDING INDEXES INCLUDING CONSTRAINTS
                    );
                    """);

            Credentials credentials = new Credentials("testuser", "1234");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}