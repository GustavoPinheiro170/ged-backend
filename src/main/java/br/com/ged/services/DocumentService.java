package br.com.ged.services;

import br.com.ged.domains.Document;
import br.com.ged.domains.enums.DocumentStatus;
import br.com.ged.repositories.DocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document create(Document document) {
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        document.setStatus(DocumentStatus.DRAFT);

        return documentRepository.save(document);
    }

    public Document update(Long id, Document request) {

        Document doc = documentRepository.findById(id)
                .orElseThrow();

        doc.setTitle(request.getTitle());
        doc.setDescription(request.getDescription());
        doc.setTags(request.getTags());
        doc.setUpdatedAt(LocalDateTime.now());

        return documentRepository.save(doc);
    }

    public Document changeStatus(Long id, DocumentStatus status){

        Document doc = documentRepository.findById(id)
                .orElseThrow();

        doc.setStatus(status);
        doc.setUpdatedAt(LocalDateTime.now());

        return documentRepository.save(doc);
    }


}