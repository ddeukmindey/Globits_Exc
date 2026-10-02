package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.UserExtDto;
import com.globits.hr.dto.loginkeycloak.UserKeyCloackDto;
import com.globits.hr.dto.search.UserSearchDto;
import com.globits.security.dto.UserDto;

@Service
public interface UserExtService {
    Page<UserDto> pagingUsers(UserSearchDto dto);

    UserExtDto deleteById(Long userId);

    List<UserExtDto> deleteListId(List<Long> userId);

    ResponseEntity<UserKeyCloackDto> creatUserKeyCloak(UserDto dto);

    UserDto getCurrentUserName();

    UserDto creatUser(UserDto dto);

    ResponseEntity<UserKeyCloackDto> updateUserKeyCloak(UserDto dto);

    StaffDto getCurrentStaff();
}
