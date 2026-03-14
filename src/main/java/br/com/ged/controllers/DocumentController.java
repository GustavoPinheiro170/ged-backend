package br.com.ged.controllers;

import br.com.ged.domains.Document;
import br.com.ged.domains.enums.DocumentStatus;
import br.com.ged.repositories.DocumentRepository;
import br.com.ged.services.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    private final DocumentRepository documentRepository;

    public DocumentController(DocumentService service, DocumentRepository documentRepository) {
        this.documentService = service;
        this.documentRepository = documentRepository;
    }

    @PostMapping
    public Document create(@RequestBody Document doc){
        return documentService.create(doc);
    }

    @PutMapping("/{id}")
    public Document update(
            @PathVariable Long id,
            @RequestBody Document doc){

        return documentService.update(id, doc);
    }

    @PatchMapping("/{id}/status")
    public Document changeStatus(
            @PathVariable Long id,
            @RequestParam DocumentStatus status){

        return documentService.changeStatus(id, status);
    }

    @GetMapping
    public Page<Document> getDocuments(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) DocumentStatus status,
            Pageable pageable
    ) {

        if (title != null && status != null) {
            return documentRepository.findByTitleContainingIgnoreCaseAndStatus(title, status, pageable);
        } else if (title != null) {
            return documentRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (status != null) {
            return documentRepository.findByStatus(status, pageable);
        } else {
            return documentRepository.findAll(pageable);
        }
    }


}