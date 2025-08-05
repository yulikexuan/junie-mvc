package spring.start.here.juniemvc.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

@Mapper(componentModel = "spring")
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerUpsertDtoToBeer(BeerUpsertDto beerUpsertDto);
}