package com.ISMM.admin.brands;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ISMM.common.domain.Brand;

@Repository
public interface BrandRepository  extends PagingAndSortingRepository<Brand, Integer>{

}
