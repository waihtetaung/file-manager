package com.example.filemanagedemo.controller;

import com.example.filemanagedemo.entity.Document;
import com.example.filemanagedemo.repository.DocumentRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class AppController {

    @Autowired
    private  DocumentRepository documentRepository;

    @GetMapping("/")
    public String viewHomePage(Model model){
        List<Document> documentList = documentRepository.findAll();
        model.addAttribute("documentList", documentList);
        return "home";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document")MultipartFile multipartFile,
                             RedirectAttributes attributes) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        Document document = new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUploadTime(new Date());

        documentRepository.save(document);
        attributes.addFlashAttribute("message", "The file has been uploaded successfully.");

        return "redirect:/";
    }

    @GetMapping("/download")
    public void downloadFile(@Param("id" ) Long id, HttpServletResponse response) throws Exception {
        Optional<Document> result = documentRepository.findById(id);
        if (!result.isPresent()){
            throw new Exception("Could not find document with ID: " + id);
        }

        Document document = result.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + document.getName();
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(document.getContent());
        outputStream.close();
    }
}
