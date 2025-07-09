package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.authority.UserEntity;

public interface AuthUserDao {
    UserEntity createUser(UserEntity userEntity);
}
