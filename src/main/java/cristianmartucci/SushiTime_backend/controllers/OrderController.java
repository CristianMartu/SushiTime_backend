package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.entities.Order;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.orders.NewOrderDTO;
import cristianmartucci.SushiTime_backend.payloads.orders.NewOrderResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.orders.OrderStateResponseDTO;
import cristianmartucci.SushiTime_backend.services.OrderService;
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
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public NewOrderResponseDTO save(@RequestBody @Validated NewOrderDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new NewOrderResponseDTO(this.orderService.save(body).getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<Order> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "table.number") String sortBy){
        return this.orderService.getAll(page, size, sortBy);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Order getOrder(@PathVariable UUID orderId){
        return this.orderService.getOrder(orderId);
    }

    @PutMapping("/{orderId}/state")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Order changeState(@PathVariable UUID orderId, @RequestBody @Validated OrderStateResponseDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return this.orderService.changeState(orderId, body);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public void delete(@PathVariable UUID orderId){
        this.orderService.delete(orderId);
    }
}
