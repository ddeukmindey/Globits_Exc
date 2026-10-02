package com.globits.hr.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.hr.domain.Staff;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.UserExtDto;
import com.globits.hr.dto.loginkeycloak.AccessDto;
import com.globits.hr.dto.loginkeycloak.CredentialDto;
import com.globits.hr.dto.loginkeycloak.UserKeyCloackDto;
import com.globits.hr.dto.search.UserSearchDto;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.UserExtService;
import com.globits.hr.utils.RestApiUtils;
import com.globits.security.domain.User;
import com.globits.security.dto.UserDto;
import com.globits.security.repository.UserRepository;
import com.globits.security.service.UserService;

@Transactional
@Service
public class UserExtServiceImpl implements UserExtService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private Environment env;

    @PersistenceContext
    EntityManager manager;

    @Override
    public Page<UserDto> pagingUsers(UserSearchDto dto) {
        String sqlCount = "Select count(entity.id) from User entity";
        String sql = "select new com.globits.security.dto.UserDto(entity) from User entity where (1=1) ";
        String sqlWhere = "";
        if (dto.getKeyword() != null) {
            sqlWhere = " AND (entity.username LIKE :text OR entity.person.displayName LIKE :text OR entity.email LIKE :text) ";
        }
        sql += sqlWhere;
        Query query = manager.createQuery(sql);
        Query queryCount = manager.createQuery(sqlCount);
        if (dto.getKeyword() != null) {
            query.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();
        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        int startPosition = pageIndex * pageSize;
        query.setFirstResult(startPosition);
        query.setMaxResults(pageSize);
        List<UserDto> userList = query.getResultList();
        long count = (long) queryCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(userList, pageable, count);
    }

    @Override
    public UserExtDto deleteById(Long userId) {
        if (userId == null || userId <= 0) {
            return null;
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && !user.getUsername().equals("admin")) {
            userRepository.delete(user);
            return new UserExtDto(user);
        } else {
            return null;
        }
    }

    @Override
    public List<UserExtDto> deleteListId(List<Long> userId) {
        if (userId == null || userId.size() <= 0) {
            return null;
        }
        List<UserExtDto> ret = new ArrayList<>();
        for (Long long1 : userId) {
            UserExtDto dto = deleteById(long1);
            if (dto != null) {
                ret.add(dto);
            }
        }
        return ret;
    }

    @Override
    public ResponseEntity<UserKeyCloackDto> creatUserKeyCloak(UserDto dto) {
        if (dto != null) {
            UserKeyCloackDto dtoKey = new UserKeyCloackDto();
            dtoKey.setCreatedTimestamp(new Date());
            dtoKey.setUsername(dto.getUsername());
            dtoKey.setEnabled(true);
            dtoKey.setTotp(false);
            dtoKey.setEmailVerified(true);
            if (dto.getPerson() != null && dto.getPerson().getFirstName() != null) {
                dtoKey.setFirstName(dto.getPerson().getFirstName());
            } else if (dto.getFirstName() != null) {
                dtoKey.setFirstName(dto.getFirstName());
            }
            if (dto.getPerson() != null && dto.getPerson().getLastName() != null) {
                dtoKey.setLastName(dto.getPerson().getLastName());
            } else if (dto.getLastName() != null) {
                dtoKey.setLastName(dto.getLastName());
            }
            if (dtoKey.getFirstName() == null && dtoKey.getLastName() == null && dto.getPerson().getDisplayName() != null) {
                String[] output = dto.getPerson().getDisplayName().split("\\s", 0);
                String last = "";
                StringBuilder first = new StringBuilder();
                if (output.length == 1) {
                    last = output[output.length - 1];
                } else if (output.length > 1) {
                    last = output[output.length - 1];
                    for (int i = 0; i < output.length - 1; i++) {
                        if (first.length() > 0) {
                            first.append(" ");
                        }
                        first.append(output[i]);
                    }
                }
                dtoKey.setLastName(last);
                dtoKey.setFirstName(first.toString());
            }

            dtoKey.setEmail(dto.getEmail());
            dtoKey.setDisableCredentialTypes(new ArrayList<>());
            dtoKey.setRequiredActions(new ArrayList<>());
            dtoKey.setNotBefore(0);
            dtoKey.setAccess(new AccessDto());
            dtoKey.getAccess().setImpersonate(true);
            dtoKey.getAccess().setManage(true);
            dtoKey.getAccess().setManageGroupMembership(true);
            dtoKey.getAccess().setMapRoles(true);
            dtoKey.getAccess().setView(true);
            dtoKey.setRealmRoles(new ArrayList<>());
            dtoKey.getRealmRoles().add("mb-user");
            dtoKey.setCredentials(new ArrayList<>());
            CredentialDto cDto = new CredentialDto();
            cDto.setType("password");
            cDto.setValue(dto.getPassword());
            dtoKey.getCredentials().add(cDto);
            String username = "admin";
            String password = "admin";
            String urlLogin = "";
            String urlUser = "";
            if (env.getProperty("hrm.urlLogin") != null) {
                urlLogin = env.getProperty("hrm.urlLogin");
            }
            if (env.getProperty("hrm.urlUser") != null) {
                urlUser = env.getProperty("hrm.urlUser");
            }
            System.out.println(urlLogin);
            System.out.println(urlUser);
            return RestApiUtils.post(username, password, urlLogin, urlUser, dtoKey, UserKeyCloackDto.class);
        }

        return null;
    }

    @Override
    public UserDto getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String userName = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            if (jwt.getClaims() != null && jwt.getClaims().get("preferred_username") != null) {
                userName = jwt.getClaims().get("preferred_username").toString();
            }
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            userName = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            userName = (String) principal;
        } else if (authentication.getName() != null && !"anonymousUser".equals(authentication.getName())) {
            userName = authentication.getName();
        }
        if (userName != null) {
            User modifiedUser = userRepository.findByUsernameAndPerson(userName);
            if (modifiedUser != null) {
                return new UserDto(modifiedUser);
            }
        }
        return null;
    }

    @Override
    public UserDto creatUser(UserDto dto) {
        if (dto != null) {
            this.creatUserKeyCloak(dto);
            return userService.save(dto);
        }
        return null;
    }

    @Override
    public ResponseEntity<UserKeyCloackDto> updateUserKeyCloak(UserDto dto) {
        return null;
    }

    @Override
    public StaffDto getCurrentStaff() {
        UserDto dto = this.getCurrentUserName();
        if (dto != null) {
            if (dto.getUsername() != null) {
                List<Staff> listData = staffRepository.findByCode(dto.getUsername());
                if (listData != null && listData.size() > 0) {
                    StaffDto staffDto = new StaffDto(listData.get(0));
                    staffDto.setUser(dto);
                    return staffDto;
                }
            }
        }
        return null;
    }

}
