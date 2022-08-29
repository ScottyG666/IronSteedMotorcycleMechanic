package com.ISMM.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/*
		 * Exposes the absolute path *user-photos* directory under the Root of the ISMMBackend
		 * 	to be accessible client side
		 */
		String dirName = "user-photos";
		
		Path userPhotosDir = Paths.get(dirName);
		
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + dirName + "/**")
				.addResourceLocations("file:/" + userPhotosPath + "/");

		
		
		String categoryImagesDirName = "../category-images";
		Path categoryImagesDir = Paths.get(categoryImagesDirName);

		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();

		registry.addResourceHandler("/category-images/**")
			.addResourceLocations("file:/" + categoryImagesPath + "/");
	}
	

}
