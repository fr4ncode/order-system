package dev.francode.ordersystem.controller;

import dev.francode.ordersystem.dto.image.ImageRequest;
import dev.francode.ordersystem.dto.image.ImageResponse;
import dev.francode.ordersystem.service.interfaces.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ImageResponse> uploadImage(@Valid @ModelAttribute ImageRequest request) {
        ImageResponse response = imageService.uploadImage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
