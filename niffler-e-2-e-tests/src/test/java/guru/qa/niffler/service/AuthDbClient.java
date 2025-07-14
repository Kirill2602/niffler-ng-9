package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.authority.AuthorityEntity;
import guru.qa.niffler.data.entity.authority.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.Authority;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class AuthDbClient {
    private static final Config CFG = Config.getInstance();

    AuthUserDao userDao = new AuthUserDaoJdbc();
    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(CFG.authJdbcUrl())
            )
    );

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserEntity createUser(UserEntity user) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity userEntity = userDao.createUser(user);
            AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
                    authority -> {
                        AuthorityEntity authorityEntity = new AuthorityEntity();
                        authorityEntity.setUser(user);
                        authorityEntity.setAuthority(authority);
                        return authorityEntity;
                    }
            ).toArray(AuthorityEntity[]::new);
            userEntity.setAuthorities(
                    authorityDao.createAuthority(authorities)
            );
            return userEntity;
        });
    }

    public UserEntity createUserWithChained(UserEntity user) {
        return transactionTemplate.execute(status -> {
                    UserEntity userEntity = userDao.createUser(user);
                    AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
                            authority -> {
                                AuthorityEntity authorityEntity = new AuthorityEntity();
                                authorityEntity.setUser(user);
                                authorityEntity.setAuthority(authority);
                                return authorityEntity;
                            }
                    ).toArray(AuthorityEntity[]::new);
                    userEntity.setAuthorities(
                            authorityDao.createAuthority(authorities)
                    );
                    return userEntity;
                }
        );
    }
}
