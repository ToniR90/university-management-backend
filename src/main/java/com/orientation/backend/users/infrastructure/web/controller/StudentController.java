package com.orientation.backend.users.infrastructure.web.controller;

import com.orientation.backend.users.application.commands.CreateStudentCommand;
import com.orientation.backend.users.application.commands.MarkAsAlumniCommand;
import com.orientation.backend.users.application.commands.UpdateContactCommand;
import com.orientation.backend.users.application.commands.UpdateRgpdConsentCommand;
import com.orientation.backend.users.application.services.StudentService;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import com.orientation.backend.users.domain.model.query.PageResult;
import com.orientation.backend.users.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.query.StudentSearchCriteria;
import com.orientation.backend.users.infrastructure.web.dto.request.CreateStudentRequest;
import com.orientation.backend.users.infrastructure.web.dto.request.MarkAlumniRequest;
import com.orientation.backend.users.infrastructure.web.dto.request.UpdateContactRequest;
import com.orientation.backend.users.infrastructure.web.dto.request.UpdateRgpdRequest;
import com.orientation.backend.users.infrastructure.web.dto.response.PagedStudentResponse;
import com.orientation.backend.users.infrastructure.web.dto.response.StudentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/dni/{dni}")
    public ResponseEntity<StudentResponse> getStudentByDni(@PathVariable String dni){
        Student student = studentService.findByDni(dni);
        StudentResponse response = StudentResponse.fromDomain(student);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PagedStudentResponse> getAllStudents(@RequestParam (defaultValue = "0") int page,
                                                               @RequestParam (defaultValue = "20") int size,
                                                               @RequestParam (required = false) String name,
                                                               @RequestParam (required = false) String dni,
                                                               @RequestParam (required = false)CurrentYear currentYear,
                                                               @RequestParam (required = false) Boolean isAlumni,
                                                               @RequestParam (required = false)Degree degree){

        Pagination pagination = new Pagination(page, size);
        StudentSearchCriteria criteria = new StudentSearchCriteria(name, dni, currentYear, isAlumni, degree);
        PageResult<Student> students = studentService.searchStudents(criteria, pagination);

        return ResponseEntity.ok(PagedStudentResponse.fromDomain(students));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request){
        CreateStudentCommand command = new CreateStudentCommand(
                request.dni(),
                request.name(),
                request.surname(),
                request.email(),
                request.phone(),
                request.degree(),
                request.currentYear()
        );
        Student student = studentService.createStudent(command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{dni}/contact")
    public ResponseEntity<StudentResponse> updateContact(@PathVariable String dni, @Valid @RequestBody UpdateContactRequest request){
        UpdateContactCommand command = new UpdateContactCommand(
                request.email(),
                request.phone()
        );

        Student student = studentService.updateContactInfo(dni, command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{dni}/rgpd")
    public ResponseEntity<StudentResponse> updateRgpd(@PathVariable String dni, @Valid @RequestBody UpdateRgpdRequest request){
        UpdateRgpdConsentCommand command = new UpdateRgpdConsentCommand(
                request.rgpdConsentStatus(),
                request.signedYear()
        );

        Student student = studentService.updateRgpdConsent(dni, command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{dni}/alumni")
    public ResponseEntity<StudentResponse> updateAlumni(@PathVariable String dni, @Valid @RequestBody MarkAlumniRequest request){
        MarkAsAlumniCommand command = new MarkAsAlumniCommand(
                request.alumniType(),
                request.graduationYear()
        );

        Student student = studentService.markAsAlumni(dni, command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String dni){
        studentService.deleteStudent(dni);

        return ResponseEntity.noContent().build();
    }
}
