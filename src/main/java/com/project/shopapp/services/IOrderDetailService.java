package com.project.shopapp.services;

import com.project.shopapp.dtos.requests.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(Long id) throws Exception;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;
    void deleteOrderDetail(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
