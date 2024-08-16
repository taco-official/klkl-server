package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.dto.response.TagWithSubcategoryResponse;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubcategoryTagService {
	public List<TagWithSubcategoryResponse> getTags(List<Subcategory> subcategoryList) {
		return subcategoryList.stream()
			.map(TagWithSubcategoryResponse::from)
			.toList();
	}
}
