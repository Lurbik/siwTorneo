package it.uniroma3.siw.torneo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";

    public String salvaFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        // Crea la cartella se non esiste
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Nome unico per evitare conflitti
        String nomeFile = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(nomeFile);
        Files.copy(file.getInputStream(), filePath);

        return nomeFile;
    }
}