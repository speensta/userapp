package com.userapp.service;


import com.userapp.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="order")
public interface OrderRestApiClient {

    @GetMapping("/order-service/{userid}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userid);

}
