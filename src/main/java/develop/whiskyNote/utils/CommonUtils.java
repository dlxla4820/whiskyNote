package develop.whiskyNote.utils;

import develop.whiskyNote.dto.UserSessionDto;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.exception.UnauthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.UUID;

public class CommonUtils {

    public static UserSessionDto getUserSession() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return UserSessionDto.builder()
                .uuid("anonymous".equals(username) ? null : UUID.fromString(username))
                .role(authorities.iterator().next().toString())
                .build();
    }

    public static UUID getUserUuidIfAdminOrUser(){
        UserSessionDto user = getUserSession();
        String auth = user.getRole();
        if(RoleType.ADMIN.getRole().equals(auth) || RoleType.USER.getRole().equals(auth))
            return user.getUuid();
        else
            throw new UnauthenticatedException("unauthenticated");
    }

    public static UUID getUserUuidIfAdmin(){
        UserSessionDto user = getUserSession();
        if(RoleType.ADMIN.getRole().equals(user.getRole()))
            return user.getUuid();
        if(RoleType.USER.getRole().equals(user.getRole()))
            throw new ForbiddenException("Access Denied");
        else
            throw new UnauthenticatedException("unauthenticated");
    }


    public static boolean containsKorean(String str) throws NullPointerException {
        return str.matches(".*[가-힣].*");
    }
}
