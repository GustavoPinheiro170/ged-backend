package br.com.ged.services;

import br.com.ged.domains.Document;
import br.com.ged.domains.DocumentVersion;
import br.com.ged.repositories.DocumentRepository;
import br.com.ged.repositories.DocumentVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentFileServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentVersionRepository versionRepository;

    @InjectMocks
    private DocumentFileService documentFileService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUploadFile() throws Exception {

        Document document = new Document();
        document.setId(1L);

        when(documentRepository.findById(1L))
                .thenReturn(Optional.of(document));

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test.pdf",
                        "application/pdf",
                        "fake-content".getBytes()
                );

        documentFileService.upload(1L, file, "admin");

        verify(versionRepository, times(1))
                .save(any(DocumentVersion.class));
    }

    @Test
    void shouldUpdateArchiveWhenVersionExists() throws Exception {

        Document document = new Document();
        document.setId(1L);

        DocumentVersion version = new DocumentVersion();
        version.setId(1L);

        when(documentRepository.findById(1L))
                .thenReturn(Optional.of(document));

        when(versionRepository.findById(1L))
                .thenReturn(Optional.of(version));

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "update.pdf",
                        "application/pdf",
                        "fake-content".getBytes()
                );

        documentFileService.updateArchive(1L, file, "admin");

        verify(versionRepository, times(1))
                .save(any(DocumentVersion.class));
    }

    @Test
    void shouldCreateVersionIfNotExists() throws Exception {

        Document document = new Document();
        document.setId(1L);

        when(documentRepository.findById(1L))
                .thenReturn(Optional.of(document));

        when(versionRepository.findById(1L))
                .thenReturn(Optional.empty());

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "new.pdf",
                        "application/pdf",
                        "fake-content".getBytes()
                );

        documentFileService.updateArchive(1L, file, "admin");

        verify(versionRepository, times(1))
                .save(any(DocumentVersion.class));
    }

    @Test
    void shouldDownloadArchive() throws Exception {

        DocumentVersion version = new DocumentVersion();
        version.setFileKey("test.pdf");

        when(versionRepository
                .findTopByDocumentIdOrderByUploadedAtDesc(1L))
                .thenReturn(Optional.of(version));

        Path storage = Path.of("storage/test.pdf");

        Files.createDirectories(storage.getParent());
        Files.write(storage, "fake-content".getBytes());

        ResponseEntity<Resource> response =
                documentFileService.downloadArchive(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}