package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;

public class UserDataDbClient {
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    public UserEntity createUser(UserEntity userEntity) {
        return userdataUserDao.createUser(userEntity);
    }

    public Optional<UserEntity> findUserById(UUID id) {
        return userdataUserDao.findById(id);
    }
    public Optional<UserEntity> findUserByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    public void deleteUser(UserEntity userEntity) {
        userdataUserDao.delete(userEntity);
    }
}
