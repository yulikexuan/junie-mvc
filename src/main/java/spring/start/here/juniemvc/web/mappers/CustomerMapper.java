package spring.start.here.juniemvc.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.start.here.juniemvc.domain.model.Customer;
import spring.start.here.juniemvc.web.model.CustomerDto;
import spring.start.here.juniemvc.web.model.CustomerUpsertDto;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "beerOrders", ignore = true)
    Customer toEntity(CustomerUpsertDto upsert);
}
