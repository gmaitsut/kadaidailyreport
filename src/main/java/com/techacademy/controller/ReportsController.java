package com.techacademy.controller;

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
    @GetMapping(value = "/{code}/")
    public String detail(@PathVariable String code, Model model) {

        model.addAttribute("reports", reportsService.findById(code));
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

//    // 日報更新画面
//    @GetMapping(value = "/{code}/update")
//    public String edit(@PathVariable String code, Model model, Reports reports) {
//        if (null != code) {
//            model.addAttribute("reports", reportsService.findByCode(code));
//        } else {
//            model.addAttribute("reports", reports);
//        }
//
//        return "reports/update";
//    }
//
//    // 日報更新処理
//    @PostMapping(value = "/{code}/update")
//    public String update(@PathVariable String code, @Validated Reports reports, BindingResult res, Model model) {
//
//        // URLにある従業員番号を更新画面の入力フォームにセット
//        reports.setCode(code);
//
//        // 入力チェックでエラー表示になった場合に更新画面を再表示させる
//        if (res.hasErrors()) {
//            return edit(null, model, reports);
//        }
//
//        // エラーがない場合は従業員を更新する
//        ErrorKinds result = reportsService.update(reports);
//
//        //パスワードがエラーだった場合のエラー表示
//        if (ErrorMessage.contains(result)) {
//
//            //エラーメッセージをセットする
//            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
//
//            //更新画面を再表示させる
//            return edit(null, model, reports);
//        }
//
//        //更新出来て一覧画面に戻る
//        return "redirect:/reports";
//    }
//
//    // 日報削除処理
//    @PostMapping(value = "/{code}/delete")
//    public String delete(@PathVariable String code, @AuthenticationPrincipal UserDetail userDetail, Model model) {
//
//        ErrorKinds result = reportsService.delete(code, userDetail);
//
//        if (ErrorMessage.contains(result)) {
//            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
//            model.addAttribute("reports", reportsService.findByCode(code));
//            return detail(code, model);
//        }
//
//        return "redirect:/reports";
//    }

}
