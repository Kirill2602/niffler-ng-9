package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.authority.AuthorityEntity;
import guru.qa.niffler.data.entity.authority.UserEntity;

import java.util.UUID;

public record AuthorityJson(
        UUID id,
        Authority authority,
        UserEntity user
) {
    public static AuthorityJson fromEntity(AuthorityEntity entity) {
        return new AuthorityJson(
                entity.getId(),
                entity.getAuthority(),
                entity.getUser()
        );
    }
}
