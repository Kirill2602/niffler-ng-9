package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.authority.UserEntity;

import java.util.List;

public interface AuthUserDao {
    UserEntity createUser(UserEntity userEntity);

    List<UserEntity> findAllUsers();
}
