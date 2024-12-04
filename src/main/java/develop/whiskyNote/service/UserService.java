package develop.whiskyNote.service;

import develop.whiskyNote.auth.TokenProvider;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.repository.UserInfoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static develop.whiskyNote.enums.ErrorCode.PARAMETER_INVALID_SPECIFIC;


@Service
public class UserService {
    private final UserInfoRepository userInfoRepository;
    private final TokenProvider tokenProvider;

    public UserService(UserInfoRepository userInfoRepository, TokenProvider tokenProvider) {
        this.userInfoRepository = userInfoRepository;
        this.tokenProvider = tokenProvider;
    }

    public ResponseDto<?> saveUser(UserRequestDto requestBody){
        if(requestBody.getDeviceId() == null)
            return ResponseDto.builder()
                    .description(Description.FAIL)
                    .code(PARAMETER_INVALID_SPECIFIC.getStatus())
                    .errorCode(PARAMETER_INVALID_SPECIFIC.getErrorCode())
                    .errorDescription(String.format(PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "device_id"))
                    .build();
        User user = userInfoRepository.saveUser(requestBody);

        String tokenSubject = String.format("%s:%s", user.getUuid(), RoleType.USER.getRole());
        Map<String, String> token = new HashMap<>();
        token.put("accessToken",tokenProvider.createAccessToken(tokenSubject));
        token.put("refreshToken", tokenProvider.createRefreshToken(tokenSubject));
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(token)
                .build();
    }

}
