package com.ISMM.admin.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ISMM.admin.security.ISMMUserDetails;
import com.ISMM.admin.service.FileUploadUtil;
import com.ISMM.admin.users.UserService;
import com.ISMM.common.domain.User;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired 
	private UserService userService;
	
	//AuthentiationPrincipal security annotation to grab the object representing the current logged
	//	in user
	@GetMapping("")
	public String viewDetails(@AuthenticationPrincipal ISMMUserDetails loggedUser, 
							  ModelMap model) {
		String email = loggedUser.getUsername();
		model.put("user", userService.getByEmail(email));
		return "users/account_form";
	}
	
	
	/**
	 * 
	 * @param user Maps to the form fields of the user_account.html form
	 * @param redirectAttributes Displays the message to the user that the profile has been saved 
	 * @param multipartFile Allows us to upload/update a picture for the user
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/update")
	public String postSaveDetails(	User user, RedirectAttributes redirectAttributes,
									@AuthenticationPrincipal ISMMUserDetails loggedUser,
							    	@RequestParam("image")MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.updateAccount(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			userService.updateAccount(user);		
		}
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
		
		return "redirect:/account";
	}
	
}
