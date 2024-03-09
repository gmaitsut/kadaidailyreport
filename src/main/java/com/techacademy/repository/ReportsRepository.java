package com.techacademy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

public interface ReportsRepository extends JpaRepository<Report, Integer> {

    List<Report> findByReportDateAndEmployee(LocalDate reportDate,Employee employee);
    List<Report> findByTitleAndContent(Integer reportTitle,Integer reportContent);
}