package repositories.jdbcTemplate;

import config.ConnectionConfig;
import config.JdbcTemplateConfig;
import context.Component;
import dto.UserSignInDto;
import models.AuthData;
import models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import repositories.UsersRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository, Component {

    private final String FIND_BY_MAIL = "SELECT * FROM chat_user WHERE \"mail\"=?";
    private final String SAVE = "INSERT INTO chat_user (name, mail, password) VALUES (?, ?, ?)";
    private final String UPDATE_VERIFIER = "UPDATE chat_user SET \"verifier\" = ? WHERE \"id\" = ?";
    private final String FIND_VERIFIER_BY_ID = "SELECT verifier FROM chat_user WHERE \"id\" = ?";
    private final String FIND_BY_ID = "SELECT * FROM chat_user WHERE \"id\" = ?";

    private JdbcTemplate jdbcTemplate = JdbcTemplateConfig.getInstance().getJdbcTemplate();
    private RowMapper<User> userRowMapper = (row, rowNumber) ->
            User.builder()
            .id(row.getInt("id"))
            .name(row.getString("name"))
            .role(row.getString("role"))
            .build();

    private RowMapper<UserSignInDto> userSignInDtoRowMapper = (row, rowNumber) ->
            UserSignInDto.builder()
                    .password(row.getString("password"))
                    .id(row.getInt("id"))
                    .mail(row.getString("mail"))
                    .role(row.getString("role"))
                    .name(row.getString("name"))
                    .build();

    @Override
    public Optional<User> findByMailAndPassword(String mail, String password) {
        try {
            UserSignInDto userSignInDto = jdbcTemplate.queryForObject(FIND_BY_MAIL, new Object[]{mail}, userSignInDtoRowMapper);
            if (isPasswordEqualUserPassword(password, userSignInDto.getPassword())) {
                return Optional.ofNullable(User.builder()
                        .id(userSignInDto.getId())
                        .name(userSignInDto.getName())
                        .authData(new AuthData(userSignInDto.getMail()))
                        .role(userSignInDto.getRole())
                        .build());
            } else {
                return Optional.ofNullable(null);
            }
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private boolean isPasswordEqualUserPassword(String password, String userPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, userPassword);
    }

    @Override
    public String findVerifierById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(FIND_VERIFIER_BY_ID, new Object[]{id}, (row, rowNum) ->
                    row.getString("verifier"));
        }  catch (EmptyResultDataAccessException e) {
            return "NO VERIFIER";
        }
    }

    @Override
    public void updateVerifier(String verifier, Integer id) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(UPDATE_VERIFIER);
            statement.setString(1, verifier);
            statement.setInt(2, id);
            return statement;
        });
    }

    @Override
    public void save(User model) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Integer id) {
        try {
            User user = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public String getComponentName() {
        return "UsersRepositoryJdbcTemplateImpl";
    }
}
