package com.globits.hr.rest;

import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

@RestController("hrPersonController")
@RequestMapping("/api/person")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestPersonController {

    private static final Logger logger = LoggerFactory.getLogger(RestPersonController.class);

    @Autowired
    private Environment env;

    @PersistenceContext
    private EntityManager entityManager;

    private String getUploadFolder() {
        String base = env.getProperty("hrm.file.folder");
        if (base == null || base.trim().isEmpty()) {
            base = "d:/GLobits_Excrs/GLobits/uploads/";
        }
        File baseDir = new File(base, "avatars");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir.getAbsolutePath() + File.separator;
    }

    /**
     * Upload avatar image for a Person
     * POST /api/person/upload-avatar
     */
    @Transactional
    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "personId", required = false) String personId) {
        Map<String, Object> response = new HashMap<>();

        if (file == null || file.isEmpty()) {
            response.put("success", false);
            response.put("message", "File không được để trống");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = ".png";
            }

            String filename = UUID.randomUUID().toString() + extension;
            String folder = getUploadFolder();
            File targetFile = new File(folder, filename);
            file.transferTo(targetFile);

            response.put("success", true);
            response.put("name", filename);
            response.put("avatar", filename);
            response.put("imagePath", filename);
            response.put("avatarUrl", "/api/person/avatar/" + filename);
            response.put("fullUrl", "http://localhost:8071/api/person/avatar/" + filename);

            if (personId != null && !personId.trim().isEmpty()) {
                try {
                    Query qPerson = entityManager.createNativeQuery(
                            "UPDATE tbl_person SET avatar = :avatar, image_path = :avatar WHERE id = :id");
                    qPerson.setParameter("avatar", filename);
                    qPerson.setParameter("id", personId);
                    qPerson.executeUpdate();

                    Query qStaff = entityManager.createNativeQuery(
                            "UPDATE tbl_staff SET avatar = :avatar WHERE id = :id");
                    qStaff.setParameter("avatar", filename);
                    qStaff.setParameter("id", personId);
                    qStaff.executeUpdate();
                    response.put("personId", personId);
                } catch (Exception ex) {
                    logger.error("Error updating person avatar in DB: {}", ex.getMessage());
                }
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error upload avatar: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Upload and update avatar for specific person ID
     * POST /api/person/{id}/avatar
     */
    @Transactional
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadAvatarForPerson(
            @PathVariable("id") String id,
            @RequestParam("file") MultipartFile file) {
        return uploadAvatar(file, id);
    }

    /**
     * Get avatar image by filename
     * GET /api/person/avatar/{filename}
     */
    @GetMapping(value = "/avatar/{filename:.+}")
    public void getAvatarImage(HttpServletResponse response, @PathVariable("filename") String filename) throws IOException {
        String folder = getUploadFolder();
        File file = new File(folder, filename);
        if (!file.exists()) {
            // Also check parent folder
            File parentFile = new File(env.getProperty("hrm.file.folder", "d:/GLobits_Excrs/GLobits/uploads/"), filename);
            if (parentFile.exists()) {
                file = parentFile;
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Avatar not found");
                return;
            }
        }

        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "image/jpeg";
        }
        response.setContentType(contentType);
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            IOUtils.copy(in, out);
            out.flush();
        }
    }

    /**
     * Get avatar image by person ID
     * GET /api/person/{id}/avatar
     */
    @GetMapping(value = "/{id}/avatar")
    public void getAvatarByPersonId(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        try {
            Query q = entityManager.createNativeQuery("SELECT avatar, image_path FROM tbl_person WHERE id = :id");
            q.setParameter("id", id);
            List<?> result = q.getResultList();
            if (result != null && !result.isEmpty()) {
                Object[] row = (Object[]) result.get(0);
                String avatar = row[0] != null ? row[0].toString() : (row[1] != null ? row[1].toString() : null);
                if (avatar != null && !avatar.trim().isEmpty()) {
                    getAvatarImage(response, avatar);
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Error retrieving avatar for person {}: {}", id, e.getMessage());
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No avatar for person");
    }

    /**
     * Get Person information by ID
     * GET /api/person/{id}
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> getPerson(@PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Query q = entityManager.createNativeQuery(
                    "SELECT id, first_name, last_name, display_name, gender, birth_date, email, phone_number, avatar, image_path FROM tbl_person WHERE id = :id");
            q.setParameter("id", id);
            List<?> list = q.getResultList();
            if (list != null && !list.isEmpty()) {
                Object[] row = (Object[]) list.get(0);
                map.put("id", row[0]);
                map.put("firstName", row[1]);
                map.put("lastName", row[2]);
                map.put("displayName", row[3]);
                map.put("gender", row[4]);
                map.put("birthDate", row[5]);
                map.put("email", row[6]);
                map.put("phoneNumber", row[7]);
                map.put("avatar", row[8] != null ? row[8] : row[9]);
                map.put("avatarUrl", map.get("avatar") != null ? "/api/person/avatar/" + map.get("avatar") : null);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error getting person {}: {}", id, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
