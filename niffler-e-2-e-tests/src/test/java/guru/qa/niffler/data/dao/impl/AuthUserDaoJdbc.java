package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.authority.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }
    private static final PasswordEncoder PASSWORD = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, userEntity.getUsername());
            ps.setString(2, PASSWORD.encode(userEntity.getPassword()));
            ps.setBoolean(3, userEntity.getEnabled());
            ps.setBoolean(4, userEntity.getAccountNonExpired());
            ps.setBoolean(5, userEntity.getAccountNonLocked());
            ps.setBoolean(6, userEntity.getCredentialsNonExpired());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            userEntity.setId(generatedKey);
            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
