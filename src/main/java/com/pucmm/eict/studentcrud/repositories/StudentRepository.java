package com.pucmm.eict.studentcrud.repositories;

import com.pucmm.eict.studentcrud.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByMatricula(int matricula);
}