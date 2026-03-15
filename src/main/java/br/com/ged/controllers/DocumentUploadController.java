package br.com.ged.controllers;

import br.com.ged.domains.DocumentVersion;
import br.com.ged.repositories.DocumentVersionRepository;
import br.com.ged.services.DocumentFileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@RestController
@RequestMapping("/documents")
public class DocumentUploadController {

    private final DocumentFileService service;

    private final DocumentVersionRepository documentVersionRepository;

    public DocumentUploadController(DocumentFileService service, DocumentVersionRepository documentVersionRepository) {
        this.service = service;
        this.documentVersionRepository = documentVersionRepository;
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<?> upload(
            @PathVariable Long id,
            @RequestParam MultipartFile file,
            Principal principal) throws IOException {
            service.upload(id, file, principal.getName());
            return ResponseEntity.ok("Arquivo enviado");

    }

    @PutMapping("/{id}/update/archive")
    public ResponseEntity<?> updateDocumentArchive(
            @PathVariable Long id,
            @RequestParam MultipartFile file,
            Principal principal) throws IOException {
        service.updateArchive(id, file, principal.getName());
        return ResponseEntity.ok("Arquivo Atualizado");

    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(@PathVariable Long id) throws Exception {
        return  service.downloadArchive(id);
    }
}