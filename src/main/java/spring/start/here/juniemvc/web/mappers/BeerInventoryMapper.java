package spring.start.here.juniemvc.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.domain.model.BeerInventory;
import spring.start.here.juniemvc.web.model.BeerInventoryDto;

@Mapper(componentModel = "spring")
public interface BeerInventoryMapper {

    @Mapping(target = "beerId", source = "beer.id")
    BeerInventoryDto toDto(BeerInventory entity);

    @Mapping(target = "beer", expression = "java(beerFromId(dto.beerId()))")
    @Mapping(target = "id", source = "id")
    BeerInventory toEntity(BeerInventoryDto dto);

    default Beer beerFromId(Integer beerId) {
        if (beerId == null) return null;
        Beer beer = new Beer();
        beer.setId(beerId);
        return beer;
    }
}
