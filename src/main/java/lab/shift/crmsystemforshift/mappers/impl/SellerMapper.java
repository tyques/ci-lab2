package lab.shift.crmsystemforshift.mappers.impl;

import lab.shift.crmsystemforshift.data.dto.requests.SellerCreateRequest;
import lab.shift.crmsystemforshift.data.dto.response.SellerResponse;
import lab.shift.crmsystemforshift.data.entities.SellerEntity;
import lab.shift.crmsystemforshift.mappers.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerMapper implements Mapper<SellerEntity, SellerResponse> {
    private final ModelMapper modelMapper;

    @Override
    public SellerResponse mapToDto(SellerEntity entity) {
        return modelMapper.map(entity, SellerResponse.class);
    }

    @Override
    public SellerEntity mapFromDto(SellerResponse request) {
        return modelMapper.map(request, SellerEntity.class);
    }
}
