package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserDataDbClient {
    private static final Config CFG = Config.getInstance();

    public UserEntity createUser(UserEntity userEntity, int transactionLevel) {
        return transaction(connection -> {
                    return new UserdataUserDaoJdbc(connection).createUser(userEntity);
                }, CFG.userdataJdbcUrl()
                , transactionLevel);
    }

    public Optional<UserEntity> findUserById(UUID id, int transactionLevel) {
        return transaction(connection -> {
                    return new UserdataUserDaoJdbc(connection).findById(id);
                }, CFG.userdataJdbcUrl()
                , transactionLevel);
    }

    public Optional<UserEntity> findUserByUsername(String username, int transactionLevel) {
        return transaction(connection -> {
                    return new UserdataUserDaoJdbc(connection).findByUsername(username);
                }, CFG.userdataJdbcUrl()
                , transactionLevel);
    }

    public void deleteUser(UserEntity userEntity, int transactionLevel) {
        transaction(connection -> {
                    new UserdataUserDaoJdbc(connection).delete(userEntity);
                }, CFG.userdataJdbcUrl()
                , transactionLevel);
    }
}
