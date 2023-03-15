package com.funnco.petcheckserver.controller;

import com.funnco.petcheckserver.dto.DtoMapper;
import com.funnco.petcheckserver.dto.request.RequestUserDto;
import com.funnco.petcheckserver.dto.response.ResponseUserDto;
import com.funnco.petcheckserver.entity.User;
import com.funnco.petcheckserver.repository.UserRepository;
import com.funnco.petcheckserver.utils.AuthUtils;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public AuthenticationController(UserRepository userRepository, AuthUtils authUtils) {
        this.userRepository = userRepository;
        this.authUtils = authUtils;
    }

    @PostMapping("/registration")
    public ResponseEntity<ResponseUserDto> registerNewUser(@RequestBody RequestUserDto newUser) {
        User user = DtoMapper.mapRequestUserDtoToEntity(newUser);
        if (StringUtils.isBlank(user.getFirstName())
                || StringUtils.isBlank(user.getPassword())
                || StringUtils.isBlank(user.getLastName())
                || StringUtils.isBlank(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of the parameters are empty, or incorrect");
        }
        if (!AuthUtils.isEmailValid(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of the parameters are empty, or incorrect");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with such email already exists");
        }
        user.setPassword(authUtils.hashPassword(user.getPassword(), user.getEmail()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.mapEntityUserToResponseDto(user));
    }
}

