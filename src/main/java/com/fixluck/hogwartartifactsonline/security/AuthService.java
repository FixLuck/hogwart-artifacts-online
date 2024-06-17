package com.fixluck.hogwartartifactsonline.security;

import com.fixluck.hogwartartifactsonline.hogwartuser.HogwartsUser;
import com.fixluck.hogwartartifactsonline.hogwartuser.MyUserPrincipal;
import com.fixluck.hogwartartifactsonline.hogwartuser.converter.UserToUserDtoConverter;
import com.fixluck.hogwartartifactsonline.hogwartuser.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserToUserDtoConverter userToUserDtoConverter;

    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserToUserDtoConverter userToUserDtoConverter, JwtProvider jwtProvider) {
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.jwtProvider = jwtProvider;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Create user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogwartsUser hogwartsUser = principal.getHogwartsUser();
        UserDto userDto = this.userToUserDtoConverter.convert(hogwartsUser);
        // Create a JWT

        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResult = new HashMap<>();

        loginResult.put("userInfo", userDto);
        loginResult.put("token", token);

        return loginResult;

    }
}
