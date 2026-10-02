package com.globits.hr.utils;

import com.globits.hr.dto.loginkeycloak.AccessDto;
import com.globits.hr.dto.loginkeycloak.CredentialDto;
import com.globits.hr.dto.loginkeycloak.UserKeyCloackDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;

public class RestApiUtils {
    public static String host = "";
    public static String port = "";
    public static String api = "";
    public static String access_token = "";
    public static String token_type = "";
    public static String url = host + ":" + port + api;


    public static ResponseEntity<String> postLogin(String username, String password, String url) {
        try {
            HttpHeaders headersLogin = new HttpHeaders();
            headersLogin.add("Content-Type", "application/x-www-form-urlencoded");
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", "admin-cli");
            map.add("grant_type", "password");
            map.add("username", username);
            map.add("password", password);
            map.add("scope", "openid");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headersLogin);
            RestTemplate restTemplate = new RestTemplate();

            try {
                ResponseEntity<String> responseLogin = restTemplate.exchange(url,
                        HttpMethod.POST, request, String.class);
                System.out.println(responseLogin.getStatusCodeValue());

                if (responseLogin.getBody() != null && responseLogin.getBody().length() > 0) {
                    if (responseLogin.getBody().contains(",")) {
                        String[] output = responseLogin.getBody().split(",");
                        if (output.length > 0) {
                            for (String s : output) {
                                if (s != null && s.contains(":")) {
                                    String[] acc = s.split(":");
                                    if (acc.length > 0) {
                                        for (int j = 0; j < acc.length; j++) {
                                            if (acc[j].contains("access_token")) {
                                                access_token = acc[j + 1];
                                                access_token = access_token.replace('"', ' ');
                                                access_token = access_token.replace('"', ' ');
                                            }
                                            if (acc[j].contains("token_type")) {
                                                token_type = acc[j + 1];
                                                token_type = token_type.replace('"', ' ');
                                                token_type = token_type.replace('"', ' ');
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getLocalizedMessage());
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> ResponseEntity<T> post(String username, String password, String urlLogin, String url, Object parameterObject, Class<T> returnType) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        postLogin(username, password, urlLogin);
        headers.add("Authorization", RestApiUtils.token_type + " " + RestApiUtils.access_token);
        HttpEntity<T> entity = new HttpEntity<T>((T) parameterObject, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, returnType);
    }

    public static void main(String[] args) {
        UserKeyCloackDto dto = new UserKeyCloackDto();
        dto.setCreatedTimestamp(new Date());
        dto.setUsername("letung1");
        dto.setEnabled(true);
        dto.setTotp(false);
        dto.setEmailVerified(true);
        dto.setFirstName("le");
        dto.setLastName("tung1");
        dto.setEmail("letung1@gmail.com");
        dto.setDisableCredentialTypes(new ArrayList<>());
        dto.setRequiredActions(new ArrayList<>());
        dto.setNotBefore(0);
        dto.setAccess(new AccessDto());
        dto.getAccess().setImpersonate(true);
        dto.getAccess().setManage(true);
        dto.getAccess().setManageGroupMembership(true);
        dto.getAccess().setMapRoles(true);
        dto.getAccess().setView(true);
        dto.setRealmRoles(new ArrayList<>());
        dto.getRealmRoles().add("mb-user");
        dto.setCredentials(new ArrayList<>());
        CredentialDto cDto = new CredentialDto();
        cDto.setType("password");
        cDto.setValue("letung2");
        postLogin("admin", "admin", "http://gcom.globits.net:8073/auth/realms/master/protocol/openid-connect/token");
        System.out.print(RestApiUtils.token_type + " " + RestApiUtils.access_token);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<UserKeyCloackDto> s = post("admin", "admin", "http://gcom.globits.net:8073/auth/realms/master/protocol/openid-connect/token", "http://gcom.globits.net:8073/auth/admin/realms/globits/users", dto, UserKeyCloackDto.class);
        System.out.print(s);
    }
}
