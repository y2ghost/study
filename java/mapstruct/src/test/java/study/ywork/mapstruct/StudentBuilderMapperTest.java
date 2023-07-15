package study.ywork.mapstruct;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import study.ywork.mapstruct.dto.StudentDTO;
import study.ywork.mapstruct.mapper.StudentBuilderMapper;
import study.ywork.mapstruct.model.StudentBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentBuilderMapperTest {
    private StudentBuilderMapper mapper = Mappers.getMapper(StudentBuilderMapper.class);

    @Test
    public void testToEntity() {
        StudentDTO dto = new StudentDTO();
        dto.setName("dto_1");
        dto.setId(1);
        StudentBuilder entity = mapper.toEntity(dto);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getId(), entity.getId());
    }

    @Test
    public void testToDto() {
        StudentBuilder entity = StudentBuilder.builder().id(1).name("dto_1").create();
        StudentDTO dto = mapper.toDto(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getId(), entity.getId());
    }
}
