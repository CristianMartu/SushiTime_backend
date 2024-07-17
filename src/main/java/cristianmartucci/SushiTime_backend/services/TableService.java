package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.Table;
import cristianmartucci.SushiTime_backend.enums.TableState;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.tables.NewTableDTO;
import cristianmartucci.SushiTime_backend.payloads.tables.TableStateResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.tables.UpdateTableDTO;
import cristianmartucci.SushiTime_backend.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public Table save(NewTableDTO body){
        if (this.tableRepository.findByNumber(body.number()).isPresent()){
            throw new BadRequestException("Numero tavolo già presente");
        }
        Table table = new Table(body.number(), body.maxCapacity());
        return tableRepository.save(table);
    }

    public Page<Table> getAll(int pageNumber, int pageSize, String sortBy){
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return this.tableRepository.findAll(pageable);
    }

    public Table findById(UUID id) {
        return this.tableRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Table changeState(UUID id, TableStateResponseDTO body){
        Table table = this.findById(id);
        table.setState(stringToState(body.state()));
        return this.tableRepository.save(table);
    }

    public Table updateNumber(UUID id, UpdateTableDTO body){
        Table table = this.findById(id);
        if (body.number() != null) table.setNumber(body.number());
        if (body.maxCapacity() != null) table.setMaxCapacity(body.maxCapacity());
        if (body.currentPeople() != null) {
            if (body.currentPeople() > table.getMaxCapacity()){
                throw new BadRequestException("Il tavolo può avere al massimo " + table.getMaxCapacity() + " persone");
            }
            table.setCurrentPeople(body.currentPeople());
        }
        return this.tableRepository.save(table);
    }

    public void delete(UUID id){
        this.tableRepository.delete(this.findById(id));
    }

    private static TableState stringToState(String state){
        try {
            return TableState.valueOf(state.toUpperCase());
        } catch(IllegalArgumentException error){
            throw new BadRequestException("Stato inserito non corretto! Stati disponibili: OCCUPIED, AVAILABLE, RESERVED, OUT_OF_SERVICE");
        }
    }
}
