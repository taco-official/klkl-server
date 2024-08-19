package taco.klkl.domain.category.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.SubcategoryTagRepository;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.dto.response.TagResponse;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubcategoryTagServiceImpl implements SubcategoryTagService {

	private final SubcategoryTagRepository subcategoryTagRepository;

	@Override
	public Set<TagResponse> findTagsBySubcategoryList(
		final List<Subcategory> subcategoryList
	) {
		return subcategoryList.stream()
			.map(subcategoryTagRepository::findAllBySubcategory)
			.flatMap(Collection::stream)
			.map(SubcategoryTag::getTag)
			.map(TagResponse::from)
			.collect(Collectors.toSet());
	}
}
