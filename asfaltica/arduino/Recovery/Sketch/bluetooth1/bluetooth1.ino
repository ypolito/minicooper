#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins
 
//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //10 RX, 11 TX.
 
void setup()
{
  BT.begin(115200); //Velocidad del puerto del módulo Bluetooth
  Serial.begin(115200); //Abrimos la comunicación serie con el PC y establecemos velocidad
}
 
void loop()
{
  if(BT.available())
  {
//  delay(3000);
    BT.write(BT.read());    
    Serial.write("recibido\n");
   
  }
 /*
  if(Serial.available())
  {
     BT.write(Serial.read());
  }
  */
//  delay(3000);
//       //BT.write("Secuencia");    
//       Serial.write("delay\n");
  
}
