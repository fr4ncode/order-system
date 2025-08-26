package dev.francode.ordersystem.service.interfaces;

import dev.francode.ordersystem.dto.image.ImageRequest;
import dev.francode.ordersystem.dto.image.ImageResponse;

public interface ImageService {
    ImageResponse uploadImage(ImageRequest request);
}
