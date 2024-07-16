package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "USER")
public class User {

    @Id
    private String id;
    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;

    @Indexed(unique=true)
    private String fiscalCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return fiscalCode != null ? fiscalCode.equals(user.fiscalCode) : user.fiscalCode == null;
    }

    @Override
    public int hashCode() {
        return fiscalCode != null ? fiscalCode.hashCode() : 0;
    }
}
