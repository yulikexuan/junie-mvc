package spring.start.here.juniemvc.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.domain.model.BeerOrderLine;
import spring.start.here.juniemvc.web.model.BeerOrderLineDto;
import spring.start.here.juniemvc.web.model.BeerOrderLineUpsertDto;

@Mapper(componentModel = "spring")
public interface BeerOrderLineMapper {

    @Mapping(target = "beerId", source = "beer.id")
    BeerOrderLineDto toDto(BeerOrderLine entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    @Mapping(target = "beer", source = "beerId", qualifiedByName = "beerFromId")
    @Mapping(target = "quantityAllocated", ignore = true)
    BeerOrderLine toEntity(BeerOrderLineUpsertDto upsert);

    @Named("beerFromId")
    default Beer beerFromId(Integer id) {
        if (id == null) return null;
        Beer b = new Beer();
        b.setId(id);
        return b;
        }
}
