package dev.francode.ordersystem.dto.image;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRequest {

    private MultipartFile url;
    private Long productId;
}
