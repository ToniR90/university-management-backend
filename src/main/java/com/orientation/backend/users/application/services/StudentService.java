package com.orientation.backend.users.application.services;

import com.orientation.backend.users.application.commands.CreateStudentCommand;
import com.orientation.backend.users.application.commands.MarkAsAlumniCommand;
import com.orientation.backend.users.application.commands.UpdateContactCommand;
import com.orientation.backend.users.application.commands.UpdateRgpdConsentCommand;
import com.orientation.backend.users.application.exceptions.CreatedStudentException;
import com.orientation.backend.users.application.exceptions.UpdateStudentException;
import com.orientation.backend.users.application.exceptions.StudentNotFoundException;
import com.orientation.backend.users.application.exceptions.core.BusinessViolation;
import com.orientation.backend.users.application.exceptions.core.ErrorCode;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.AlumniType;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.query.StudentSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {
	
	private final StudentRepository studentRepository;
	
	@Transactional
	public Student createStudent(CreateStudentCommand command) {
		List<BusinessViolation> violations = new ArrayList<>();
		
		Dni dni = Dni.of(command.dni());
		
		if (studentRepository.existsByDni(dni)) {
			violations.add(new BusinessViolation(
					"dni",
					"DNI already exists",
					ErrorCode.USER_ALREADY_EXISTS
			));
		}
		
		if (!violations.isEmpty()) {
			throw new CreatedStudentException(violations);
		}
		
		Student student = Student.builder()
				.dni(dni)
				.fullName(FullName.of(command.name(), command.surname()))
				.email(Optional.ofNullable(command.email()).map(Email::of))
				.phone(Optional.ofNullable(command.phone()).map(Phone::of))
				.degree(Degree.fromString(command.degree()))
				.currentYear(CurrentYear.valueOf(command.currentYear()))
				.alumniInfo(AlumniInfo.notAlumni())
				.rgpdConsent(RgpdConsent.pending())
				.build();
		
		return studentRepository.save(student);
	}
	
	@Transactional
	public void deleteStudent(String dni) {
		Student student = findByDni(dni);
		student.deactivate();
		studentRepository.save(student);
	}
	

	public Student findByDni(String dni) {
		return studentRepository.findByDni(Dni.of(dni)).orElseThrow(() -> new StudentNotFoundException(dni));
	}
	
	public List<Student> findAll() {
		return studentRepository.findAll();
	}
	
	@Transactional
	public Student updateContactInfo(String dni, UpdateContactCommand command) {
		List<BusinessViolation> violations = new ArrayList<>();
		
		if (command.email() == null && command.phone() == null) {
			violations.add(new BusinessViolation("contact", "No es pot actualitzar sense cap dada de contacte",
					ErrorCode.BUSINESS_RULE_VIOLATION));
		}
		
		Student student = findByDni(dni);
		
		if (command.email() != null) {
			Email email = Email.of(command.email());
			if (student.getEmail().isPresent()) {
				student.updateEmail(email);
			} else {
				student.addEmail(email);
			}
		}
		
		if (command.phone() != null) {
			Phone phone = Phone.of(command.phone());
			if (student.getPhone().isPresent()) {
				student.updatePhone(phone);
			} else {
				student.addPhone(phone);
			}
		}
		
		if (!violations.isEmpty()) {
			throw new UpdateStudentException(violations);
		}
		
		return studentRepository.save(student);
	}
	
	@Transactional
	public Student updateRgpdConsent(String dni, UpdateRgpdConsentCommand command) {
		List<BusinessViolation> violations = new ArrayList<>();
		
		Student student = findByDni(dni);
		
		RgpdConsent consent = null;
		switch (command.rgpdConsentStatus()) {
			case "PENDING" -> consent = RgpdConsent.pending();
			case "SIGNED_IN_PERSON" -> consent = RgpdConsent.signedInPerson();
			case "SIGNED_ONLINE" -> consent = RgpdConsent.signedOnline();
			case "ALREADY_SIGNED" -> {
				if (command.signedYear() == null) {
					violations.add(new BusinessViolation(
							"signedYear",
							"Any de signatura requerit per ALREADY_SIGNED",
							ErrorCode.BUSINESS_RULE_VIOLATION
					));
				} else {
					consent = RgpdConsent.alreadySigned(command.signedYear());
				}
			}
			default -> violations.add(new BusinessViolation(
					"rgpdConsentStatus",
					"Status RGPD no vàlid: " + command.rgpdConsentStatus(),
					ErrorCode.BUSINESS_RULE_VIOLATION
			));
		}
		
		if (!violations.isEmpty()) {
			throw new UpdateStudentException(violations);
		}
		student.updateRgpdConsent(consent);
		
		return studentRepository.save(student);
	}
	
	@Transactional
	public Student markAsAlumni(String dni, MarkAsAlumniCommand command) {
		List<BusinessViolation> violations = new ArrayList<>();
		Student student = findByDni(dni);
		
		if (command.graduationYear() < 1900 || command.graduationYear() > LocalDateTime.now().getYear()) {
			violations.add(new BusinessViolation("graduationYear", "L'any de graduació no és correcte", ErrorCode.BUSINESS_RULE_VIOLATION));
		}
		
		AlumniType type = null;
		try {
			type = AlumniType.valueOf(command.alumniType());
		} catch (IllegalArgumentException e) {
			violations.add(new BusinessViolation(
					"alumniType",
					"Tipus d'alumni no vàlid: " + command.alumniType(),
					ErrorCode.BUSINESS_RULE_VIOLATION
			));
		}
		
		if (!violations.isEmpty()) {
			throw new UpdateStudentException(violations);
		}
		
		student.markAsAlumni(type, command.graduationYear());
		
		return studentRepository.save(student);
	}

	public PageResult<Student> searchStudents(StudentSearchCriteria criteria, Pagination pagination) {
		return studentRepository.search(criteria, pagination);
	}
}