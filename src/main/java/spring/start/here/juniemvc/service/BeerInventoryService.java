package spring.start.here.juniemvc.service;

import spring.start.here.juniemvc.web.model.BeerInventoryDto;

import java.util.List;
import java.util.Optional;

public interface BeerInventoryService {
    List<BeerInventoryDto> getAll();
    List<BeerInventoryDto> getByBeerId(Integer beerId);
    Optional<BeerInventoryDto> getById(Integer id);
}
