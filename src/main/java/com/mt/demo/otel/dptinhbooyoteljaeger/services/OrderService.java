package com.mt.demo.otel.dptinhbooyoteljaeger.services;

import com.mt.demo.otel.dptinhbooyoteljaeger.payloads.Order;

public interface OrderService {

    Order createOrder(int id, String name);
}
