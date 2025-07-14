package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.authority.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.model.auth.Authority;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public List<AuthorityEntity> createAuthority(AuthorityEntity... authorityEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authorityEntity[i].getUser().getId());
                        ps.setString(2, authorityEntity[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorityEntity.length;
                    }
                }
        );
        return Arrays.asList(authorityEntity);
    }

    @Override
    public List<AuthorityEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM authority",
                new BeanPropertyRowMapper<>(AuthorityEntity.class));
    }
}
