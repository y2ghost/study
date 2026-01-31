package study.ywork.mapstruct.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import study.ywork.mapstruct.dto.StudentDTO;
import study.ywork.mapstruct.dto.SubjectDTO;
import study.ywork.mapstruct.model.Student;

@Mapper
public interface StudentMapper {
    // 演示自定义转换函数
    default Student toEntityManual(StudentDTO dto) {
        if (null == dto) {
            return null;
        }

        Student entity = new Student();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setClassName(dto.getClassVal());

        SubjectDTO subjectDTO = dto.getSubject();
        if (null != subjectDTO) {
            entity.setSubject(subjectDTO.getName());
        }

        return entity;
    }

    @Mapping(target = "className", source = "classVal")
    @Mapping(target = "subject", source = "subject.name")
    Student toEntity(StudentDTO dto);

    // @Mapping(target = "classVal", source = "className")
    // @Mapping(target = "subject.name", source = "subject")
    // 注释的注解功能等同InheritInverseConfiguration注解
    // 使用toEntity方法的反向配置
    @InheritInverseConfiguration
    StudentDTO toDto(Student entity);
}
