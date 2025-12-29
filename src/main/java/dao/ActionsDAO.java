package dao;

import models.Action;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ActionsDAO {
    void logAction(Connection con, String username, String actionName) throws SQLException;
    List<Action> getAllActions(Connection con) throws SQLException;
}
