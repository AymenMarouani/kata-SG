# kata-SG

## Bank account kata

Think of your personal bank account experience. When in doubt, go for the simplest solution.

## Requirements

- Deposit and Withdrawal
- Account statement (date, amount, balance)
- Statement printing

The expected result is a service API, and its underlying implementation, that meets the expressed needs.  
Nothing more, especially no UI, no persistence.

## User Stories

### US 1:

In order to save money  
As a bank client  
I want to make a deposit in my account

### US 2:

In order to retrieve some or all of my savings  
As a bank client  
I want to make a withdrawal from my account

### US 3:

In order to check my operations  
As a bank client  
I want to see the history (operation, date, amount, balance) of my operations

## Solution

Exposed REST API with the following endpoints

| feature       | verb | url                   | request body                     | response                                                                                                                                      |
|---------------|------|-----------------------|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| Save/Withdraw | POST | /accounts/{accountId} | { "amount": 10, "type": "SAVE" } | {"result":"SUCCESSFUL","message":"Amount of 10$ successfully deposited in your account","balance":29}                                         |
| Retrieve      | GET  | /accounts/{accountId} | NA                               | {"operations":\[{"type":"SAVE","amount":10,"balance":10,"date":"2023-06-12"},{"type":"WITHDRAW","amount":1,"balance":9,"date":"2023-06-14"}]} |

### Examples

#### View account history

```shell
curl http://localhost:8080/bank-service/api/v1/accounts/ACCOUNT_001
```

```json
{
  "operations": [
    {
      "type": "SAVE",
      "amount": 10,
      "balance": 10,
      "date": "2023-06-12"
    },
    {
      "type": "WITHDRAW",
      "amount": 1,
      "balance": 9,
      "date": "2023-06-14"
    },
    {
      "type": "SAVE",
      "amount": 10,
      "balance": 19,
      "date": "2023-06-15"
    },
    {
      "type": "SAVE",
      "amount": 10,
      "balance": 29,
      "date": "2023-06-15"
    }
  ]
}
```

#### Successful money deposit

```shell
curl -X POST \
     -H "Content-Type: application/json" \
     -d '{ "amount": 10, "type": "SAVE" }' \
     http://localhost:8080/bank-service/api/v1/accounts/ACCOUNT_001
```

```json
{
  "result":"SUCCESSFUL",
  "message":"Amount of 10$ successfully deposited in your account",
  "balance":19
}
```

#### Successful money withdrawal

```shell
curl -X POST \
     -H "Content-Type: application/json" \
     -d '{ "amount": 10, "type": "WITHDRAW" }' \
     http://localhost:8080/bank-service/api/v1/accounts/ACCOUNT_001
```

```json
{
  "result":"SUCCESSFUL",
  "message":"Amount of 10$ withdrawn from your account",
  "balance":9
}
```

#### Insufficient account balance

```shell
curl -X POST \
     -H "Content-Type: application/json" \
     -d '{ "amount": 100, "type": "WITHDRAW" }' \
     http://localhost:8080/bank-service/api/v1/accounts/ACCOUNT_001
```

```json
{
  "result":"FAILED",
  "message":"Insufficient account balance",
  "balance":19
}
```

## TODO

- add the case where the account did not exist
- add exception management
- add logging
- add support for swagger
- add persistence
- add unit tests for the controller
- add BDD test
