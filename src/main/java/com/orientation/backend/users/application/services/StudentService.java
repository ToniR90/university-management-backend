package com.orientation.backend.users.application.services;

import com.orientation.backend.users.application.commands.CreateStudentCommand;
import com.orientation.backend.users.application.commands.MarkAsAlumniCommand;
import com.orientation.backend.users.application.commands.UpdateContactCommand;
import com.orientation.backend.users.application.commands.UpdateRgpdConsentCommand;
import com.orientation.backend.users.application.exceptions.DuplicateDniException;
import com.orientation.backend.users.application.exceptions.InvalidStudentOperationException;
import com.orientation.backend.users.application.exceptions.StudentNotFoundException;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.AlumniType;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.RgpdConsentStatus;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public Student createStudent(CreateStudentCommand command) {
        Dni dni = Dni.of(command.dni());

        if(studentRepository.existsByDni(dni)) {
            throw new DuplicateDniException(command.dni());
        }

        Student student = Student.builder()
                .dni(dni)
                .fullName(FullName.of(command.name(), command.firstSurname(), command.secondSurname()))
                .email(Optional.ofNullable(command.email()).map(Email::of))
                .phone(Optional.ofNullable(command.phone()).map(Phone::of))
                .degree(command.degree())
                .currentYear(CurrentYear.valueOf(command.currentYear()))
                .alumniInfo(AlumniInfo.notAlumni())
                .rgpdConsent(RgpdConsent.pending())
                .build();

        return studentRepository.save(student);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException(id));
    }

    public Student findByDni(String dni) {
        return studentRepository.findByDni(Dni.of(dni)).orElseThrow(() -> new StudentNotFoundException(dni));
    }

    @Transactional
    public Student updateContactInfo(Long id, UpdateContactCommand command){
        if(command.email() == null && command.phone() == null) {
            throw new InvalidStudentOperationException("No es pot actualitzar sense cap dada de contacte");
        }

        Student student = findById(id);

        Email email = Email.ofNullable(command.email());
        Phone phone = Phone.ofNullable(command.phone());

        student.updateContactInfo(email, phone);

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateRgpdConsent(Long id, UpdateRgpdConsentCommand command){
        Student student = findById(id);

        RgpdConsent consent = switch(command.rgpdConsentStatus()) {
            case "PENDING" -> RgpdConsent.pending();
            case "SIGNED_IN_PERSON" -> RgpdConsent.signedInPerson();
            case "SIGNED_ONLINE" -> RgpdConsent.signedOnline();
            case "ALREADY_SIGNED" -> {
                if (command.signedYear() == null) {
                    throw new InvalidStudentOperationException("Any de signatura requerit per ALREADY_SIGNED");
                }
                yield RgpdConsent.alreadySigned(command.signedYear());
            }
            default -> throw new InvalidStudentOperationException("Status RGPD no vàlid: " + command.rgpdConsentStatus());
        };
        student.updateRgpdConsent(consent);

        return studentRepository.save(student);
    }

    @Transactional
    public Student markAsAlumni(Long id, MarkAsAlumniCommand command) {
        Student student = findById(id);

        if(command.graduationYear() < 1900 || command.graduationYear() > LocalDateTime.now().getYear()) {
            throw new InvalidStudentOperationException("L'any de graduació no és correcte");
        }

        AlumniType type;
        try {
            type = AlumniType.valueOf(command.alumniType());
        } catch (IllegalArgumentException e) {
            throw new InvalidStudentOperationException("Tipus d'alumni no vàlid: " + command.alumniType());
        }

        student.markAsAlumni(type, command.graduationYear());

        return studentRepository.save(student);
    }
}