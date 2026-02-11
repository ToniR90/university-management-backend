package com.orientation.backend.users.infrastructure.web.controller;

import com.orientation.backend.users.application.commands.CreateStudentCommand;
import com.orientation.backend.users.application.services.StudentService;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.infrastructure.web.dto.request.CreateStudentRequest;
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
}
