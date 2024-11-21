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
            
    }
}
