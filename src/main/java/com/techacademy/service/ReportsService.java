package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportsRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportsService {

    private final ReportsRepository reportsRepository;
    public Object reportDate;

    @Autowired
    public ReportsService(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report reports) {


        // 業務チェック日付
        if (isReportDateAndEmployee(reports.getReportDate(),reports.getEmployee())) {
            return ErrorKinds.DUPLICATE_ERROR;
        }

        reports.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        reports.setCreatedAt(now);
        reports.setUpdatedAt(now);

        reportsRepository.save(reports);
        return ErrorKinds.SUCCESS;
    }

    // 日報更新
    @Transactional
    public ErrorKinds update(Report reports) {

     // 業務チェック日付
        if (isReportDateAndEmployee(reports.getReportDate(),reports.getEmployee())) {
            return ErrorKinds.DATECHECK_ERROR;
        }

        reports.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        reports.setCreatedAt(now);
        reports.setUpdatedAt(now);

        reportsRepository.save(reports);
        return ErrorKinds.SUCCESS;


//        //上書きする日報番号を元データで検索
//        Report motoreport = findById(reports.getId());
//
//        //入力した日付を元データにセットする
//        motoreport.setReportDate(reports.getReportDate());
//
//        //入力したタイトルを元データにセットする
//        motoreport.setTitle(reports.getTitle());
//        return null;


    }

//    // 日報削除
//    @Transactional
//    public ErrorKinds delete(String code, UserDetail userDetail) {
//
//        // 自分を削除しようとした場合はエラーメッセージを表示
//        if (id.equals(userDetail.getReport().getId())) {
//            return ErrorKinds.LOGINCHECK_ERROR;
//        }
//        Report reports = findById(id);
//        LocalDateTime now = LocalDateTime.now();
//        reports.setUpdatedAt(now);
//        reports.setDeleteFlg(true);
//
//        return ErrorKinds.SUCCESS;
//    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportsRepository.findAll();
    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportsRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report reports = option.orElse(null);
        return reports;
    }

    public Boolean isReportDateAndEmployee(LocalDate reportDate,Employee employee) {
        // findByIdで検索
        List<Report> reportList = reportsRepository.findByReportDateAndEmployee(reportDate,employee);
        return !reportList.isEmpty();
    }


}
