package guru.qa.niffler.data.entity.authority;

import guru.qa.niffler.model.auth.Authority;
import guru.qa.niffler.model.auth.AuthorityJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity implements Serializable {
    private UUID id;
    private Authority authority;
    private UserEntity user;


    public static AuthorityEntity fromJson(AuthorityJson json) {
        AuthorityEntity entity = new AuthorityEntity();
        entity.setId(json.id());
        entity.setAuthority(json.authority());
        entity.setUser(json.user());
        return entity;
    }
}
