package cristianmartucci.SushiTime_backend.services;

import cristianmartucci.SushiTime_backend.entities.Table;
import cristianmartucci.SushiTime_backend.enums.Role;
import cristianmartucci.SushiTime_backend.enums.TableState;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.exceptions.NotFoundException;
import cristianmartucci.SushiTime_backend.payloads.tables.NewTableDTO;
import cristianmartucci.SushiTime_backend.payloads.tables.NewTableStateResponseDTO;
import cristianmartucci.SushiTime_backend.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public Table save(NewTableDTO body){
        Table table = new Table(body.number());
        return tableRepository.save(table);
    }

    public Table findById(UUID utenteId) {
        return this.tableRepository.findById(utenteId).orElseThrow(() -> new NotFoundException(utenteId));
    }

    public Table changeState(UUID id, NewTableStateResponseDTO body){
        Table table = this.findById(id);
        table.setState(stringToState(body.state()));
        return this.tableRepository.save(table);
    }

    private static TableState stringToState(String state){
        try {
            return TableState.valueOf(state.toUpperCase());
        } catch(IllegalArgumentException error){
            throw new BadRequestException("Stato inserito non corretto! Stati disponibili: AVAILABLE, BUSY");
        }
    }
}
