#include <WiFi.h>
#include <WebServer.h>

#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>

/* ========= CONFIG ========= */

const char* WIFI_SSID = "VIISAR-ESP"; 
const char* WIFI_PASS = "12345678";

#define SERVICE_UUID        "12345678-1234-1234-1234-1234567890ab"
#define CHARACTERISTIC_UUID "87654321-4321-4321-4321-ba0987654321"

/* ========= GLOBALS ========= */

WebServer server(80);
BLECharacteristic* bleChar;

String numberToLetters(int n) {
  if (n <= 0) return "";

  char letter = 'A' + ((n - 1) % 26);
  int repeat = ((n - 1) / 26) + 1;

  String result = "";
  for (int i = 0; i < repeat; i++) {
    result += letter;
  }
  return result;
}
/* ========= COMMON HANDLER ========= */
void handleMessage(String msg) {
  Serial.print("📩 Message received: ");
  Serial.println(msg);

  int colonIndex = msg.indexOf(':');
  if (colonIndex == -1) {
    Serial.println("❌ Invalid format (missing :)");
    return;
  }

  String command = msg.substring(0, colonIndex);
  String numberPart = msg.substring(colonIndex + 1);

  int index = numberPart.toInt();

  Serial.print("Command = ");
  Serial.println(command);

  Serial.print("Parsed index = ");
  Serial.println(index);

  String label = numberToLetters(index);

  Serial.print("Converted label = ");
  Serial.println(label);

}


/* ========= WIFI ========= */

void setupWiFi() {
  WiFi.softAP(WIFI_SSID, WIFI_PASS);
  Serial.print("WiFi AP IP: ");
  Serial.println(WiFi.softAPIP());

  server.on("/send", []() {
    if (!server.hasArg("msg")) {
      server.send(400, "text/plain", "Missing msg");
      return;
    }
    handleMessage(server.arg("msg"));
    server.send(200, "text/plain", "OK");
  });

  server.begin();
  Serial.println("HTTP server started");
}

/* ========= BLE ========= */

class BleCallbacks : public BLECharacteristicCallbacks {
  void onWrite(BLECharacteristic* c) override {
    std::string v = c->getValue();
    if (!v.empty()) {
      handleMessage(String(v.c_str()));
    }
  }
};

void setupBLE() {
  BLEDevice::init("VIISAR-ESP");

  BLEServer* server = BLEDevice::createServer();
  BLEService* service = server->createService(SERVICE_UUID);

  bleChar = service->createCharacteristic(
    CHARACTERISTIC_UUID,
    BLECharacteristic::PROPERTY_WRITE |
    BLECharacteristic::PROPERTY_WRITE_NR
  );

  bleChar->setCallbacks(new BleCallbacks());
  bleChar->setValue("0");

  service->start();

  BLEAdvertising* adv = BLEDevice::getAdvertising();
  adv->addServiceUUID(SERVICE_UUID);
  adv->setScanResponse(true);
  adv->setMinPreferred(0x06);
  adv->setMinPreferred(0x12);

  BLEDevice::startAdvertising();
  Serial.println("BLE advertising started");
}

/* ========= SETUP / LOOP ========= */

void setup() {
  Serial.begin(115200);
  delay(1000);

  Serial.println("Starting ESP...");
  setupWiFi();
  setupBLE();
  Serial.println("ESP READY (WiFi + BLE)");
}

void loop() {
  server.handleClient();
  delay(2);
}
