package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.authority.AuthorityEntity;

import java.util.List;

public interface AuthAuthorityDao {
    List<AuthorityEntity> createAuthority(AuthorityEntity ... authorityEntity);
}
