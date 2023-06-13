package com.example.proyectogticsgrupo2.controller;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.ByteBuffer;

@RestController
public class GcsController {

    public static byte[] downloadObject
            (String projectId, String bucketName, String blobName) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, blobName);

        try (ReadChannel reader = storage.reader(blobId)) {
            ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
            while (reader.read(bytes) > 0) {
                bytes.flip();
                bytes.clear();
            }
            byte[] image = bytes.array();
            return image;
        }

    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> displayItemImage() throws IOException {
        byte[] image = downloadObject("hola-a20d1", "lab5william", "image.jpeg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @GetMapping("/imagenEvento")
    public ResponseEntity<byte[]> displayItemImage(@RequestParam("id") int id) throws IOException {
//        byte[] image = downloadObject("plenary-magpie-386203", "spring-bucket-jchavezs", "image.jpeg");
        String blobName = "proyecto/foto-evento-" + id +".jpeg";

        byte[] image = downloadObject("hola-a20d1", "lab5william", blobName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
