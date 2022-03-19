package com.example.gmrestapi.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface AutoShopProjection {

    @Value("#{target.address.city + ' '  + target.address.street + ' ' + target.address.home}")
    String getAddress();

    @Value("#{target.gm.corpName}")
    String getGM();

//    @Value("#{autoShopService.cartocarname(target.carList)}")
//    List<String> getCars();

}
