package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.authority.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserDaoJdbc implements AuthUserDao {
    private static final Config CFG = Config.getInstance();


    private static final PasswordEncoder PASSWORD = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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

    @Override
    public List<UserEntity> findAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\"")) {
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    while (rs.next()) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setUsername(rs.getString("username"));
                        userEntity.setPassword(PASSWORD.encode(rs.getString("password")));
                        userEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                        userEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                        userEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                        users.add(userEntity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
