package com.E_commerce.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final String DIRETORIO = "uploads";

    public String salvar(MultipartFile arquivo) throws IOException {

        if (arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio.");
        }
        if (arquivo.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("A imagem deve ter no máximo 5 MB.");
        }
        String nome = arquivo.getOriginalFilename();

        if (nome == null ||
                !(nome.toLowerCase().endsWith(".png") ||
                        nome.toLowerCase().endsWith(".jpg") ||
                        nome.toLowerCase().endsWith(".jpeg") ||
                        nome.toLowerCase().endsWith(".gif") ||
                        nome.toLowerCase().endsWith(".webp"))) {

            throw new IllegalArgumentException("Formato de imagem não permitido.");
        }

        BufferedImage imagem = ImageIO.read(arquivo.getInputStream());

        if (imagem == null) {
            throw new IllegalArgumentException("O arquivo enviado não é uma imagem válida.");
        }

        String tipo = arquivo.getContentType();

        if (tipo == null || !tipo.startsWith("image/")) {
            throw new IllegalArgumentException("O arquivo deve ser uma imagem.");
        }

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
