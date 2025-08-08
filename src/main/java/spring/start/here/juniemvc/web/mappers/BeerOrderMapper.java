package spring.start.here.juniemvc.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.start.here.juniemvc.domain.model.BeerOrder;
import spring.start.here.juniemvc.web.model.BeerOrderDto;

@Mapper(componentModel = "spring", uses = {BeerOrderLineMapper.class})
public interface BeerOrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerRef", ignore = true) // entity does not contain this field
    @Mapping(target = "orderLines", source = "beerOrderLines")
    BeerOrderDto toDto(BeerOrder entity);
}
