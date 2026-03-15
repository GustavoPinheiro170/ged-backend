package br.com.ged.repositories;

import br.com.ged.domains.Document;
import br.com.ged.domains.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query(
            value = """
            SELECT *
            FROM document
            WHERE (:title IS NULL OR LOWER(title) LIKE '%' || CAST(LOWER(:title) AS TEXT) || '%')
            AND (:status IS NULL OR status = :status)
            """,
                    countQuery = """
            SELECT COUNT(*)
            FROM document
            WHERE (:title IS NULL OR LOWER(title) LIKE '%' || CAST(LOWER(:title) AS TEXT) || '%')
            AND (:status IS NULL OR status = :status)
            """,
                    nativeQuery = true
    )
    Page<Document> findByFilters(@Param("title") String title, @Param("status") String status, Pageable pageable);
}