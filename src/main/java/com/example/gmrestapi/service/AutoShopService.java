package com.example.gmrestapi.service;

import com.example.gmrestapi.dto.ApiResponse;
import com.example.gmrestapi.dto.AutoShopDTO;
import com.example.gmrestapi.dto.DTOClientAutoShop;
import com.example.gmrestapi.entity.Address;
import com.example.gmrestapi.entity.AutoShop;
import com.example.gmrestapi.entity.Car;
import com.example.gmrestapi.entity.GM;
import com.example.gmrestapi.repository.AddressRepository;
import com.example.gmrestapi.repository.AutoshopRepository;
import com.example.gmrestapi.repository.CarRepository;
import com.example.gmrestapi.repository.GMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutoShopService {

    final AddressRepository addressRepository;
    final AutoshopRepository autoshopRepository;
    final GMRepository gmRepository;
    final CarRepository carRepository;


    public ApiResponse add(AutoShopDTO dto) {
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setHome(dto.getHome());
        address.setStreet(dto.getStreet());
        Address save = addressRepository.save(address);

        AutoShop autoShop = new AutoShop();

        Optional<GM> optionalGM = gmRepository.findById(dto.getGmId());
        if (optionalGM.isEmpty()) return new ApiResponse("Not found!", false);

        autoShop.setGm(optionalGM.get());
        autoShop.setName(dto.getName());

        List<Car> allById = carRepository.findAllById(dto.getCarIds());

        autoShop.setCarList(allById);
        autoShop.setAddress(save);
        autoshopRepository.save(autoShop);
        return new ApiResponse("Added", true);
    }

    public ApiResponse edit(Integer id, AutoShopDTO dto) {

        Optional<AutoShop> byId = autoshopRepository.findById(id);
        if (byId.isEmpty()) return new ApiResponse("AutoShop Not found!", false);

        AutoShop autoShop = byId.get();

        Address gmAddress = autoShop.getAddress();
        gmAddress.setStreet(dto.getStreet());
        gmAddress.setHome(dto.getHome());
        gmAddress.setCity(dto.getCity());

        autoShop.setAddress(gmAddress);
        autoShop.setName(dto.getName());

        Optional<GM> optionalGM = gmRepository.findById(dto.getGmId());
        autoShop.setGm(optionalGM.get());

        List<Car> allById = carRepository.findAllById(dto.getCarIds());
        autoShop.setCarList(allById);

        autoshopRepository.save(autoShop);
        return new ApiResponse("Edited", true);
    }

    public ApiResponse getAll() {
        List<AutoShop> autoShops=autoshopRepository.findAllByActiveTrue();

        List<AutoShopDTO> autoShopDTOS=new ArrayList<>();

        for (AutoShop autoShop : autoShops) {
            autoShopDTOS.add(autoShoptoDto(autoShop));
        }


        List<DTOClientAutoShop> dtoClientShops=new ArrayList<>();
        for (AutoShopDTO autoShopDTO : autoShopDTOS) {
            dtoClientShops.add(dtoClientShop(autoShopDTO));
        }

        return new ApiResponse("Mana", true, dtoClientShops);


//        List<AutoShopProjection> allByActiveTrue = autoshopRepository.findAllByActiveTrue();
//        return new ApiResponse("Mana", true, allByActiveTrue);
    }


    public DTOClientAutoShop dtoClientShop(AutoShopDTO autoShopDTO){
      DTOClientAutoShop dtoClientShop=new DTOClientAutoShop();

      dtoClientShop.setCity(autoShopDTO.getCity());
      dtoClientShop.setHome(autoShopDTO.getHome());
      dtoClientShop.setStreet(autoShopDTO.getStreet());
      dtoClientShop.setName(autoShopDTO.getName());
      dtoClientShop.setGmName(gmRepository.getById(autoShopDTO.getGmId()).getCorpName());

      List<String> carName =new ArrayList<>();
        for (Car car : carRepository.findAllById(autoShopDTO.getCarIds())) {
            carName.add(car.getModel());
        }


      dtoClientShop.setCarNames(carName);

        return dtoClientShop;
    }

    public static AutoShopDTO autoShoptoDto(AutoShop autoShop){
        AutoShopDTO autoShopDTO=new AutoShopDTO();

        autoShopDTO.setCity(autoShop.getAddress().getCity());
        autoShopDTO.setName(autoShop.getName());
        autoShopDTO.setStreet(autoShop.getAddress().getStreet());
        autoShopDTO.setGmId(autoShop.getGm().getId());
        autoShopDTO.setHome(autoShop.getAddress().getHome());

        List<UUID> carIds = new ArrayList<>();
        for (Car car : autoShop.getCarList()) {
            carIds.add(car.getId());
        }

        autoShopDTO.setCarIds(carIds);

        return autoShopDTO;
    }


    public static List<String> cartocarname(List<Car> carList){
        List<String> carName=new ArrayList<>();
        for (Car car : carList) {
            carName.add(car.getModel());
        }

        return carName;
    }
}
