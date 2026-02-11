package com.orientation.backend.users.infrastructure.web.controller;

import com.orientation.backend.users.application.commands.CreateStudentCommand;
import com.orientation.backend.users.application.commands.MarkAsAlumniCommand;
import com.orientation.backend.users.application.commands.UpdateContactCommand;
import com.orientation.backend.users.application.commands.UpdateRgpdConsentCommand;
import com.orientation.backend.users.application.services.StudentService;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.infrastructure.web.dto.request.CreateStudentRequest;
import com.orientation.backend.users.infrastructure.web.dto.request.MarkAlumniRequest;
import com.orientation.backend.users.infrastructure.web.dto.request.UpdateContactRequest;
import com.orientation.backend.users.infrastructure.web.dto.request.UpdateRgpdRequest;
import com.orientation.backend.users.infrastructure.web.dto.response.StudentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id){
        Student student = studentService.findById(id);
        StudentResponse response = StudentResponse.fromDomain(student);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<StudentResponse> getStudentByDni(@PathVariable String dni){
        Student student = studentService.findByDni(dni);
        StudentResponse response = StudentResponse.fromDomain(student);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(){
        List<Student> students = studentService.findAll();
        List<StudentResponse> responses = students.stream()
                .map(StudentResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request){
        CreateStudentCommand command = new CreateStudentCommand(
                request.dni(),
                request.name(),
                request.firstSurname(),
                request.secondSurname(),
                request.email(),
                request.phone(),
                request.degree(),
                request.currentYear()
        );
        Student student = studentService.createStudent(command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/contact")
    public ResponseEntity<StudentResponse> updateContact(@PathVariable Long id, @Valid @RequestBody UpdateContactRequest request){
        UpdateContactCommand command = new UpdateContactCommand(
                request.email(),
                request.phone()
        );

        Student student = studentService.updateContactInfo(id, command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/rgpd")
    public ResponseEntity<StudentResponse> updateRgpd(@PathVariable Long id, @Valid @RequestBody UpdateRgpdRequest request){
        UpdateRgpdConsentCommand command = new UpdateRgpdConsentCommand(
                request.rgpdConsentStatus(),
                request.signedYear()
        );

        Student student = studentService.updateRgpdConsent(id, command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/alumni")
    public ResponseEntity<StudentResponse> updateAlumni(@PathVariable Long id, @Valid @RequestBody MarkAlumniRequest request){
        MarkAsAlumniCommand command = new MarkAsAlumniCommand(
                request.alumniType(),
                request.graduationYear()
        );

        Student student = studentService.markAsAlumni(id, command);
        StudentResponse response = StudentResponse.fromDomain(student);

        return ResponseEntity.ok(response);
    }
}
