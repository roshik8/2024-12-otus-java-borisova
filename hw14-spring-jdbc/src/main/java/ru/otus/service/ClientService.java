package ru.otus.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> findAll() {
        return (List<Client>) clientRepository.findAll();
    }

    public Client createClient(String name, String address, String phone) {
        Set<Address> addresses = Arrays.stream(address.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(street -> new Address(null, street, null))
                .collect(Collectors.toSet());

        Set<Phone> phones = Arrays.stream(phone.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(number -> new Phone(null, number, null))
                .collect(Collectors.toSet());

        Client client = new Client(null, name, addresses, phones);
        return clientRepository.save(client);
    }
}
