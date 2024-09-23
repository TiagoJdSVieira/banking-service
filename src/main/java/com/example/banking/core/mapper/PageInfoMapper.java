package com.example.banking.core.mapper;

import com.example.banking.model.PageInfo;

public class PageInfoMapper {
    public static PageInfo toPageInfo(int totalPages, long totalElements, Integer page, Integer size) {
        return PageInfo.builder().page(page).size(size).totalPages(totalPages).totalElements(totalElements).build();
    }
}
