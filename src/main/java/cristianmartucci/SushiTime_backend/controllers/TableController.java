package cristianmartucci.SushiTime_backend.controllers;

import cristianmartucci.SushiTime_backend.entities.Table;
import cristianmartucci.SushiTime_backend.exceptions.BadRequestException;
import cristianmartucci.SushiTime_backend.payloads.tables.NewTableDTO;
import cristianmartucci.SushiTime_backend.payloads.tables.NewTableResponseDTO;
import cristianmartucci.SushiTime_backend.payloads.tables.NewTableStateResponseDTO;
import cristianmartucci.SushiTime_backend.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tables")
public class TableController {
    @Autowired
    private TableService tableService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewTableResponseDTO save(@RequestBody @Validated NewTableDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return new NewTableResponseDTO(this.tableService.save(body).getId());
    }

    @PatchMapping("/{id}/state")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Table changeState(@PathVariable UUID id , @RequestBody @Validated NewTableStateResponseDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return this.tableService.changeState(id, body);
    }

}
