package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.entities.Order;
import cristianmartucci.SushiTime_backend.entities.OrderDetail;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.NewOrderDetailDTO;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.NewOrderDetailResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.UpdateOrderDetailDTO;
import cristianmartucci.SushiTime_backend.payloads.orders.OrderStateResponseDTO;
import cristianmartucci.SushiTime_backend.services.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderDetailController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @PostMapping("/{orderId}/details")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public NewOrderDetailResponseDTO save(@PathVariable UUID orderId, @RequestBody @Validated NewOrderDetailDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new NewOrderDetailResponseDTO(this.orderDetailsService.save(body, orderId).getId());
    }

    @GetMapping("{orderId}/details")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<OrderDetail> getAll(@PathVariable UUID orderId ,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "state") String sortBy){
        return this.orderDetailsService.getAllByOrder(orderId, page, size, sortBy);
    }

    @GetMapping("/details")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<OrderDetail> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "state") String sortBy){
        return this.orderDetailsService.getAll(page, size, sortBy);
    }

    @GetMapping("/order/{orderDetailId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public OrderDetail getOrderDetail(@PathVariable UUID orderDetailId){
        return this.orderDetailsService.findById(orderDetailId);
    }

    @PutMapping("/order/{orderDetailId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public OrderDetail update(@PathVariable UUID orderDetailId, @RequestBody @Validated UpdateOrderDetailDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return this.orderDetailsService.update(orderDetailId, body);
    }

    @DeleteMapping("/order/{orderDetailId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public void delete(@PathVariable UUID orderDetailId){
        this.orderDetailsService.delete(orderDetailId);
    }
}
