package develop.whiskyNote.service;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.repository.UserInfoRepository;
import enums.Description;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static enums.ErrorCode.PARAMETER_INVALID_SPECIFIC;

@Service
public class UserService {
    private final UserInfoRepository userInfoRepository;

    public UserService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public ResponseDto<?> saveUser(UserRequestDto requestBody){
        if(requestBody.getDeviceId() == null)
            return ResponseDto.builder()
                    .description(Description.FAIL)
                    .code(PARAMETER_INVALID_SPECIFIC.getStatus())
                    .errorCode(PARAMETER_INVALID_SPECIFIC.getErrorCode())
                    .errorDescription(String.format(PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "device_id"))
                    .build();
        userInfoRepository.saveUser(requestBody);
        //Todo : jwt 토큰 발급
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

}
