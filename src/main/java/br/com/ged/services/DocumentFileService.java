package br.com.ged.services;

import br.com.ged.domains.Document;
import br.com.ged.domains.DocumentVersion;
import br.com.ged.repositories.DocumentRepository;
import br.com.ged.repositories.DocumentVersionRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentFileService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;

    public DocumentFileService(
            DocumentRepository documentRepository,
            DocumentVersionRepository versionRepository) {

        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
    }

    public void upload(Long documentId, MultipartFile file, String user) throws IOException {
        try {
            Document doc = documentRepository.findById(documentId)
                    .orElseThrow();

            String fileKey = UUID.randomUUID() + "_" + file.getOriginalFilename();

            if (!Files.exists(Paths.get("storage"))) {
                Files.createDirectories(Paths.get("storage"));
            }

            Path path = Paths.get("storage/" + fileKey);




            Files.write(path, file.getBytes());

            DocumentVersion version = new DocumentVersion();
            version.setDocument(doc);
            version.setFileKey(fileKey);
            version.setUploadedAt(LocalDateTime.now());
            version.setUploadedBy(user);

            versionRepository.save(version);
        }catch (RuntimeException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void updateArchive(Long documentId, MultipartFile file, String user) throws IOException {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow();

        String fileKey = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path path = Paths.get("storage/" + fileKey);
        Files.write(path, file.getBytes());

        Optional<DocumentVersion> version = versionRepository.findById(doc.getId());

        if(version.isPresent()) {
            version.get().setDocument(doc);
            version.get().setFileKey(fileKey);
            version.get().setUploadedAt(LocalDateTime.now());
            version.get().setUploadedBy(user);
            versionRepository.save(version.get());
        }else {
            DocumentVersion initialVersion = new DocumentVersion();
            initialVersion.setDocument(doc);
            initialVersion.setFileKey(fileKey);
            initialVersion.setUploadedAt(LocalDateTime.now());
            initialVersion.setUploadedBy(user);

            versionRepository.save(initialVersion);
        }
    }


    public ResponseEntity<Resource> downloadArchive(Long id) throws Exception {

        try {
            DocumentVersion version =
                    versionRepository.findTopByDocumentIdOrderByUploadedAtDesc(id)
                            .orElseThrow();

            String encodedFileName =
                    URLEncoder.encode(version.getFileKey(), StandardCharsets.UTF_8);

            Path path = Paths.get("storage/" + version.getFileKey());

            Resource resource = new UrlResource(path.toUri());
            String contentType = Files.probeContentType(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=UTF-8" + encodedFileName )
                    .body(resource);
        }catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
}