package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDto<?>> register(@RequestBody UserRequestDto requestBody){
        ResponseDto<?> response = userService.saveUser(requestBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

}
