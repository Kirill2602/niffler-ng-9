package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;


public class UserDataDbClient {
    private static final Config CFG = Config.getInstance();
    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.userdataJdbcUrl())
            )
    );

    public UserEntity createUser(UserEntity userEntity) {
        return txTemplate.execute(status -> userdataUserDao.createUser(userEntity));
    }

    public Optional<UserEntity> findUserById(UUID id) {
        return txTemplate.execute(status -> userdataUserDao.findById(id));
    }

    public Optional<UserEntity> findUserByUsername(String username) {
        return txTemplate.execute(status -> userdataUserDao.findByUsername(username));
    }

    public void deleteUser(UserEntity userEntity) {
        txTemplate.execute(status -> {
            userdataUserDao.delete(userEntity);
            return null;
        });
    }
}
