package br.com.ged.repositories;

import br.com.ged.domains.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    Optional<DocumentVersion> findTopByDocumentIdOrderByUploadedAtDesc(Long documentId);
}