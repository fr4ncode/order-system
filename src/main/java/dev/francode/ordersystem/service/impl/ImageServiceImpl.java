package dev.francode.ordersystem.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dev.francode.ordersystem.dto.image.ImageRequest;
import dev.francode.ordersystem.dto.image.ImageResponse;
import dev.francode.ordersystem.entity.Image;
import dev.francode.ordersystem.entity.Product;
import dev.francode.ordersystem.exceptions.custom.ValidationException;
import dev.francode.ordersystem.repository.ImageRepository;
import dev.francode.ordersystem.repository.ProductRepository;
import dev.francode.ordersystem.service.interfaces.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp"
    );

    @Override
    @Transactional
    public ImageResponse uploadImage(ImageRequest request) {
        MultipartFile file = request.getUrl();
        if (file == null || file.isEmpty()) {
            throw new ValidationException("La imagen es obligatoria.");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ValidationException("Tipo de imagen no permitido. Solo JPG, PNG y WEBP.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ValidationException("Producto no encontrado."));

        String uploadedUrl = uploadToCloudinary(file);

        Image image = new Image();
        image.setUrl(uploadedUrl);
        image.setProduct(product);

        imageRepository.save(image);

        return ImageResponse.builder()
                .url(uploadedUrl)
                .build();
    }

    private String uploadToCloudinary(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new ValidationException("Error al subir imagen: " + e.getMessage());
        }
    }
}