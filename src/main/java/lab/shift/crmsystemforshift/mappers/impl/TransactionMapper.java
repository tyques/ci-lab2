package lab.shift.crmsystemforshift.mappers.impl;

import lab.shift.crmsystemforshift.data.dto.response.TransactionResponse;
import lab.shift.crmsystemforshift.data.entities.TransactionEntity;
import lab.shift.crmsystemforshift.mappers.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper implements Mapper<TransactionEntity, TransactionResponse> {
    private final ModelMapper modelMapper;

    @Override
    public TransactionResponse mapToDto(TransactionEntity entity) {
        return modelMapper.map(entity, TransactionResponse.class);    }

    @Override
    public TransactionEntity mapFromDto(TransactionResponse request) {
        return modelMapper.map(request, TransactionEntity.class);
    }
}
