package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.authority.AuthorityEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public List<AuthorityEntity> createAuthority(AuthorityEntity... authorityEntity) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO authority (user_id, authority) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity authority : authorityEntity) {
                ps.setObject(1, authority.getUser().getId());
                ps.setObject(2, authority.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
            UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                int id = 0;
                if (rs.next()) {
                    while (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                        authorityEntity[id++].setId(generatedKey);
                    }
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            return Arrays.asList(authorityEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAllAuthorities() {
        List<AuthorityEntity> authorities = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority")) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    while (rs.next()) {
                        AuthorityEntity authority = new AuthorityEntity();
                        authorities.add(authority);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorities;
    }
}

