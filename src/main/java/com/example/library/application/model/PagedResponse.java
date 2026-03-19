package com.example.library.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> content;
    private PageInfo page;

    public static <T> PagedResponse<T> from(Page<T> pageResult) {
        return PagedResponse.<T>builder()
                .content(pageResult.getContent())
                .page(PageInfo.from(pageResult))
                .build();
    }
}