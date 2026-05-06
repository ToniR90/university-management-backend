package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.*;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class StudentJpaMapper {

    // ========== JPA -> Domain ==========
    public static Student toDomain(StudentJpaEntity jpaEntity) {
        Student.Builder builder = Student.builder();
        PersonJpaMapper.fillBuilder(jpaEntity, builder);

        AlumniInfo alumniInfo;
        if (jpaEntity.getIsAlumni()) {
            AlumniType type = AlumniType.valueOf(jpaEntity.getAlumniType());
            Integer year = jpaEntity.getGraduationYear();
            alumniInfo = AlumniInfo.createAlumni(type, year);
        } else {
            alumniInfo = AlumniInfo.notAlumni();
        }

        CurrentYear currentYear = CurrentYear.valueOf(jpaEntity.getCurrentYear());

        Optional<String> counselorNotes = Optional.ofNullable(jpaEntity.getCounselorNotes());

        builder
            .degree(Degree.fromString(jpaEntity.getDegree()))
            .currentYear(currentYear)
            .alumniInfo(alumniInfo)
            .counselorNotes(counselorNotes);

        return builder.build();
    }

    // ========== Domain -> Jpa ==========
    public static StudentJpaEntity toJpaEntity(Student student) {
        StudentJpaEntity jpaEntity = new StudentJpaEntity();
        PersonJpaMapper.fillJpaEntity(student, jpaEntity);

        String degree = student.getDegree().name();

        String currentYear = student.getCurrentYear().name();

        Boolean isAlumni = student.getAlumniInfo().isAlumni();
        String alumniType = student.getAlumniInfo().getType().map(AlumniType::name).orElse(null);
        Integer graduationYear = student.getAlumniInfo().getGraduationYear().orElse(null);

        String counselorNotes = student.getCounselorNotes().orElse(null);


        jpaEntity.setDegree(degree);
        jpaEntity.setCurrentYear(currentYear);
        jpaEntity.setIsAlumni(isAlumni);
        jpaEntity.setAlumniType(alumniType);
        jpaEntity.setGraduationYear(graduationYear);
        jpaEntity.setCounselorNotes(counselorNotes);

        return jpaEntity;
    }
}