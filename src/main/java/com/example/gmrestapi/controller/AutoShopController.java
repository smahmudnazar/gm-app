package com.example.gmrestapi.controller;

import com.example.gmrestapi.dto.ApiResponse;
import com.example.gmrestapi.dto.AutoShopDTO;
import com.example.gmrestapi.entity.AutoShop;
import com.example.gmrestapi.projection.AutoShopProjection;
import com.example.gmrestapi.repository.AutoshopRepository;
import com.example.gmrestapi.repository.CarRepository;
import com.example.gmrestapi.service.AutoShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/autoshop")
@RequiredArgsConstructor
public class AutoShopController {

    final AutoshopRepository autoshopRepository;
    final CarRepository carRepository;
    final AutoShopService autoshopService;

    //getAll
    @GetMapping
    public HttpEntity<?> getAll() {
        List<AutoShop> all = autoshopRepository.findAllByActiveTrue();
        return ResponseEntity.ok().body(all);

    }

    //getOne
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id) {
        Optional<AutoShop> byId = autoshopRepository.findById(id);

        return ResponseEntity.status(byId.isEmpty() ?
                HttpStatus.NOT_FOUND : HttpStatus.OK).body(byId.orElse(new AutoShop()));
    }

    //add
    @PostMapping
    public HttpEntity<?> add(@RequestBody AutoShopDTO dto) {
        ApiResponse response = autoshopService.add(dto);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    //edit
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id, @RequestBody AutoShopDTO autoshopdto) {
        ApiResponse response = autoshopService.edit(id, autoshopdto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    //delete
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        //really delete
//        if (autoshopRepository.existsById(id)) {
//            autoshopRepository.deleteById(id);
//            return ResponseEntity.ok().body("Deleted!");
//        } else {
//            return ResponseEntity.status(404).body("AutoShop NOT found!");
//        }
        Optional<AutoShop> byId = autoshopRepository.findById(id);
        if (byId.isEmpty()) return ResponseEntity.status(404).body("NOT FOUND");
        AutoShop autoshop = byId.get();
        autoshop.setActive(false);
        autoshopRepository.save(autoshop);
//        return ResponseEntity.status(204).body("DELETED!");
        return ResponseEntity.ok().body("DELETED!");
    }

    //AutoShopsByGMid
    @GetMapping("/byGmId/{id}")
    public HttpEntity<?> getAllByGm(@PathVariable UUID id) {
        return ResponseEntity.ok().body(autoshopRepository.findAllByGm_Id(id));
    }

    @GetMapping("/forClient")
    public HttpEntity<?> getAllForClient() {
        ApiResponse response = autoshopService.getAll();

        return ResponseEntity.ok().body(response);
    }




}
