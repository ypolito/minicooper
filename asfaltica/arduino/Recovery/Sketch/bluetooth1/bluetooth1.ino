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
  delay(2000);
  Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad

  BT.write("AT");
  delay(2000);
  //Serial.write(BT.read());
  BT.end();
  
  BT.begin(19200); //Velocidad del puerto del módulo Bluetooth
  delay(2000);
  BT.write("AT");
  delay(2000);
  //Serial.write(BT.read());
  BT.end();

  BT.begin(38400); //Velocidad del puerto del módulo Bluetooth
  delay(2000);
  BT.write("AT");
  delay(2000);
  //Serial.write(BT.read());
  BT.end();

  BT.begin(57600); //Velocidad del puerto del módulo Bluetooth
  delay(2000);
  BT.write("AT");
  delay(2000);
  //Serial.write(BT.read());
  BT.end();

  BT.begin(115200); //Velocidad del puerto del módulo Bluetooth
  delay(2000);
  BT.write("AT");
  delay(2000);
  //Serial.write(BT.read());
  BT.end();

  BT.begin(230400); //Velocidad del puerto del módulo Bluetooth
  delay(2000);
  BT.write("AT");
  delay(2000);
  //Serial.write(BT.read());
  BT.end();
  
  BT.begin(9600); //Velocidad del puerto del módulo Bluetooth  
  Serial.write("Servicios iniciados...\n");  
}
 
void loop()
{
// Serial.write(BT.read());
  if(BT.available())
  {
//  delay(3000);
//    BT.write("Detectado");    
//    BT.write(BT.read());    
    Serial.write(BT.read());
  }

  if(Serial.available())
  {
     BT.write(Serial.read());
  }

//  delay(5000);
//       //BT.write("Secuencia");    
//       Serial.write("delay\n");
  
}
