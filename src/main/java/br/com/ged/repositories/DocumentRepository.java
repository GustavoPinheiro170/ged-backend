package br.com.ged.repositories;

import br.com.ged.domains.Document;
import br.com.ged.domains.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Page<Document> findByTitleContainingAndStatus(
            String title,
            DocumentStatus status,
            Pageable pageable
    );

    Page<Document> findByTitleContainingIgnoreCaseAndStatus(String title, DocumentStatus status, Pageable pageable);

    Page<Document> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Document> findByStatus(DocumentStatus status, Pageable pageable);
}