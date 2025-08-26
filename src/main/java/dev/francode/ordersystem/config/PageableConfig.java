package dev.francode.ordersystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PageableConfig implements WebMvcConfigurer {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_PAGE_NUMBER = 10000; // Máximo 10,000 páginas

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(pageableResolver());
    }

    @Bean
    public PageableHandlerMethodArgumentResolver pageableResolver() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver() {
            @Override
            public Pageable resolveArgument(MethodParameter methodParameter,
                                            ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest,
                                            WebDataBinderFactory binderFactory) {

                Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
                return validatePageable(pageable);
            }
        };

        resolver.setMaxPageSize(MAX_PAGE_SIZE);
        resolver.setOneIndexedParameters(false);
        return resolver;
    }

    private Pageable validatePageable(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        // Validar número de página
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number must not be less than zero");
        }

        if (pageNumber > MAX_PAGE_NUMBER) {
            throw new IllegalArgumentException("Page number must not be greater than " + MAX_PAGE_NUMBER);
        }

        // Validar tamaño de página
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }

        if (pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must not be greater than " + MAX_PAGE_SIZE);
        }

        // Validar que el offset no exceda Integer.MAX_VALUE
        long offset = (long) pageNumber * (long) pageSize;
        if (offset > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Page offset must not exceed Integer.MAX_VALUE (" + Integer.MAX_VALUE + ")");
        }

        return pageable;
    }
}