package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.Order;
import cristianmartucci.SushiTime_backend.entities.OrderDetail;
import cristianmartucci.SushiTime_backend.entities.Product;
import cristianmartucci.SushiTime_backend.enums.OrderDetailState;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.NewOrderDetailDTO;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.UpdateOrderDetailDTO;
import cristianmartucci.SushiTime_backend.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderDetailsService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    public OrderDetail findById(UUID id){
        return this.orderDetailRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public OrderDetail save(NewOrderDetailDTO body, UUID orderId){
        Order order = this.orderService.findById(orderId);
        Product product = this.productService.findByName(body.productName());
        OrderDetail orderDetail = new OrderDetail(body.quantity() == null ? 1 : body.quantity(), body.price() == null ? product.getPrice() : body.price(), order, product);
        return this.orderDetailRepository.save(orderDetail);
    }

    public Page<OrderDetail> getAllByOrder(UUID orderId, int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.orderDetailRepository.getAllDetailByOrder(orderId, pageable);
    }

    public Page<OrderDetail> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.orderDetailRepository.findAll(pageable);
    }

    public OrderDetail update(UUID id, UpdateOrderDetailDTO body){
        OrderDetail orderDetail = this.findById(id);
        if (body.orderId() != null) orderDetail.setOrder(this.orderService.findById(UUID.fromString(body.orderId())));
        if (body.productName() != null) orderDetail.setProduct(this.productService.findByName(body.productName()));
        if (body.quantity() != null) orderDetail.setQuantity(body.quantity());
        if (body.price() != null) orderDetail.setPrice(body.price());
        if (body.state() != null) orderDetail.setState(stringToState(body.state()));
        return this.orderDetailRepository.save(orderDetail);
    }

    public void delete(UUID id){
        this.orderDetailRepository.delete(this.findById(id));
    }

    private static OrderDetailState stringToState(String state){
        try {
            return OrderDetailState.valueOf(state.toUpperCase());
        }catch (IllegalArgumentException error){
            throw new BadRequestException("Stato inserito non corretto! Stati disponibili: IN_PROGRESS, SERVED, CANCELED");
        }
    }
}
