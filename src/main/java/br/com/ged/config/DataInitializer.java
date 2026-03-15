package br.com.ged.config;

import br.com.ged.domains.Document;
import br.com.ged.domains.User;
import br.com.ged.domains.enums.DocumentStatus;
import br.com.ged.domains.enums.Profiles;
import br.com.ged.repositories.DocumentRepository;
import br.com.ged.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, DocumentRepository documentRepository) {
        return args -> {

            if(userRepository.count() == 0){
                if (!userRepository.existsByUsername("admin")) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword("123456");
                    admin.setProfile(List.of(Profiles.ADMIN, Profiles.USER));
                    userRepository.save(admin);
                }
                if (!userRepository.existsByUsername("user")) {
                    User user = new User();
                    user.setUsername("user");
                    user.setPassword("123456");
                    user.setProfile(List.of(Profiles.USER));
                    userRepository.save(user);

                }

            }

            if(documentRepository.count() == 0){

                Document draft = new Document();
                draft.setTitle("Proposta_Comercial_Cliente_Acme_2026");
                draft.setDescription("Documento de proposta comercial em elaboração para o cliente Acme.");
                draft.setTags(List.of("proposta","comercial","cliente-acme"));
                draft.setOwner("gustavo.pinheiro");
                draft.setStatus(DocumentStatus.DRAFT);
                draft.setCreatedAt(LocalDateTime.now().minusDays(1));
                draft.setUpdatedAt(LocalDateTime.now());

                Document published = new Document();
                published.setTitle("Contrato_Servicos_TI_Acme_2026");
                published.setDescription("Contrato oficial de prestação de serviços de TI.");
                published.setTags(List.of("contrato","juridico","servicos-ti"));
                published.setOwner("departamento.juridico");
                published.setStatus(DocumentStatus.PUBLISHED);
                published.setCreatedAt(LocalDateTime.now().minusDays(20));
                published.setUpdatedAt(LocalDateTime.now().minusDays(15));

                Document archived = new Document();
                archived.setTitle("Relatorio_Financeiro_Projeto_X_2022");
                archived.setDescription("Relatório financeiro final do Projeto X arquivado.");
                archived.setTags(List.of("financeiro","relatorio","projeto-x"));
                archived.setOwner("financeiro");
                archived.setStatus(DocumentStatus.ARCHIVED);
                archived.setCreatedAt(LocalDateTime.now().minusYears(2));
                archived.setUpdatedAt(LocalDateTime.now().minusYears(1));

                documentRepository.saveAll(List.of(draft, published, archived));
            }

        };
    }
}