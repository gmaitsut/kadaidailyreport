package com.techacademy.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;

import com.techacademy.entity.Report;
import com.techacademy.service.ReportsService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportsController {

    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", reportsService.findAll().size());
        model.addAttribute("reportsList", reportsService.findAll());

        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {

        model.addAttribute("report", reportsService.findById(id));
        
        return "reports/detail";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report reports,@AuthenticationPrincipal UserDetail userDetail) {
        reports.setEmployee(userDetail.getEmployee());

        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report reports, BindingResult res, Model model,@AuthenticationPrincipal UserDetail userDetail) {



        // 日付入力チェック　入力が空だった場合
        if (res.hasErrors()) {
            return create(reports,userDetail);
        }
            ErrorKinds result = reportsService.save(reports);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return create(reports, userDetail);
            }

        return "redirect:/reports";
    }

    // 日報更新画面
    @GetMapping(value = "/{id}/update")
    public String edit(@PathVariable Integer id, Model model, Report reports) {
        if (null != id) {
            model.addAttribute("report", reportsService.findById(id));
        } else {
            model.addAttribute("report", reports);
        }

        return "reports/update";
    }

    // 日報更新処理
    @PostMapping(value = "/{id}/update")
    public String update(@PathVariable Integer id, @Validated Report reports, BindingResult res, Model model) {

        // URLにある日報番号を更新画面の入力フォームにセット
        reports.setId(id);

        // 入力チェックでエラー表示になった場合に更新画面を再表示させる
        if (res.hasErrors()) {
            return edit(null, model, reports);
        }

        // エラーがない場合は日報を更新する
        ErrorKinds result = reportsService.update(reports);

        //日報データが登録済みだった場合のエラー表示
        if (ErrorMessage.contains(result)) {

            //エラーメッセージをセットする
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));

            //更新画面を再表示させる
            return edit(null, model, reports);
        }

        //更新出来て一覧画面に戻る
        return "redirect:/reports";
    }

    // 日報削除処理
//    @PostMapping(value = "/{id}/delete")
//    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {
//
//        ErrorKinds result = reportsService.delete(id, userDetail);
//
//        if (ErrorMessage.contains(result)) {
//            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
//            model.addAttribute("reports", reportsService.findById(id));
//            return detail(id, model);
//        }
//
//        return "redirect:/reports";
//    }

}
