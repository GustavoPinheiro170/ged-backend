package br.com.ged.services;

import br.com.ged.domains.Document;
import br.com.ged.domains.DocumentVersion;
import br.com.ged.repositories.DocumentRepository;
import br.com.ged.repositories.DocumentVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
}