package com.project.shopapp.services;

import com.project.shopapp.dtos.requests.CartItemDTO;
import com.project.shopapp.dtos.requests.OrderDTO;
import com.project.shopapp.dtos.responses.OrderResponse;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.*;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        // convert orderDTO => order (bỏ qua id)
        Order order = new Order();
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        // Kiểm tra shipping date phải >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);

        // Tao danh sach cac doi tuong OrderDetail tu cartItems
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO: orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            // Lay thong tin san pham tu cartItemDTO
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            // check product ton tai khong
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new DataNotFoundException("Product not found with id: " + productId)
            );

            // Dat thong tin cho OrderDetail
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotalMoney(orderDetail.getPrice() * orderDetail.getNumberOfProducts());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public OrderResponse getOrder(Long id) throws DataNotFoundException {
        return modelMapper.map(
                orderRepository.findById(id).orElseThrow(
                        () -> new DataNotFoundException("Cannot find order with id: " + id)
                ),
                OrderResponse.class
        );
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot find order with id: " + id)
        );
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId())
        );
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        return modelMapper.map(orderRepository.save(order), OrderResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot find order with id: " + id)
        );
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrderByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
