package develop.whiskyNote.service;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserInfoRepository userInfoRepository;

    public UserService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public ResponseDto<?> saveUser(UserRequestDto requestBody){
        if(requestBody.getDeviceId() == null)
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(),Description.FAIL,ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorCode(), String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "device_id"));
        UUID userUuid = userInfoRepository.getUuidByDeviceId(requestBody.getDeviceId());
        if(userUuid == null)
            userInfoRepository.saveUser(requestBody);
        return new ResponseDto<>(HttpStatus.OK.value(), Description.SUCCESS);
    }
}
