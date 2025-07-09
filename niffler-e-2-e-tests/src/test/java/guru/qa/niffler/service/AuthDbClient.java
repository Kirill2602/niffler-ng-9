package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.authority.AuthorityEntity;
import guru.qa.niffler.data.entity.authority.UserEntity;
import guru.qa.niffler.model.auth.Authority;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.transaction;

public class AuthDbClient {
    private static final Config CFG = Config.getInstance();

    public UserEntity createUser(UserEntity user, int transactionLevel) {
        return transaction(connection -> {
            UserEntity userEntity =  new AuthUserDaoJdbc(connection).createUser(user);
            AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
                    authority -> {
                        AuthorityEntity authorityEntity = new AuthorityEntity();
                        authorityEntity.setUser(user);
                        authorityEntity.setAuthority(authority);
                        return authorityEntity;
                    }
            ).toArray(AuthorityEntity[]::new);
            userEntity.setAuthorities(
                    new AuthAuthorityDaoJdbc(connection).createAuthority(authorities)
            );
            return userEntity;
        }, CFG.authJdbcUrl(), transactionLevel);
    }
}
