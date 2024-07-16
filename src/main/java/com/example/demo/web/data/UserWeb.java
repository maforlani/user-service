package com.example.demo.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWeb {

    private String id;
    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;
    private String fiscalCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserWeb user = (UserWeb) o;

        return fiscalCode != null ? fiscalCode.equals(user.getFiscalCode()) : user.getFiscalCode() == null;
    }

    @Override
    public int hashCode() {
        return fiscalCode != null ? fiscalCode.hashCode() : 0;
    }
}
