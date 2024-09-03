package com.pucmm.eict.studentcrud.services;

import com.pucmm.eict.studentcrud.models.Student;
import com.pucmm.eict.studentcrud.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentByMatricula(int matricula) {
        return studentRepository.findByMatricula(matricula);
    }

    public void createStudent(Student student) {
        studentRepository.save(student);
    }

    public void updateStudent(int matricula, Student updatedStudent) {
        Student existingStudent = getStudentByMatricula(matricula);
        if (existingStudent != null) {
            existingStudent.setNombre(updatedStudent.getNombre());
            existingStudent.setApellido(updatedStudent.getApellido());
            existingStudent.setTelefono(updatedStudent.getTelefono());
            studentRepository.save(existingStudent);
        }
    }

    public void deleteStudent(int matricula) {
        Student student = studentRepository.findByMatricula(matricula);
        if (student != null) {
            studentRepository.delete(student);
        }
    }
}
