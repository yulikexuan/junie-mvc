package spring.start.here.juniemvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.repository.BeerRepository;
import spring.start.here.juniemvc.web.mappers.BeerMapper;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of BeerService interface
 */
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    @Transactional
    public BeerDto saveBeer(BeerUpsertDto beerUpsertDto) {
        Beer beer = beerMapper.beerUpsertDtoToBeer(beerUpsertDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BeerListDto getAllBeers(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber != null ? pageNumber : 0,
                pageSize != null ? pageSize : 25);

        Page<Beer> beerPage = beerRepository.findAll(pageRequest);

        return BeerListDto.builder()
                .beers(beerPage.getContent().stream()
                        .map(beerMapper::beerToBeerDto)
                        .collect(Collectors.toList()))
                .totalPages(beerPage.getTotalPages())
                .currentPage(beerPage.getNumber())
                .totalElements(beerPage.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public Optional<BeerDto> updateBeer(Integer id, BeerUpsertDto beerUpsertDto) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    // Update the existing beer with new values
                    existingBeer.setBeerName(beerUpsertDto.getBeerName());
                    existingBeer.setBeerStyle(beerUpsertDto.getBeerStyle());
                    existingBeer.setUpc(beerUpsertDto.getUpc());
                    existingBeer.setPrice(beerUpsertDto.getPrice());
                    existingBeer.setQuantityOnHand(beerUpsertDto.getQuantityOnHand());

                    // Save the updated beer
                    Beer savedBeer = beerRepository.save(existingBeer);
                    return beerMapper.beerToBeerDto(savedBeer);
                });
    }

    @Override
    @Transactional
    public boolean deleteBeer(Integer id) {
        return beerRepository.findById(id)
                .map(beer -> {
                    beerRepository.delete(beer);
                    return true;
                })
                .orElse(false);
    }
}