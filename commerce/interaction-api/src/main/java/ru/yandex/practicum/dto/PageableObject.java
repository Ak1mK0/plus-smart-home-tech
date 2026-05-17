package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableObject {
    private int offset;
    private SortObject sort;
    private boolean unpaged;
    private boolean paged;
    private int pageNumber;
    private int pageSize;
}
