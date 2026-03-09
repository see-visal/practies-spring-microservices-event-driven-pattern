package com.see_visal.common.domain.valueoject;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TransactionId  {

    private final UUID value;



    public  TransactionId(UUID value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
