#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins

//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //10 RX, 11 TX.
int ledPin = 13;


void setup()
{
  pinMode(ledPin,OUTPUT);
  digitalWrite(ledPin,LOW);
  BT.begin(9600); //Velocidad del puerto del módulo Bluetooth
  Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad
}

void loop()
{
  if(BT.available())
  {
    Serial.write(BT.read());
    //Serial.println("");
    
  }

  if(Serial.available())
  {
    digitalWrite(ledPin,HIGH);
     BT.write(Serial.read());
     
    // delay(1000);
     digitalWrite(ledPin,LOW);
  }
}

