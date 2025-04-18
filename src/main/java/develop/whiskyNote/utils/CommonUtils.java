package develop.whiskyNote.utils;

import develop.whiskyNote.dto.ErrorMessageResponseDto;
import develop.whiskyNote.dto.UserSessionDto;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.exception.UnauthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommonUtils {

    public static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

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

    public static String getRandomCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }


    public static ErrorMessageResponseDto<?,?> createErrorMessageResponseDtoByErrorMap(HashMap<String, List<String>> errorMap){
        int totalErrorCount = errorMap.values().stream()
                .mapToInt(List::size)
                .sum();
        if(totalErrorCount == 1)
            return ErrorMessageResponseDto.builder()
                    .message(errorMap.values().stream()
                            .flatMap(List::stream)
                            .collect(Collectors.joining(", ")))
                    .errors(errorMap)
                    .build();
        else
            return ErrorMessageResponseDto.builder()
                    .message(String.format("%s (%d more errors)",
                            errorMap.values().stream()
                                    .findFirst()
                                    .map(list -> list.get(0))
                                    .orElse("No errors found"),
                            totalErrorCount - 1))
                    .errors(errorMap)
                    .build();
    }
}
