package com.funnco.petcheckserver.utils;

import com.funnco.petcheckserver.entity.User;
import com.funnco.petcheckserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class AuthUtils {

    final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public AuthUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String hashPassword(String password, String personalSalt){
        String stringToHash = password + ":" + personalSalt + ":secret_pepper_566B59703373367639792442264529482B4D6251655468576D5A713474377721";
        StringBuilder result = new StringBuilder();
        DigestUtils.appendMd5DigestAsHex(stringToHash.getBytes(StandardCharsets.UTF_8), result);
        return result.toString();
    }

    public boolean areUserDetailsValid(String token) {
        String[] decodedDetails = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8).split(":");
        User user = userRepository.findByEmailAndPassword(decodedDetails[0], hashPassword(decodedDetails[1], decodedDetails[0]));
        return user != null;
    }

    public boolean areUsersMatch(String token, User user){
        String[] decodedDetails = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8).split(":");
        User userFromToken = userRepository.findByEmailAndPassword(decodedDetails[0], hashPassword(decodedDetails[1], decodedDetails[0]));
        return userFromToken.getId().equals(user.getId());
    }
    public static boolean isEmailValid(String email){
        return email.matches("^[0-9A-Za-z._\\-]+@[A-Za-z]+\\.[a-z]{2,3}$");
    }
}
