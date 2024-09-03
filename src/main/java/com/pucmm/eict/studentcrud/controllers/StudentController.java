package com.pucmm.eict.studentcrud.controllers;

import com.pucmm.eict.studentcrud.models.Student;
import com.pucmm.eict.studentcrud.services.StudentService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String redirectToStudentList() {
        return "redirect:/students";
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "studentList";
    }

    @GetMapping("/students/{matricula}")
    public String getStudentById(@PathVariable int matricula, Model model) {
        Student student = studentService.getStudentByMatricula(matricula);
        model.addAttribute("student", student);
        return "studentDetails";
    }

    @GetMapping("/students/create")
    public String showCreateStudentForm() {
        return "createStudent";
    }

    @PostMapping("/students/create")
    public ResponseEntity<String> createStudent(@ModelAttribute("student") Student student,
                                                BindingResult bindingResult) {
        if (StringUtils.isEmpty(student.getNombre()) ||
                StringUtils.isEmpty(student.getApellido()) ||
                StringUtils.isEmpty(student.getTelefono())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el estudiante. Todos los campos son obligatorios.");
        }

        if (bindingResult.hasErrors()) {
            System.out.println("Ocurrió un error en el controlador pedido-crearCliente: " + bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        try {
            studentService.createStudent(student);
            return ResponseEntity.ok("Estudiante creado exitosamente");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Error al crear el estudiante. La matrícula debe ser única.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el estudiante: " + e);
        }
    }

    @GetMapping("/students/{matricula}/edit")
    public String showEditStudentForm(@PathVariable int matricula, Model model) {
        Student student = studentService.getStudentByMatricula(matricula);
        model.addAttribute("student", student);
        return "editStudent";
    }

    @PostMapping("/students/{matricula}/edit")
    public ResponseEntity<String> updateStudent(@PathVariable int matricula, @ModelAttribute Student student) {
        Student currentStudent = studentService.getStudentByMatricula(matricula);

        if (currentStudent.getNombre().equals(student.getNombre())
                && currentStudent.getApellido().equals(student.getApellido())
                && currentStudent.getTelefono().equals(student.getTelefono())) {
            return ResponseEntity.status(422).body("No haz editado ningún campo");
        }

        studentService.updateStudent(matricula, student);
        return new ResponseEntity<>("Modificaciones realizadas con éxito", HttpStatus.OK);
    }

    @PostMapping("/students/{matricula}/delete")
    public ResponseEntity<String> deleteStudent(@PathVariable int matricula) {
        System.out.println("MATRICULA: " + matricula);
        try {
            studentService.deleteStudent(matricula);
            return new ResponseEntity<>("Estudiante eliminado exitosamente", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Estudiante con matrícula " + matricula + " no encontrado", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar al estudiante", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
