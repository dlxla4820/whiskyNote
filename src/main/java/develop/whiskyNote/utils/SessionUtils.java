package develop.whiskyNote.utils;

import develop.whiskyNote.dto.UserSessionDto;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.exception.UnauthenticatedException;
import develop.whiskyNote.repository.UserInfoRepository;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class SessionUtils {
    private final UserInfoRepository userInfoRepository;

    public SessionUtils(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }


    public User getUser(RoleType roleType) {
        UserSessionDto userSession = CommonUtils.getUserSession();

        if(roleType.equals(RoleType.ADMIN)) {
            if (!userSession.getRole().equals(RoleType.ADMIN.getRole()) && !userSession.getRole().equals(RoleType.USER.getRole()))
                throw new UnauthenticatedException("Unauthorized");
            if(userSession.getRole().equals(RoleType.USER.getRole()))
                throw new ForbiddenException("Access Denied");
        }
        if(roleType.equals(RoleType.USER))
            if (!userSession.getRole().equals(RoleType.ADMIN.getRole()) && !userSession.getRole().equals(RoleType.USER.getRole()))
                throw new UnauthenticatedException("Unauthorized");

        User user = userInfoRepository.getUserByUuid(userSession.getUuid());
        if(user == null)
            throw new NoSuchElementException("User not found");
        return user;
    }
}
