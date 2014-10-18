#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins
 
//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //10 RX, 11 TX.
/* 
   1 = 1200 bps
   2 = 2400 bps
   3 = 4800 bps
   4 = 9600 bps (por defecto)
   5 = 19200 bps
   
   6 = 38400 bps
   7 = 57600 bps
   8 = 115200 bps
   9 = 230400 bps
   A = 460800 bps
   B = 921600 bps
   C = 1382400 bps
*/ 
void setup()
{
  BT.begin(9600); //Velocidad del puerto del módulo Bluetooth
  Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad
    delay(3000);
    BT.write("AT");
     Serial.write("AT");
 // }
  delay(3000);
  
  BT.write("AT+BAUD4");
       Serial.write("AT+BAUD4");
  
    delay(3000);

  
}
 
void loop()
{
   //Serial.write(BT.read());
  if(BT.available())
  {
    Serial.write(BT.read());
  }
 
//  if(Serial.available())
 // {
}

