package com.example.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletTransactionRequest implements Serializable{
    private Float amount ;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{\"amount\":")
                .append(amount)
                .append("}");
        
        return builder.toString() ;
    }
}
