package study.ywork.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import study.ywork.mapstruct.dto.StudentDTO;
import study.ywork.mapstruct.model.StudentBuilder;

@Mapper
public interface StudentBuilderMapper {
    StudentBuilder toEntity(StudentDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "classVal", ignore = true)
    @Mapping(target = "subject", ignore = true)
    StudentDTO toDto(StudentBuilder entity);
}
