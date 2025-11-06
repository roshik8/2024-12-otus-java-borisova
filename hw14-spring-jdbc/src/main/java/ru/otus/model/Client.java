package ru.otus.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
public record Client(
        @Id Long id,
        String name,
        @MappedCollection(idColumn = "client_id") Set<Address> addresses,
        @MappedCollection(idColumn = "client_id") Set<Phone> phones) {
}
