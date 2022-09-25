<<<<<<<< HEAD:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/categories/CategoriesController.java
package com.ISMM.admin.categories;
========
package com.ISMM.admin.inventory;
>>>>>>>> main:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/inventory/InventoryController.java

import java.io.IOException;
import java.util.List;

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

<<<<<<<< HEAD:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/categories/CategoriesController.java
import com.ISMM.admin.exceptions.CategoryNotFoundException;
========
import com.ISMM.admin.exceptions.InventoryItemNotFoundException;
>>>>>>>> main:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/inventory/InventoryController.java
import com.ISMM.admin.service.FileUploadUtil;
import com.ISMM.admin.service.InventoryPageInfo;
import com.ISMM.common.domain.InventoryItem;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired 
	InventoryService invService;

	@GetMapping("")
	public String listFirstPage(@Param("sortDir") String sortDir, ModelMap model) {
<<<<<<<< HEAD:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/categories/CategoriesController.java
		return listByPage(1, sortDir, null, model);
========
		return listByPage(1, sortDir, null,  model);
>>>>>>>> main:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/inventory/InventoryController.java
	}
	
	@GetMapping("/page/{pageNum}") 
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, 
<<<<<<<< HEAD:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/categories/CategoriesController.java
							 @Param("sortDir") String sortDir, String keyword,
							 ModelMap model) {
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = catService.listByPage(pageInfo, pageNum, sortDir, keyword);
		
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		
========
							@Param("sortDir") String sortDir,
							@Param("keyword") String keyword,
							ModelMap model) {
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		
		InventoryPageInfo pageInfo = new InventoryPageInfo();
		List<InventoryItem> listInventoryItems = invService.listByPage(pageInfo, pageNum, sortDir, keyword);

>>>>>>>> main:ISMMProject/ISMMWebParent/ISMMBackend/src/main/java/com/ISMM/admin/inventory/InventoryController.java
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.put("totalPages", pageInfo.getTotalPages());
		model.put("totalItems", pageInfo.getTotalElements());
		model.put("currentPage", pageNum);
		model.put("sortField", "name");
		model.put("sortDir", sortDir);
		model.put("keyword", keyword);
		model.put("startCount", startCount);
		model.put("endCount", endCount);

		model.put("listInventoryItems", listInventoryItems);
		model.put("reverseSortDir", reverseSortDir);

		return "inventory/inventory_list";		
	}
	
	
	@GetMapping("/new") 
	public String newCategory (ModelMap model) {

		model.put("inventoryItem", new InventoryItem());
		
		List<InventoryItem> listInventoryItems = invService.listInventoryItemsUsedInForm();
		model.put("listInventoryItems", listInventoryItems);

		model.put("pageTitle", "Create New Inventory Item");
		
		return "inventory/inventory_item_form";
	}
	
	@PostMapping("/save")
	public String saveCategory(	InventoryItem inventoryItem, @RequestParam("fileImage") MultipartFile multipartFile,
								RedirectAttributes rA) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			inventoryItem.setImage(fileName);

			InventoryItem savedInvItem = invService.save(inventoryItem);
			String uploadDir = "../inventory-images/" + savedInvItem.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			invService.save(inventoryItem);
		}	
		rA.addFlashAttribute("message", "The inventory item has been saved successfully!");
		return "redirect:/inventory";
	}
	
	@GetMapping("/edit/{id}")
	public String editInventoryItem(@PathVariable(name = "id") Integer id, ModelMap model,
			RedirectAttributes rA) {
		try {
			InventoryItem invItem = invService.get(id);
			List<InventoryItem> listInventoryItems = invService.listInventoryItemsUsedInForm();

			model.put("inventoryItem", invItem);
			model.put("listInventoryItems", listInventoryItems);
			model.put("pageTitle", "Edit Inventory Item (ID: " + id + ")");

			return "inventory/inventory_item_form";			
		} catch (InventoryItemNotFoundException ex) {
			rA.addFlashAttribute("message", ex.getMessage());
			return "redirect:/inventory";
		}
	}
	
	@GetMapping("/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		invService.updateInventoryItemEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/inventory";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		try {
			invService.delete(id);
			String inventoryItemDir = "../inventory-images/" + id;
			FileUploadUtil.removeDir(inventoryItemDir);

			redirectAttributes.addFlashAttribute("message", 
					"The item ID " + id + " has been deleted successfully");
		} catch (InventoryItemNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/inventory";
	}	
	
	
	@PostMapping("/check_unique")
	@ResponseBody
	public String checkUnique(	@Param("id") Integer id, @Param("name") String name,
								@Param("alias") String alias) {
		return invService.checkUnique(id, name, alias);
	}

}
