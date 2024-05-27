package com.fixluck.hogwartartifactsonline.hogwartuser.converter;

import com.fixluck.hogwartartifactsonline.hogwartuser.HogwartsUser;
import com.fixluck.hogwartartifactsonline.hogwartuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {

        //Ta ko gửi password vào DTO
        UserDto userDto = new UserDto(source.getId(),
                                      source.getUsername(),
                                      source.isEnabled(),
                                      source.getRoles());

        return userDto;
    }
}
