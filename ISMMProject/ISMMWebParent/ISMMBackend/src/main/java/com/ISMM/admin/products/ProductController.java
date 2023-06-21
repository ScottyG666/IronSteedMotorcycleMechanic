package com.ISMM.admin.products;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ISMM.admin.brands.BrandService;
import com.ISMM.admin.service.FileUploadUtil;
import com.ISMM.common.domain.Brand;
import com.ISMM.common.domain.Product;
import com.ISMM.common.domain.ProductImage;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	
	@Autowired
	private ProductService prodService;
	@Autowired
	private BrandService brandService;
	
	/**
	 * The Product listing page. Provides concise information about products, with buttons for quick access 
	 * 	to enable/disable a product, editing a product, and deleting a product.
	 */
	@GetMapping("")
	public String listAll(ModelMap model) {
		List<Product> listProducts = prodService.listAll();

		model.put("listProducts", listProducts);

		return "products/products";
	}
	
	/**
	 * Controller mapping for creating a new Product object. The object is added to the model so that
	 * 	it's field's can be edited depending on the inputs.
	 */
	@GetMapping("/new")
	public String newProduct(ModelMap model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.put("product", product);
		model.put("listBrands", listBrands);
		model.put("pageTitle", "Create New Product");

		return "products/product_form";
	}

	
	/**
	 * When the New / Edited Product passes the uniqueness validation, the form is submitted
	 * 	to this mapping where the product passed is added to the database. A confirmation 
	 * 	message is added to the model for user experience.
	 */
	@PostMapping("/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile mainImageMultipart,			
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts, 
			@RequestParam(name = "detailNames", required=false) String[] detailNames,
			@RequestParam(name = "detailValues" , required=false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames
			) 
					throws IOException {

		setMainImageName(mainImageMultipart, product);
		setExistingExtraImageNames(imageIDs, imageNames, product);
		setNewExtraImageNames(extraImageMultiparts, product);
		setProductDetails(detailIDs, detailNames, detailValues, product);
		
		Product savedProduct = prodService.save(product);

		deleteExtraImagesWeredRemovedOnForm(product);
		
		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		
		ra.addFlashAttribute("message", "The product has been saved successfully.");
		
		return "redirect:/products";
	}
	
	private void deleteExtraImagesWeredRemovedOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();
				
				if (!product.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + filename);
						
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + filename);
					}
				}
				
			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
			Product product) {
		if (imageIDs == null || imageIDs.length == 0) return;
		
		Set<ProductImage> images = new HashSet<>();
		
		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			
			images.add(new ProductImage(id, name, product));
		}
		
		product.setImages(images);
		
	}
	
	private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0) return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];

			if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
	}

	
	private void saveUploadedImages(MultipartFile mainImageMultipart, 
			MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);		
		}
		
		if (extraImageMultiparts.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
		
	}

	private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					product.addExtraImage(fileName);
				}
			}
		}
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
	
	/**
	 * Used when the Product form is submitted for New / Editing Products. 
	 */
	@PostMapping("/check_unique")
	@ResponseBody
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return prodService.checkUnique(id, name);
	}	

	
	/**
	 * When a request is sent to this mapping, checks for the designated Product actually exists. Then reverses 
	 * 	the enabled status of the Product found.
	 */
	@GetMapping("/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		prodService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}
	
	
	/**
	 * Deletes a product by a specified ID, which is passed along in the URL when the get request is sent. If
	 * 	the ID passed does not exist within the database, the user is alerted with a friendly custom message 
	 * 	from the Product Not Found Exception.
	 * 
	 * This method also cleans the Product images directory of previously uploaded images. Once deleted images
	 * 	are removed permanently.
	 */
	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, 
			ModelMap model, RedirectAttributes redirectAttributes) {
		try {
			prodService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;
			
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message", 
					"The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/products";
	}	
	
	
	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, ModelMap model,
			RedirectAttributes ra) {
		try {
			Product product = prodService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);


			return "products/product_form";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/products";
		}
	}
	
}