-- Create bank_account table
CREATE SEQUENCE IF NOT EXISTS sequence_name START WITH 1 INCREMENT BY 1;

CREATE TABLE bank_account
(
    id           BIGINT  NOT NULL,
    version      BIGINT,
    created_date BIGINT  NOT NULL,
    iban         VARCHAR(255),
    holder       VARCHAR(255),
    status       VARCHAR(255),
    balance      DECIMAL NOT NULL,
    currency     VARCHAR(255),
    CONSTRAINT pk_bank_account PRIMARY KEY (id)
);

CREATE INDEX idx_bank_account_currency ON bank_account (currency);

CREATE INDEX idx_bank_account_holder ON bank_account (holder);

CREATE INDEX idx_bank_account_status ON bank_account (status);

CREATE UNIQUE INDEX uc_bank_account_iban ON bank_account (iban);

-- Create transfer table
CREATE SEQUENCE IF NOT EXISTS sequence_name START WITH 1 INCREMENT BY 1;

CREATE TABLE transfer
(
    id            BIGINT  NOT NULL,
    version       BIGINT,
    created_date  BIGINT  NOT NULL,
    creditor_iban VARCHAR(255),
    debtor_iban   VARCHAR(255),
    status        VARCHAR(255),
    amount        DECIMAL NOT NULL,
    currency      VARCHAR(255),
    CONSTRAINT pk_transfer PRIMARY KEY (id)
);

CREATE INDEX idx_transfer_creditor_iban ON transfer (creditor_iban);

CREATE INDEX idx_transfer_currency ON transfer (currency);

CREATE INDEX idx_transfer_debtor_iban ON transfer (debtor_iban);

CREATE INDEX idx_transfer_status ON transfer (status);