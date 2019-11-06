package dao;

import config.ConnectionConfig;
import models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class MessageDaoImpl implements MessageDao {
    private final ConnectionConfig CONFIG = ConnectionConfig.getInstance();
    private final String SAVE = "INSERT INTO message (sender_name, text, date) values (?, ?, ?)";
    private final String FIND_ALL_WITH_PAGINATION = "SELECT * FROM message ORDER BY id LIMIT ? OFFSET ?;";


    @Override
    public LinkedList<Message> findAllWithPagination(Integer size, Integer page) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(FIND_ALL_WITH_PAGINATION);
            statement.setInt(1, size);
            statement.setInt(2, page);
            ResultSet set = statement.executeQuery();
            LinkedList<Message> messages = new LinkedList<>();
            while (set.next()) {
                Message message = new Message();
                message.setText(set.getString("text"));
                message.setTimeStamp(set.getString("date"));
                message.setSenderName(set.getString("sender_name"));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }

    @Override
    public void save(Message model) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = CONFIG.getConnection();
            statement = connection.prepareStatement(SAVE);
            statement.setString(1, model.getSenderName());
            statement.setString(2, model.getText());
            statement.setString(3, model.getTimeStamp());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            CONFIG.close(statement);
            CONFIG.close(connection);
        }
    }
}
