package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.authority.AuthorityEntity;
import guru.qa.niffler.data.entity.authority.UserEntity;
import guru.qa.niffler.model.auth.Authority;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorityRowMapper implements RowMapper<AuthorityEntity> {
    private AuthorityRowMapper() {

    }

    public static final AuthorityRowMapper instance = new AuthorityRowMapper();

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity entity = new AuthorityEntity();
        entity.setId(rs.getObject("id", UUID.class));
        entity.setUser(rs.getObject("user", UserEntity.class));
        entity.setAuthority(rs.getObject("authority", Authority.class));
        return entity;
    }
}
