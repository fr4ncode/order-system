package dev.francode.ordersystem.config.initializer;

import dev.francode.ordersystem.entity.Category;
import dev.francode.ordersystem.entity.Product;
import dev.francode.ordersystem.entity.UserApp;
import dev.francode.ordersystem.entity.enums.ERole;
import dev.francode.ordersystem.repository.CategoryRepository;
import dev.francode.ordersystem.repository.ProductRepository;
import dev.francode.ordersystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataInicializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInicializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios

        if (userRepository.findByEmail("falabella@gmail.com").isEmpty()) {
            UserApp normalUser = new UserApp();
            normalUser.setEmail("falabella@gmail.com");
            normalUser.setPassword(passwordEncoder.encode("987654321"));
            normalUser.setRol(ERole.CLIENTE);
            userRepository.save(normalUser);
            System.out.println("Usuario falabella creado exitosamente.");
        } else {
            System.out.println("El usuario normal ya existe.");
        }

        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            UserApp adminUser = new UserApp();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("987654321"));
            adminUser.setRol(ERole.ADMIN);
            userRepository.save(adminUser);
            System.out.println("Usuario admin creado exitosamente.");
        } else {
            System.out.println("El usuario admin ya existe.");
        }

        // Crear categorías
        if (categoryRepository.count() == 0) {
            Category spaProducts = new Category();
            spaProducts.setName("Productos de Spa");

            Category cuidadoPersonal = new Category();
            cuidadoPersonal.setName("Cuidado Personal");

            Category preservativos = new Category();
            preservativos.setName("Preservativos");

            categoryRepository.saveAll(Arrays.asList(spaProducts, cuidadoPersonal, preservativos));
            System.out.println("Categorías creadas exitosamente.");

            // Crear productos
            Product p1 = new Product();
            p1.setName("Jabones Artesanales");
            p1.setDescription("Jabones hechos a mano con ingredientes naturales.");
            p1.setPrice(new BigDecimal("5.50"));
            p1.setPriceDiscount(new BigDecimal("4.99"));
            p1.setStock(100);
            p1.setBrandName("Natural Spa");
            p1.setCategory(spaProducts);

            Product p2 = new Product();
            p2.setName("Loción Corporal Hidratante");
            p2.setDescription("Loción para el cuerpo con aloe vera y vitamina E.");
            p2.setPrice(new BigDecimal("15.00"));
            p2.setPriceDiscount(new BigDecimal("12.50"));
            p2.setStock(80);
            p2.setBrandName("Belleza Natural");
            p2.setCategory(cuidadoPersonal);

            Product p3 = new Product();
            p3.setName("Aceite Esencial de Lavanda");
            p3.setDescription("Aceite esencial 100% puro para aromaterapia.");
            p3.setPrice(new BigDecimal("20.00"));
            p3.setPriceDiscount(new BigDecimal("18.00"));
            p3.setStock(50);
            p3.setBrandName("Aroma Vida");
            p3.setCategory(spaProducts);

            Product p4 = new Product();
            p4.setName("Set de Spa en Casa");
            p4.setDescription("Incluye jabón, loción y aceite esencial para relajación.");
            p4.setPrice(new BigDecimal("40.00"));
            p4.setPriceDiscount(new BigDecimal("35.00"));
            p4.setStock(30);
            p4.setBrandName("Relax Plus");
            p4.setCategory(spaProducts);

            Product p5 = new Product();
            p5.setName("Loción Corporal Revitalizante");
            p5.setDescription("Loción con extracto de pepino y té verde.");
            p5.setPrice(new BigDecimal("17.00"));
            p5.setPriceDiscount(new BigDecimal("14.50"));
            p5.setStock(70);
            p5.setBrandName("Belleza Natural");
            p5.setCategory(cuidadoPersonal);

            Product p6 = new Product();
            p6.setName("Preservativos Clásicos");
            p6.setDescription("Preservativos de látex estándar con lubricación.");
            p6.setPrice(new BigDecimal("10.00"));
            p6.setPriceDiscount(new BigDecimal("9.00"));
            p6.setStock(200);
            p6.setBrandName("SafeLove");
            p6.setCategory(preservativos);

            Product p7 = new Product();
            p7.setName("Preservativos Ultra Finos");
            p7.setDescription("Preservativos ultrafinos para mayor sensibilidad.");
            p7.setPrice(new BigDecimal("12.00"));
            p7.setPriceDiscount(new BigDecimal("10.50"));
            p7.setStock(150);
            p7.setBrandName("SafeLove");
            p7.setCategory(preservativos);

            Product p8 = new Product();
            p8.setName("Aceite Esencial de Eucalipto");
            p8.setDescription("Aceite para aromaterapia con aroma refrescante.");
            p8.setPrice(new BigDecimal("19.00"));
            p8.setPriceDiscount(new BigDecimal("17.00"));
            p8.setStock(60);
            p8.setBrandName("Aroma Vida");
            p8.setCategory(spaProducts);

            Product p9 = new Product();
            p9.setName("Jabones de Lavanda");
            p9.setDescription("Jabones artesanales con aroma a lavanda.");
            p9.setPrice(new BigDecimal("6.00"));
            p9.setPriceDiscount(new BigDecimal("5.50"));
            p9.setStock(90);
            p9.setBrandName("Natural Spa");
            p9.setCategory(spaProducts);

            Product p10 = new Product();
            p10.setName("Set de Cuidado Personal");
            p10.setDescription("Incluye loción y crema corporal hidratante.");
            p10.setPrice(new BigDecimal("30.00"));
            p10.setPriceDiscount(new BigDecimal("27.00"));
            p10.setStock(40);
            p10.setBrandName("Belleza Natural");
            p10.setCategory(cuidadoPersonal);

            productRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10));
            System.out.println("Productos creados exitosamente.");
        } else {
            System.out.println("Las categorías ya existen, no se crearon productos.");
        }
    }
}
