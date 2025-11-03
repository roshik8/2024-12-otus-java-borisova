package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.service.ClientService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients";
    }

    @PostMapping
    public String createClient(
            @RequestParam("name") String name,
            @RequestParam("addresses") String address,
            @RequestParam("phones") String phone) {
        clientService.createClient(name, address, phone);
        return "redirect:/clients";
    }
}
