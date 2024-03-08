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

//    // 日報更新
//    @Transactional
//    public ErrorKinds update(Reports reports) {

////        //上書きする従業員の従業員番号を元データで検索
////        Reports motodate = findByCode(reports.getCode());
//
//        //入力した名前を元データにセットする
//        motodate.setName(reports.getName());
//
//        //入力した権限を元データにセットする
//        motodate.setRole(reports.getRole());
//
//        // パスワードが空だった場合は何もしない
//        if ("".equals(reports.getPassword())) {
//
//        // パスワードが入っていた場合
//        } else {
//            //入力した新しいパスワードを元データにセットする
//            motodate.setPassword(reports.getPassword());
//            // 元データにセットされたパスワードが条件に当てはまるかチェックする
//            ErrorKinds result = reportsPasswordCheck(motodate);
//            //エラーがあるか確認　エラーがなければ83行目に進む
//            if (ErrorKinds.CHECK_OK != result) {
//                return result;
//            }
//        }
//
//        //更新日時を元データにセットする
//        LocalDateTime now = LocalDateTime.now();
//        motodate.setUpdatedAt(now);
//
//        reportsRepository.save(motodate);
//        return ErrorKinds.SUCCESS;
//    }

//    // 日報削除
//    @Transactional
//    public ErrorKinds delete(String code, UserDetail userDetail) {
//
//        // 自分を削除しようとした場合はエラーメッセージを表示
//        if (code.equals(userDetail.getReports().getCode())) {
//            return ErrorKinds.LOGINCHECK_ERROR;
//        }
//        Reports reports = findByCode(code);
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
