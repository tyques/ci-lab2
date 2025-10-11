package lab.shift.crmsystemforshift.mappers;

import lab.shift.crmsystemforshift.data.dto.response.SellerResponse;

public interface Mapper <A, B>{
    B mapToDto(A a);
    A mapFromDto(B b);
}
