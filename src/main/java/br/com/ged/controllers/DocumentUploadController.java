package br.com.ged.controllers;

import br.com.ged.domains.DocumentVersion;
import br.com.ged.repositories.DocumentVersionRepository;
import br.com.ged.services.DocumentFileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {

        DocumentVersion version =
                documentVersionRepository.findTopByDocumentIdOrderByUploadedAtDesc(id)
                        .orElseThrow();

        Path path = Paths.get("storage/" + version.getFileKey());

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + version.getFileKey())
                .body(resource);
    }
}