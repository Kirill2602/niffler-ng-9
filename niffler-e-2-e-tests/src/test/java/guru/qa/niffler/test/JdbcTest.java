package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDataDbClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Date;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class JdbcTest {
    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.create(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-6",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx-0",
                        "duck"
                )
        );

        System.out.println(spend);
    }

    @Test
    void xaTxTest() {
        UserDataDbClient usersDbClient = new UserDataDbClient();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username87");
        userEntity.setSurname("surname");
        userEntity.setFirstname("firstname");
        userEntity.setFullname("fullname");
        userEntity.setPhoto(new byte[]{1, 2, 3});
        userEntity.setPhotoSmall(new byte[]{4, 5, 6});
        userEntity.setCurrency(CurrencyValues.USD);
        UserEntity user = usersDbClient.createUser(userEntity);
        System.out.println(UserJson.fromEntity(user));
    }

    @Test
    void dbTest() {
        AuthDbClient authDbClient = new AuthDbClient();
        guru.qa.niffler.data.entity.authority.UserEntity userEntity = new guru.qa.niffler.data.entity.authority.UserEntity();
        userEntity.setUsername("username0");
        userEntity.setPassword("password99");
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        guru.qa.niffler.data.entity.authority.UserEntity user = authDbClient.createUser(userEntity);
        System.out.println(guru.qa.niffler.model.auth.UserJson.fromEntity(user));
    }

    @Test
    void dbTes2() {
        UserdataUserDao userDao = new UserdataUserDaoSpringJdbc();
        UserEntity user = userDao.findByUsername("Kirill").get();
        System.out.println(UserJson.fromEntity(user));
    }

    @Test
    void dbTes3() {
        UserdataUserDao userDao = new UserdataUserDaoSpringJdbc();
        userDao.findAll().forEach(user -> System.out.println(UserJson.fromEntity(user)));
    }

    @Test
    void dbTes4() {
        AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
        authAuthorityDao.findAllAuthorities()
                .forEach(a -> System.out.println(AuthorityJson.fromEntity(a)));
    }

    @Test
    @DisplayName("createUserWithChained")
    void dbTes5() {
        AuthDbClient client = new AuthDbClient();
        guru.qa.niffler.data.entity.authority.UserEntity userEntity = new guru.qa.niffler.data.entity.authority.UserEntity();
        userEntity.setUsername("username112");
        userEntity.setPassword("password99");
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        guru.qa.niffler.data.entity.authority.UserEntity user = client.createUserWithChained(userEntity);
        System.out.println(guru.qa.niffler.model.auth.UserJson.fromEntity(user));
    }
    /*
     * C username = null
     * вставка не идет в обе таблицы user и authority
     * ===============================================
     * С валидным юзером и в setAuthority(null)
     * в таблицу user происходит запись, а в authority нет (не происходит откат)
     * */
}
