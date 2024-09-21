package com.example.teluskoExcel.services;


import com.example.teluskoExcel.models.Product;
import com.example.teluskoExcel.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ExcelUploadService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUploadService.class);

    @Autowired
    private ProductRepository productRepository;

    // Upload and process the Excel file
    public List<Product> uploadExcel(MultipartFile file) throws IOException {
        logger.info("Starting file upload for: {}", file.getOriginalFilename());
        List<Product> productList = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

        // Extract the "Product" sheet
        Sheet productSheet = workbook.getSheet("Product");
        if (productSheet == null) {
            logger.error("Sheet 'Product' not found in the Excel file");
            throw new IllegalArgumentException("Sheet 'Product' not found in the Excel file");
        }

        // Read headers dynamically
        List<String> headers = new ArrayList<>();
        for (Row row : productSheet) {
            if (row.getRowNum() == 0) {
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    headers.add(row.getCell(j).getStringCellValue());
                }
            } else {
                Map<String, Object> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    String value = row.getCell(j) != null ? row.getCell(j).toString() : null;
                    rowData.put(header, value);
                }

                // Convert row data to JSON string
                String jsonData = new ObjectMapper().writeValueAsString(rowData);
                Product product = new Product();
                product.setData(jsonData);
                productList.add(product);
            }
        }

        workbook.close();
        logger.info("Successfully processed file: {}", file.getOriginalFilename());
        return productRepository.saveAll(productList);
    }

    // Retrieve all products for download
    public List<Product> getAllProducts() {
        logger.info("Retrieving all products from the database.");
        return productRepository.findAll();
    }
}

