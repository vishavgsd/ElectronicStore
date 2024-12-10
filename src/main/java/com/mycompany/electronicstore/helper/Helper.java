package com.mycompany.electronicstore.helper;

import com.mycompany.electronicstore.dtos.PageableResponse;
import com.mycompany.electronicstore.dtos.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <E,D> PageableResponse<D> getPageableResponse(Page<E> page, Class<D> type){
        List<E> entity = page.getContent();
        List<D> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
        PageableResponse<D> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(dtoList);
        pageableResponse.setPageNumber(page.getNumber());
        pageableResponse.setPageSize(page.getSize());
        pageableResponse.setTotalElements(page.getTotalElements());
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setLastPage(page.isLast());
        return pageableResponse;
    }
}
