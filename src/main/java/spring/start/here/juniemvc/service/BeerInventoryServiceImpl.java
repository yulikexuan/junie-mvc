package spring.start.here.juniemvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.repository.BeerInventoryRepository;
import spring.start.here.juniemvc.repository.BeerRepository;
import spring.start.here.juniemvc.web.mappers.BeerInventoryMapper;
import spring.start.here.juniemvc.web.model.BeerInventoryDto;

import java.util.List;
import java.util.Optional;

@Service
class BeerInventoryServiceImpl implements BeerInventoryService {

    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryMapper beerInventoryMapper;

    BeerInventoryServiceImpl(BeerInventoryRepository beerInventoryRepository,
                             BeerRepository beerRepository,
                             BeerInventoryMapper beerInventoryMapper) {
        this.beerInventoryRepository = beerInventoryRepository;
        this.beerRepository = beerRepository;
        this.beerInventoryMapper = beerInventoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerInventoryDto> getAll() {
        return beerInventoryRepository.findAll().stream().map(beerInventoryMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerInventoryDto> getByBeerId(Integer beerId) {
        Optional<Beer> beerOpt = beerRepository.findById(beerId);
        return beerOpt.map(beer -> beerInventoryRepository.findAllByBeer(beer).stream().map(beerInventoryMapper::toDto).toList())
                .orElseGet(List::of);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerInventoryDto> getById(Integer id) {
        return beerInventoryRepository.findById(id).map(beerInventoryMapper::toDto);
    }
}
