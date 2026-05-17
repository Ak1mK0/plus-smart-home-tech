package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageProductDto {
    private int totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private List<ProductDto> content;
    private int number;
    private SortObject sort;
    private int numberOfElements;
    private PageableObject pageable;
    private boolean empty;
}
