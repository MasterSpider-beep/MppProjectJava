package mpp.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUsersRepository implements UsersRepository{
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public DBUsersRepository(Properties properties) {
        logger.info("Initializing DBUsersRepository with properties: {}", properties);
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public boolean existsUser(String username, String password) {
        logger.traceEntry("Searching user {}", username);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * from Users WHERE username = ? AND password = ?")){
            statement.setString(1,username);
            statement.setString(2,password);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                logger.traceExit("Found correct user");
                return true;
            }
            else{
                logger.traceExit("User not found");
                return false;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

}
