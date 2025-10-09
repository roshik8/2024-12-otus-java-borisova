package ru.otus.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"java:S1989"})
public class ClientServlet extends HttpServlet {

    private final transient DBServiceClient dbServiceClient;
    private final transient TemplateProcessor templateProcessor;

    public ClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clients", dbServiceClient.findAll());

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(templateProcessor.getPage("clients.html", paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String addressStr = req.getParameter("address");
        String phonesStr = req.getParameter("phone");

        Client client = new Client(name);

        if (addressStr != null && !addressStr.isBlank()) {
            client.setAddress(new Address(null, addressStr));
        }

        if (phonesStr != null && !phonesStr.isBlank()) {
            String[] numbers = phonesStr.split(",");
            List<Phone> phoneList = new ArrayList<>();
            for (String number : numbers) {
                number = number.trim();
                if (!number.isEmpty()) {
                    phoneList.add(new Phone(null, number, client));
                }
            }
            client.setPhones(phoneList);
        }
        dbServiceClient.saveClient(client);
        resp.sendRedirect("/clients");
    }

}
