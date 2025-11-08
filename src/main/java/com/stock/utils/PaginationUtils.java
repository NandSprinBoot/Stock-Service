package com.stock.utils;

import com.stock.dto.PageRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class PaginationUtils {

    public static Pageable createPageRequest(PageRequestDto dto) {
        Sort sort = dto.getDirection().equalsIgnoreCase("desc")
                ? Sort.by(dto.getSortBy()).descending()
                : Sort.by(dto.getSortBy()).ascending();

        return PageRequest.of(dto.getPage(), dto.getSize(), sort);
    }
}
