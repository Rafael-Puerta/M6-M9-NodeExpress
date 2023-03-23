const express = require('express')
const fs = require('fs/promises')
const url = require('url')
const post = require('./post.js')
const { v4: uuidv4 } = require('uuid')
const mysql=require('mysql2')

// Wait 'ms' milliseconds
function wait (ms) {
  return new Promise(resolve => setTimeout(resolve, ms))
}

// Start HTTP server
const app = express()

// Set port number
const port = process.env.PORT || 3030

// Publish static files from 'public' folder
app.use(express.static('public'))

// Activate HTTP server
const httpServer = app.listen(port, appListen)
function appListen () {
  console.log(`Listening for HTTP queries on: http://localhost:${port}`)
}

process.on("SIGINT", () => {
  console.log("Closing http server");
  httpServer.close()
})

// Set URL rout for POST queries
app.post('/dades', getDades)
async function getDades (req, res) {
  let receivedPOST = await post.getPostObject(req)
  let result = { status: "KO", result: "Unkown type" }

  if (receivedPOST) {
    if (receivedPOST.type == "ping") {

      result = { status: "OK", result: "" }
      await wait(1000)
    }
    else if(receivedPOST.type == "users"){
      let fetch=await queryDatabase('SELECT * FROM Usuaris')
      result = { status: "OK", result: fetch }
      await wait(1000)
    }
    else if(receivedPOST.type == "user"){
      // console.log(receivedPOST.name); // use this for query (id)
      if(receivedPOST.name){
        let fetch=await queryDatabase(`SELECT * FROM Usuaris Where id='${receivedPOST.name}'`)
        result = { status: "OK", result: fetch[0] }
      }
      await wait(1000)
    }
    else if(receivedPOST.type == "add"){
      // result = { status: "OK", result: [{name:"tes",id:"1"},{name:"test",id:"2"}] }
      await queryDatabase(`INSERT INTO Usuaris (city,name,surname,direction,mail,phone) VALUES ('${receivedPOST.city}','${receivedPOST.name}','${receivedPOST.surname}','${receivedPOST.direction}','${receivedPOST.mail}','${receivedPOST.phone}')`)
      result={ status: "OK", result: "" }
      await wait(1000)
    }
    else if(receivedPOST.type == "edit"){
      // result = { status: "OK", result: [{name:"tes",id:"1"},{name:"test",id:"2"}] }
      let fetch=await queryDatabase(`SELECT * FROM Usuaris Where id='${receivedPOST.id}'`)
      if(fetch){
        await queryDatabase(`UPDATE Usuaris SET city='${receivedPOST.city}',name='${receivedPOST.name}',surname='${receivedPOST.surname}',direction='${receivedPOST.direction}',mail='${receivedPOST.mail}',phone='${receivedPOST.phone}' Where id='${receivedPOST.id}'`)
        result={ status: "OK", result: "" }
      }
      await wait(1000)
    }
    
  }

  res.writeHead(200, { 'Content-Type': 'application/json' })
  res.end(JSON.stringify(result))
}

// Run WebSocket server
const WebSocket = require('ws')
const wss = new WebSocket.Server({ server: httpServer })
const socketsClients = new Map()
console.log(`Listening for WebSocket queries on ${port}`)

// What to do when a websocket client connects
wss.on('connection', (ws) => {

  console.log("Client connected")

  // Add client to the clients list
  const id = uuidv4()
  const color = Math.floor(Math.random() * 360)
  const metadata = { id, color }
  socketsClients.set(ws, metadata)

  // Send clients list to everyone
  sendClients()

  // What to do when a client is disconnected
  ws.on("close", () => {
    socketsClients.delete(ws)
  })

  // What to do when a client message is received
  ws.on('message', (bufferedMessage) => {
    var messageAsString = bufferedMessage.toString()
    var messageAsObject = {}
    
    try { messageAsObject = JSON.parse(messageAsString) } 
    catch (e) { console.log("Could not parse bufferedMessage from WS message") }

    if (messageAsObject.type == "bounce") {
      var rst = { type: "bounce", message: messageAsObject.message }
      ws.send(JSON.stringify(rst))
    } else if (messageAsObject.type == "broadcast") {
      var rst = { type: "broadcast", origin: id, message: messageAsObject.message }
      broadcast(rst)
    } else if (messageAsObject.type == "private") {
      var rst = { type: "private", origin: id, destination: messageAsObject.destination, message: messageAsObject.message }
      private(rst)
    }
  })
})

// Send clientsIds to everyone
function sendClients () {
  var clients = []
  socketsClients.forEach((value, key) => {
    clients.push(value.id)
  })
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      var id = socketsClients.get(client).id
      var messageAsString = JSON.stringify({ type: "clients", id: id, list: clients })
      client.send(messageAsString)
    }
  })
}

// Send a message to all websocket clients
async function broadcast (obj) {
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      var messageAsString = JSON.stringify(obj)
      client.send(messageAsString)
    }
  })
}

// Send a private message to a specific client
async function private (obj) {
  wss.clients.forEach((client) => {
    if (socketsClients.get(client).id == obj.destination && client.readyState === WebSocket.OPEN) {
      var messageAsString = JSON.stringify(obj)
      client.send(messageAsString)
      return
    }
  })
}

// Perform a query to the database
function queryDatabase (query) {

  // TODO change url to the railway database
  return new Promise((resolve, reject) => {
    var connection = mysql.createConnection({
      host: process.env.MYSQLHOST || "containers-us-west-205.railway.app",
      port: process.env.MYSQLPORT || 5631,
      user: process.env.MYSQLUSER || "root",
      password: process.env.MYSQLPASSWORD || "LGgH18MopgWeHFNW9WRF",
      database: process.env.MYSQLDATABASE || "railway"
    });

    connection.query(query, (error, results) => { 
      if (error) reject(error);
      resolve(results)
    });
     
    connection.end();
  })
}