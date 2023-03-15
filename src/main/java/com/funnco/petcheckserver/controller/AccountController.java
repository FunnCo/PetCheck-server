package com.funnco.petcheckserver.controller;

import com.funnco.petcheckserver.dto.DtoMapper;
import com.funnco.petcheckserver.dto.request.RequestUserDto;
import com.funnco.petcheckserver.dto.response.EmptyResponse;
import com.funnco.petcheckserver.dto.response.ResponseUserDto;
import com.funnco.petcheckserver.entity.Animal;
import com.funnco.petcheckserver.entity.User;
import com.funnco.petcheckserver.repository.AnimalRepository;
import com.funnco.petcheckserver.repository.UserRepository;
import com.funnco.petcheckserver.utils.AuthUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    private final UserRepository userRepository;
    private final  AuthUtils authUtils;
    private final AnimalRepository animalRepository;

    public AccountController(UserRepository userRepository, AuthUtils authUtils, AnimalRepository animalRepository) {
        this.userRepository = userRepository;
        this.authUtils = authUtils;
        this.animalRepository = animalRepository;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<ResponseUserDto> getUserInfo(@PathVariable(name = "id") Integer id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad ID");
        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with such ID is not found");
        }
        return ResponseEntity.ok(DtoMapper.mapEntityUserToResponseDto(user));
    }

    @GetMapping("/accounts/search")
    public ResponseEntity<List<ResponseUserDto>> searchForAccounts(
            @RequestParam(value = "firstName", required = false) String firstname,
            @RequestParam(value = "lastName", required = false) String lastname,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer searchFrom,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer sizeOfResponse
    ) {
        if (searchFrom == null) {
            searchFrom = 0;
        }
        if (sizeOfResponse == null) {
            sizeOfResponse = 10;
        }
        if (sizeOfResponse <= 0 || searchFrom < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad size or from parameters");
        }
        List<User> matchedUsers = new ArrayList<>();

        if (firstname != null) {
            List<User> matchedByFirstname = userRepository.findUsersByFirstNameContainingIgnoreCase(firstname);
            matchedUsers.addAll(matchedByFirstname);
        }
        if (lastname != null) {
            List<User> matchedByLastname = userRepository.findUsersByLastNameContainingIgnoreCase(lastname);
            matchedUsers.addAll(matchedByLastname);
        }
        if (email != null) {
            List<User> matchedByEmail = userRepository.findUsersByEmailContainingIgnoreCase(email);
            matchedUsers.addAll(matchedByEmail);
        }

        matchedUsers = new ArrayList<>(matchedUsers.stream()
                .collect(Collectors.toMap(User::getId, value -> value, (value1, value2) -> value1))
                .values()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .toList());

        matchedUsers.subList(0, searchFrom).clear();

        List<ResponseUserDto> resultUsers = new ArrayList<>();
        for (int i = 0; i < sizeOfResponse && i < matchedUsers.size(); i++) {
            resultUsers.add(DtoMapper.mapEntityUserToResponseDto(matchedUsers.get(i)));
        }
        return ResponseEntity.ok(resultUsers);
    }

    @PutMapping("/accounts/{accountId}")
    public ResponseEntity<ResponseUserDto> updateUser(@PathVariable("accountId") Integer id, @RequestBody RequestUserDto newInfo, HttpServletRequest request) {
        if (id == null || id <= 0
                || StringUtils.isBlank(newInfo.getFirstName())
                || StringUtils.isBlank(newInfo.getPassword())
                || StringUtils.isBlank(newInfo.getLastName())
                || StringUtils.isBlank(newInfo.getEmail())
                || !AuthUtils.isEmailValid(newInfo.getEmail())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of the parameters are empty, or incorrect");
        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with such ID is not found");
        }
        String authDetails = request.getHeader("Authorization");
        if(authDetails == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No auth details found");
        }
        authDetails = authDetails.substring(6);
        authDetails = authDetails.replace("=", "");

        if(!authUtils.areUsersMatch(authDetails, user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't change other's info");
        }

        if(userRepository.existsByEmail(newInfo.getEmail()) && !Objects.equals(newInfo.getEmail(), user.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with such email already exists");
        }

        user.setPassword(authUtils.hashPassword(newInfo.getPassword(), newInfo.getEmail()));
        user.setEmail(newInfo.getEmail());
        user.setFirstName(newInfo.getFirstName());
        user.setLastName(newInfo.getLastName());
        userRepository.save(user);

        return ResponseEntity.ok(DtoMapper.mapEntityUserToResponseDto(user));
    }

    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<EmptyResponse> deleteUser(@PathVariable("accountId") Integer id, HttpServletRequest request){
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of the parameters are empty, or incorrect");
        }

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with such ID is not found");
        }

        String authDetails = request.getHeader("Authorization");
        if(authDetails == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No auth details found");
        }

        authDetails = authDetails.substring(6);
        authDetails = authDetails.replace("=", "");

        if(!authUtils.areUsersMatch(authDetails, user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't change other's info");
        }

        Animal connectedAnimal = animalRepository.findFirstByChipper(user).orElse(null);
        if(connectedAnimal!=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animals are still connected to this account");
        }
        userRepository.delete(user);
        return ResponseEntity.ok(new EmptyResponse());
    }


}


