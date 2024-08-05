package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.entities.Order;
import cristianmartucci.SushiTime_backend.entities.OrderDetail;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.NewOrderDetailDTO;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.NewOrderDetailResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.orderDetails.UpdateOrderDetailDTO;
import cristianmartucci.SushiTime_backend.services.OrderDetailsService;
import cristianmartucci.SushiTime_backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderDetailController {
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/{orderId}/details")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public List<NewOrderDetailResponseDTO> save(@PathVariable UUID orderId, @RequestBody @Validated List<NewOrderDetailDTO> bodyList, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        LocalDateTime time = LocalDateTime.now();
        Order order = this.orderService.findById(orderId);
        if (!Objects.equals(order.getState().toString(), "IN_PROGRESS")){
            throw new BadRequestException("Ordine non disponibile per le ordinazioni, stato attuale: " + order.getState());
        }
        int maxProductPerPerson = 6;
        int maxProductPerTable = order.getTable().getCurrentPeople() * maxProductPerPerson;
        int tableProducts = bodyList.stream().mapToInt(NewOrderDetailDTO::quantity).sum();
        Optional<OrderDetail> lastOrder = this.orderDetailsService.getAllByOrder(orderId, 0, 10, "orderTime").stream().findFirst();
        if (lastOrder.isPresent()){
            Duration duration = Duration.between(lastOrder.get().getOrderTime(), time);
            if (duration.toMinutes() < 10){
                throw new IllegalArgumentException("È necessario attendere " + (10 - duration.toMinutes()) +" minuti per il prossimo ordine.");
            }
        }
        if (tableProducts > maxProductPerTable){
            throw new IllegalArgumentException("Non si possono ordinare più di " + maxProductPerTable + " prodotti per il numero corrente di persone.");
        }
        return bodyList.stream().map(body -> new NewOrderDetailResponseDTO(this.orderDetailsService.save(body, order, time).getId())).toList();
    }

    @GetMapping("{orderId}/details")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<OrderDetail> getAllByOrder(@PathVariable UUID orderId ,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "orderTime") String sortBy){
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
