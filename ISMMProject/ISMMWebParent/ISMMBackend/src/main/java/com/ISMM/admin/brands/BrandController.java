package com.ISMM.admin.brands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ISMM.common.domain.Brand;

@Controller
@RequestMapping("/brands")
public class BrandController {

	@Autowired
	private BrandService brandService;

	@GetMapping("")
	public String listAll(ModelMap model) {
		List<Brand> listBrands = brandService.listAll();
		model.put("listBrands", listBrands);

		return "brands/brands";
	}
}