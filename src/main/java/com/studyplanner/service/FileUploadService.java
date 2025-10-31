package com.studyplanner.service;

import com.studyplanner.entity.FileAttachment;
import com.studyplanner.entity.User;
import com.studyplanner.repository.FileAttachmentRepository;
import com.studyplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileAttachmentRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public FileAttachment uploadFile(MultipartFile file) throws IOException {
        User user = getCurrentUser();

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setFileName(file.getOriginalFilename());
        fileAttachment.setFileType(file.getContentType());
        fileAttachment.setFilePath(filePath.toString());
        fileAttachment.setFileSize(file.getSize());
        fileAttachment.setUser(user);

        return fileRepository.save(fileAttachment);
    }

    public List<FileAttachment> getUserFiles() {
        User user = getCurrentUser();
        return fileRepository.findByUserId(user.getId());
    }

    public void deleteFile(Long id) throws IOException {
        User user = getCurrentUser();
        FileAttachment file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

        if (!file.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        Path filePath = Paths.get(file.getFilePath());
        Files.deleteIfExists(filePath);
        fileRepository.delete(file);
    }
}
