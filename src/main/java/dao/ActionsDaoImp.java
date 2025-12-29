package dao;

import models.Action;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActionsDaoImp implements ActionsDAO {

    private static final String SQL_SELECT_ALL_ACTIONS =
            "SELECT ua.user_id, u.username, a.action_id, a.amount, at.description " +
                    "FROM user_actions ua " +
                    "JOIN users u ON ua.user_id = u.user_id " +
                    "JOIN actions a ON ua.action_id = a.action_id " +
                    "JOIN action_type at ON a.action_type_id = at.action_type_id " +
                    "ORDER BY a.timestamp";


    @Override
    public void logAction(Connection con, String username, String actionName) throws SQLException {

    }

    @Override
    public List<Action> getAllActions(Connection con) throws SQLException {
        List<Action> actions = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ALL_ACTIONS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Action action = new Action();
                action.setUserId(rs.getInt("user_id"));
                action.setUsername(rs.getString("username"));
                action.setActionId(rs.getInt("action_id"));
                action.setAmount(rs.getFloat("amount"));
                //action.setTimestamp(rs.getTimestamp("timestamp"));
                action.setDescription(rs.getString("description"));
                actions.add(action);
            }
        }
        return actions;
    }
}
