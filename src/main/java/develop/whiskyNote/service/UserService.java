package develop.whiskyNote.service;

import develop.whiskyNote.auth.TokenProvider;
import develop.whiskyNote.dto.BackupCodeDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.entity.BackupCode;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.entity.UserWhisky;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ModelNotFoundException;
import develop.whiskyNote.repository.BackupCodeRepository;
import develop.whiskyNote.repository.UserInfoRepository;

import develop.whiskyNote.repository.UserRepository;
import develop.whiskyNote.utils.SessionUtils;
import org.eclipse.jdt.internal.compiler.env.IBinaryNestedType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.security.SecureRandom;
import static develop.whiskyNote.enums.ErrorCode.PARAMETER_INVALID_SPECIFIC;
import static develop.whiskyNote.utils.CommonUtils.CHARACTERS;
import static develop.whiskyNote.utils.CommonUtils.getRandomCode;
import static develop.whiskyNote.utils.Constant.WHISKY_NOT_FOUND;


@Service
@Transactional
public class UserService {
    private final int CODE_LENGTH = 8;
    private final int EXPIRES_IN_MINUTES = 5;
    private final UserInfoRepository userInfoRepository;
    private final BackupCodeRepository backupCodeRepository;
    private final SessionUtils sessionUtils;
    private final TokenProvider tokenProvider;

    public UserService(UserInfoRepository userInfoRepository, BackupCodeRepository backupCodeRepository, SessionUtils sessionUtils, TokenProvider tokenProvider) {
        this.userInfoRepository = userInfoRepository;
        this.backupCodeRepository = backupCodeRepository;
        this.sessionUtils = sessionUtils;
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
        User findUser = userInfoRepository.findUserByDeviceId(requestBody.getDeviceId());
        User user = findUser == null ? userInfoRepository.saveUser(requestBody) : findUser;

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

    public ResponseDto<?> backupUser(BackupCodeDto requestBody){
        User user = sessionUtils.getUser(RoleType.USER);
        BackupCode backupCode = backupCodeRepository.findByCode(requestBody.getCode()).orElseThrow(() -> new ModelNotFoundException("Fail Backup"));
        userInfoRepository.deleteUser(user.getUuid());
        userInfoRepository.updateUserDeviceId(backupCode.getUser().getUuid(), user.getDeviceId());

        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

    public ResponseDto<?> getBackupCode(){
        User user = sessionUtils.getUser(RoleType.USER);
        BackupCode backupCode = BackupCode.builder()
                .code(getRandomCode(CODE_LENGTH))
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(EXPIRES_IN_MINUTES))
                .build();
        backupCodeRepository.save(backupCode);
        BackupCodeDto responseDto = BackupCodeDto.builder()
                .code(backupCode.getCode())
                .build();
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDto)
                .build();
    }


}
