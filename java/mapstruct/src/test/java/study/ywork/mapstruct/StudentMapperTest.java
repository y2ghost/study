package study.ywork.mapstruct;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import study.ywork.mapstruct.dto.StudentDTO;
import study.ywork.mapstruct.mapper.StudentMapper;
import study.ywork.mapstruct.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentMapperTest {
    private StudentMapper mapper = Mappers.getMapper(StudentMapper.class);

    @Test
    public void testToEntity() {
        StudentDTO dto = new StudentDTO();
        dto.setClassVal("A");
        dto.setName("dto_1");
        dto.setId(1);
        Student entity = mapper.toEntity(dto);
        assertEquals(dto.getClassVal(), entity.getClassName());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getId(), entity.getId());
        // 测试自定义转换函数
        entity = mapper.toEntityManual(dto);
        assertEquals(dto.getClassVal(), entity.getClassName());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getId(), entity.getId());
    }

    @Test
    public void testToDto() {
        Student entity = new Student();
        entity.setClassName("A");
        entity.setName("dto_1");
        entity.setId(1);
        StudentDTO dto = mapper.toDto(entity);
        assertEquals(dto.getClassVal(), entity.getClassName());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getId(), entity.getId());
    }
}
