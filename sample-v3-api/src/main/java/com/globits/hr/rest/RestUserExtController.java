package com.globits.hr.rest;

import com.globits.hr.dto.search.UserSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.globits.hr.dto.StaffDto;
import com.globits.hr.service.UserExtService;
import com.globits.security.dto.UserDto;

@RestController
@RequestMapping("/api/user-ext")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestUserExtController {
    @Autowired
    private UserExtService userExtService;

    @RequestMapping(value = "/get-current-user", method = RequestMethod.GET)
    public UserDto getCurrentUserName() {
        return userExtService.getCurrentUserName();
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserDto save(@RequestBody UserDto dto) {
        return userExtService.creatUser(dto);
    }

    @RequestMapping(value = "/get-current-staff", method = RequestMethod.GET)
    public StaffDto getCurrentStaff() {
        return userExtService.getCurrentStaff();
    }

    @PostMapping(value = "/search-user")
    public ResponseEntity<Page<UserDto>> pagingUsers(@RequestBody UserSearchDto searchDto) {
        Page<UserDto> result = userExtService.pagingUsers(searchDto);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
