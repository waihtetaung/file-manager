package com.example.filemanagedemo.controller;

import com.example.filemanagedemo.entity.Document;
import com.example.filemanagedemo.repository.DocumentRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class AppController {

    private final DocumentRepository documentRepository;

    @GetMapping("/")
    private String home(){
        return "welcome";
    }

    @GetMapping("/home")
    public String viewHomePage(Model model){
        List<Document> documentList = documentRepository.findAll();
        model.addAttribute("documentList", documentList);
        return "home";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document") MultipartFile multipartFile,
                             RedirectAttributes attributes) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        String uploadDir = "/MemberName/ProjectName/";
        String filePath = uploadDir + fileName;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }

        Document document = new Document();
        document.setName(fileName);
        document.setSize(multipartFile.getSize());
        document.setUploadTime(new Date());
        document.setFilePath(filePath);

        documentRepository.save(document);
        attributes.addFlashAttribute("message", "The file has been uploaded successfully.");

        return "redirect:/";
    }


    @GetMapping("/download")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response) throws Exception {
        Optional<Document> result = documentRepository.findById(id);
        if (result.isEmpty()) {
            throw new Exception("Could not find document with ID: " + id);
        }

        Document document = result.get();

        String filePath = document.getFilePath();
        File file = new File(filePath);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + document.getName());

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

}
