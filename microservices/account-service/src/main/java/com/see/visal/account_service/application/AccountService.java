package com.see.visal.account_service.application;

import com.see.visal.account_service.application.dto.create.CreateAccountRequest;
import com.see.visal.account_service.application.dto.create.CreateAccountResponse;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyRequest;
import com.see.visal.account_service.application.dto.deposit.DepositMoneyResponse;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountRequest;
import com.see.visal.account_service.application.dto.freeze.FreezeAccountResponse;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyRequest;
import com.see.visal.account_service.application.dto.withdraw.WithdrawMoneyResponse;

/**
 * Account Service Interface
 * Handles account creation and management operations
 */
public interface AccountService {

    /** Create a new account */
    CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);

    /** Deposit money into an existing account */
    DepositMoneyResponse depositMoney(DepositMoneyRequest depositMoneyRequest);

    /** Withdraw money from an existing account */
    WithdrawMoneyResponse withdrawMoney(WithdrawMoneyRequest withdrawMoneyRequest);

    /** Freeze an active account */
    FreezeAccountResponse freezeAccount(FreezeAccountRequest freezeAccountRequest);
}
