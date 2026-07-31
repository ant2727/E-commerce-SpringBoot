package com.E_commerce.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final String DIRETORIO = "uploads";

    public String salvar(MultipartFile arquivo) throws IOException {

        Path pasta = Paths.get(DIRETORIO);

        if (!Files.exists(pasta)) {
            Files.createDirectories(pasta);
        }

        String nomeArquivo = System.currentTimeMillis()
                + "_"
                + arquivo.getOriginalFilename();

        Path destino = pasta.resolve(nomeArquivo);

        Files.copy(
                arquivo.getInputStream(),
                destino
        );

        return nomeArquivo;
    }
}
