# Node.js MongoDB Transactions API

This project demonstrates MongoDB multi-document transactions in an “enterprise-style” Node.js service with layered architecture (config, repositories, services, routes).

## Prerequisites

- Node.js 18+ and npm
- Docker (recommended) or a local MongoDB instance configured as a replica set
- curl or any HTTP client (Postman, Insomnia, etc.)

## 1. Clone and install

git clone https://github.com/shivik/mongodb-transactions-restapi
cd mongodb-transactions-restapi

npm install



## 2. Start MongoDB as a replica set

Transactions require MongoDB running as a replica set, even for local development.

### Option A: Local mongod

mkdir -p /data/mongodb/db0

mongod --port 27017 --dbpath /data/mongodb/db0 --replSet rs0 --bind_ip localhost



In another terminal, start the replica set:

mongosh
rs.initiate()



Keep `mongod` running.

### Option B: Docker (single-node replica set)

docker run -d
--name mongo-rs0
-p 27017:27017
mongo:7
--replSet rs0 --bind_ip_all



Then:

docker exec -it mongo-rs0 mongosh --eval "rs.initiate()"



## 3. Configure environment

Create a `.env` file in the project root:

PORT=3000
MONGO_URI=mongodb://localhost:27017/?replicaSet=rs0
DB_NAME=enterprise_demo



The `replicaSet=rs0` query parameter is required for transaction support.

## 4. Start the API server

npm start

or
node src/server.js



The API will listen on `http://localhost:3000`. 

## 5. Seed demo accounts

Seed two sample accounts inside a transaction: 

curl -X POST http://localhost:3000/api/accounts/seed



Expected response (shape may vary):

{ "ok": true }



## 6. Run a transactional transfer

Perform a funds transfer wrapped in a MongoDB transaction:

curl -X POST http://localhost:3000/api/accounts/transfer
-H "Content-Type: application/json"
-d '{"fromAccountId":"A100","toAccountId":"A200","amount":100}'



You should receive a JSON response with updated balances, and both balance updates will commit or roll back together if any error occurs.

## 7. Verifying data (optional)

Use `mongosh` to inspect balances:

mongosh "mongodb://localhost:27017/enterprise_demo"
db.accounts.find().pretty()



You should see the `balance` fields updated consistently across both accounts, demonstrating a successful multi-document transaction.
