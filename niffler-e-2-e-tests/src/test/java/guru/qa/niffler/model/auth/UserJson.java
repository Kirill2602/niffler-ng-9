package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.authority.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserJson implements Serializable {
    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private List<AuthorityJson> authorities = new ArrayList<>();

    public static UserJson fromEntity(UserEntity entity) {
        UserJson userJson = new UserJson();
        userJson.setId(entity.getId());
        userJson.setUsername(entity.getUsername());
        userJson.setPassword(entity.getPassword());
        userJson.setEnabled(entity.getEnabled());
        userJson.setAccountNonExpired(entity.getAccountNonExpired());
        userJson.setAccountNonLocked(entity.getAccountNonLocked());
        userJson.setCredentialsNonExpired(entity.getCredentialsNonExpired());
        entity.getAuthorities().stream().map(AuthorityJson::fromEntity).forEach(authorityJson -> userJson.authorities.add(authorityJson));
        return userJson;
    }
}
