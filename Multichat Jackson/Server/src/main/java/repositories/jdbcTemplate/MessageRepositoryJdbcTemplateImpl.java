package repositories.jdbcTemplate;

import config.JdbcTemplateConfig;
import context.Component;
import models.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import repositories.MessageRepository;

import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MessageRepositoryJdbcTemplateImpl implements MessageRepository, Component {

    private JdbcTemplate jdbcTemplate = JdbcTemplateConfig.getInstance().getJdbcTemplate();

    private final String SAVE = "INSERT INTO message (sender_name, text, date) values (?, ?, ?)";
    private final String FIND_ALL_WITH_PAGINATION = "SELECT * FROM message ORDER BY id LIMIT ? OFFSET ?;";

    private RowMapper<Message> messageRowMapper = (row, rowNum) ->
            Message.builder()
            .id(row.getInt("id"))
            .senderName(row.getString("sender_name"))
            .text(row.getString("text"))
            .timeStamp(row.getString("date"))
            .build();


    @Override
    public List<Message> findAllWithPagination(Integer size, Integer page) {
        return jdbcTemplate.query(FIND_ALL_WITH_PAGINATION, new Object[]{size, page*size}, messageRowMapper);
    }

    @Override
    public void save(Message model) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SAVE);
            statement.setString(1, model.getSenderName());
            statement.setString(2, model.getText());
            statement.setString(3, model.getTimeStamp());
            return statement;
        });
    }

    @Override
    public List<Message> findAll() {
        return null;
    }

    @Override
    public Optional<Message> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public String getComponentName() {
        return "MessageRepositoryJdbcTemplateImpl";
    }
}
