package study.ywork.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import study.ywork.mapstruct.dto.AddressDTO;
import study.ywork.mapstruct.dto.StudentDTO;
import study.ywork.mapstruct.model.DeliveryAddress;

@Mapper
public interface DeliveryAddressMapper {
    @Mapping(source = "student.name", target = "name")
    @Mapping(source = "address.houseNo", target = "houseNumber")
    DeliveryAddress toEntity(StudentDTO student, AddressDTO address);
}
