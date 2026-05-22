package ru.yandex.practicum.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.model.Address;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    AddressDto toDto(Address address);

}
