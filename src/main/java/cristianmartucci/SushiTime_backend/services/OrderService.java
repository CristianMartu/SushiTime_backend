package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.Order;
import cristianmartucci.SushiTime_backend.entities.Table;
import cristianmartucci.SushiTime_backend.enums.OrderState;
import cristianmartucci.SushiTime_backend.enums.TableState;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.orders.NewOrderDTO;
import cristianmartucci.SushiTime_backend.payloads.orders.OrderStateResponseDTO;
import cristianmartucci.SushiTime_backend.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableService tableService;

    public Order findById(UUID id){
        return this.orderRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Order save(NewOrderDTO body){
        Table table = this.tableService.findById(body.tableId());
        if (table.getState() == TableState.AVAILABLE){
            table.setState(TableState.OCCUPIED);
        }else{
            throw new BadRequestException("Tavolo non disponibile, stato attuale: " + table.getState());
        }
        Order order = new Order(table);
        return this.orderRepository.save(order);
    }

    public Page<Order> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.orderRepository.findAll(pageable);
    }

    public Order getOrder(UUID id){
        return this.findById(id);
    }

    public Order changeState(UUID id, OrderStateResponseDTO body){
        Order order = this.findById(id);
        order.setState(stringToState(body.state()));
        return this.orderRepository.save(order);
    }

    public void delete(UUID id){
        this.orderRepository.delete(this.findById(id));
    }

    private static OrderState stringToState(String state){
        try {
            return OrderState.valueOf(state.toUpperCase());
        } catch(IllegalArgumentException error){
            throw new BadRequestException("Stato inserito non corretto! Stati disponibili: IN_PROGRESS, SERVED, CANCELED");
        }
    }
}
