package com.see_visal.common.domain.event;

import com.see_visal.common.domain.valueoject.Money;
import com.see_visal.common.domain.valueoject.TransactionId;
import com.see_visal.common.domain.valueoject.AccountId;
import com.see_visal.common.domain.valueoject.CustomerId;

import java.time.ZonedDateTime;

public record MoneyWithdrawnEvent(
        AccountId accountId,
        CustomerId customerId,
        TransactionId transactionId,
        Money amount,
        Money newBalance,
        String remark,
        ZonedDateTime createdAt
) {
    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private AccountId accountId;
        private CustomerId customerId;
        private TransactionId transactionId;
        private Money amount;
        private Money newBalance;
        private String remark;
        private ZonedDateTime createdAt;

        public Builder accountId(AccountId accountId)            { this.accountId = accountId; return this; }
        public Builder customerId(CustomerId customerId)          { this.customerId = customerId; return this; }
        public Builder transactionId(TransactionId transactionId) { this.transactionId = transactionId; return this; }
        public Builder amount(Money amount)                       { this.amount = amount; return this; }
        public Builder newBalance(Money newBalance)               { this.newBalance = newBalance; return this; }
        public Builder remark(String remark)                      { this.remark = remark; return this; }
        public Builder createdAt(ZonedDateTime createdAt)         { this.createdAt = createdAt; return this; }

        public MoneyWithdrawnEvent build() {
            return new MoneyWithdrawnEvent(accountId, customerId, transactionId, amount, newBalance, remark, createdAt);
        }
    }
}
