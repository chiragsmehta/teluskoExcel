package com.example.teluskoExcel.controllers;

import com.example.teluskoExcel.models.Product;
import com.example.teluskoExcel.services.ExcelUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelUploadController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUploadController.class);

    @Autowired
    private ExcelUploadService excelUploadService;

    // Endpoint to upload an Excel file
    @PostMapping("/upload")
    public ResponseEntity<List<Product>> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Received file upload request for: {}", file.getOriginalFilename());
        List<Product> savedData = excelUploadService.uploadExcel(file);
        return ResponseEntity.ok(savedData);
    }

    // Endpoint to download the product data as a .json file
    @GetMapping("/download/products")
    public ResponseEntity<byte[]> downloadProducts() throws IOException {
        logger.info("Request received to download product data.");
        List<Product> products = excelUploadService.getAllProducts();

        // Convert product data to JSON array format for download
        String json = new ObjectMapper().writeValueAsString(products);
        byte[] jsonData = json.getBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=products.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonData);
    }
}

