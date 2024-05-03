package study.ywork.mapstruct;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import study.ywork.mapstruct.dto.AddressDTO;
import study.ywork.mapstruct.dto.StudentDTO;
import study.ywork.mapstruct.mapper.DeliveryAddressMapper;
import study.ywork.mapstruct.model.DeliveryAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeliveryAddressMapperTest {
    private DeliveryAddressMapper mapper = Mappers.getMapper(DeliveryAddressMapper.class);

    @Test
    public void testToEntity() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setClassVal("A");
        studentDTO.setName("dto_1");
        studentDTO.setId(1);
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("Y");
        addressDTO.setState("Z");
        addressDTO.setHouseNo(1);
        DeliveryAddress deliveryAddress = mapper.toEntity(studentDTO, addressDTO);
        assertEquals(deliveryAddress.getName(), studentDTO.getName());
        assertEquals(deliveryAddress.getCity(), addressDTO.getCity());
        assertEquals(deliveryAddress.getState(), addressDTO.getState());
        assertEquals(deliveryAddress.getHouseNumber(), addressDTO.getHouseNo());
    }
}
