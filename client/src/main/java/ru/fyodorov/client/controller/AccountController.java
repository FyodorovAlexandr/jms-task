package ru.fyodorov.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fyodorov.client.service.AccountService;
import ru.fyodorov.client.service.dto.Account;

@Controller
public class AccountController {
    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/searchAccount")
    public String search(Model model){
        return "search";
    }

    @PostMapping("/getAccount")
    public String getAccount(@RequestParam(name="accountNumber", required=true) String accountNumber, Model model) {
        model.addAttribute("account", accountService.getAccountInfo(accountNumber));
        return "account";
    }

    @PostMapping("/create")
    public String createAccount(Account account, Model model) {
        model.addAttribute("account", accountService.createAccount(account));
        return "create";
    }

    //Тестировать через Postman
    @DeleteMapping("/close")
    public String closeAccount(@RequestParam(name="accountNumber", required=true)int id, Model model) {
        model.addAttribute("account", accountService.closeAccount(id));
        return "close";
    }

    @GetMapping("/showOperations")
    public String showOOperations(@RequestParam(name = "accountNumber", required = true) String accountNumber, Model model) {
        model.addAttribute("account", accountService.getAccountInfo(accountNumber));
        model.addAttribute("operations", accountService.getOperations(accountNumber));
        return "operations";
    }


    @PostMapping("/makeTransfer")
    public String makeTransfer(
            @RequestParam(name="accountNumber", required=true) String accountNumber,
            @RequestParam(name="accountTarget", required=true) String accountTarget,
            @RequestParam(name="amount", required=true) Double amount,
            Model model) {

        model.addAttribute("transferResult", accountService.makeTransfer(accountNumber, accountTarget, amount));
        return getAccount(accountNumber, model);
    }
}
