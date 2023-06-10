package com.peters.User_Registration_and_Email_Verification.product.service;

import com.peters.User_Registration_and_Email_Verification.product.entity.Product;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductImage;
import com.peters.User_Registration_and_Email_Verification.product.exception.ProductImageNotFoundException;
import com.peters.User_Registration_and_Email_Verification.product.exception.ProductNotFoundException;
import com.peters.User_Registration_and_Email_Verification.product.repository.IProductRepository;
import com.peters.User_Registration_and_Email_Verification.product.repository.ProductImageRepository;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository imageRepository;
    private final IProductRepository productRepository;


    private static final String IMAGE_FOLDER = System.getProperty("user.dir") + "/src/main/resources/product_images/";

    public ResponseEntity<CustomResponse> addImageToProduct(MultipartFile file, Long productId) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (product == null) {
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No product found!"));
        }

        if(file.getSize() > 1048576){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Cannot upload file size that is more than 1mb"));
        }

        // Check the file extension
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        List<String> supportedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");
        if (!supportedExtensions.contains(fileExtension)) {
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Invalid image file format! Supported formats: JPG, JPEG, PNG, GIF"));
        }
        long uniqueValue = System.nanoTime();
        String uniqueName = Long.toString(uniqueValue);

        // Generate the new image file name using the product name
        String productName = product.getName();
        String newFileName = productName + "_" +uniqueName+ "." + fileExtension;

        // Read the image file
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Invalid image file!"));
        }

        // Save the image file with the new file name
        String imagePath = IMAGE_FOLDER + newFileName;
        ProductImage image = ProductImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imagePath(imagePath)
                .product(product)
                .build();
        imageRepository.save(image);
        ImageIO.write(bufferedImage, fileExtension, new File(imagePath));

        if (image != null) {
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, "Successfully added image to product"));
        }
        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Something went wrong! Could not add image"));
    }

    public ResponseEntity<CustomResponse> deleteImagesByProductId(Product product) {
        List<ProductImage> images = imageRepository.findByProduct(product);
        if (!images.isEmpty()) {
            for (ProductImage image : images) {
                String imagePath = image.getImagePath();

                // Delete the image file from the folder
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
            }

            // Delete the images from the database
            imageRepository.deleteAll(images);

            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, "Successfully deleted images"));
        }

        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No images found for the given product ID"));
    }





    public byte[] retrieveProductImage (String fileName) throws IOException {
        ProductImage productImage = imageRepository.findByName(fileName).get();
        if(productImage == null){
            throw new ProductImageNotFoundException("Product Image not found");
        }
        String filePath = productImage.getImagePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
