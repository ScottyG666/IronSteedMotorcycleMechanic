package com.ISMM.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ISMM.admin.inventory.InventoryRepository;
import com.ISMM.admin.inventory.InventoryService;
import com.ISMM.common.domain.InventoryItem;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

	@MockBean
	private InventoryRepository catRepo;

	@InjectMocks
	private InventoryService catService;
	
	@Test
	public void testUniqueInNewModelReturnDuplicateName() {
		Integer id = null;
		String name = "UniqueName" ;
		String alias = "abc" ;
		
		InventoryItem cat = new InventoryItem(id, name, alias);
		
		Mockito.when(catRepo.findByName(name)).thenReturn(cat);
		Mockito.when(catRepo.findByAlias(alias)).thenReturn(null);
		
		
		String result = catService.checkUnique(id, name , alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testUniqueInNewModelReturnDuplicateAlias() {
		Integer id = null;
		String name = "NameABC" ;
		String alias = "computers" ;
		
		InventoryItem cat = new InventoryItem(id, name, alias);
		
		Mockito.when(catRepo.findByName(name)).thenReturn(null);
		Mockito.when(catRepo.findByAlias(alias)).thenReturn(cat);
		
		
		String result = catService.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	

	@Test
	public void testUniqueInNewModelReturnDuplicateOK() {
		Integer id = null;
		String name = "NameABC" ;
		String alias = "computers" ;
		
		InventoryItem cat = new InventoryItem(id, name, alias);
		
		Mockito.when(catRepo.findByName(name)).thenReturn(null);
		Mockito.when(catRepo.findByAlias(alias)).thenReturn(null);
		
		
		String result = catService.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");
	}
	
	
	
}
