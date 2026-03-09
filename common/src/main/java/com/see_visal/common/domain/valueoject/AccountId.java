package com.see_visal.common.domain.valueoject;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AccountId  {

    private final UUID value;



    public AccountId(UUID value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

