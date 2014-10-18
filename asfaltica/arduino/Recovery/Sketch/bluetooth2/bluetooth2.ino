#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins
 
//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //0 RX, 1 TX.
int inicio = 0;

void setup()
{
  BT.begin(9600); //Velocidad del puerto del módulo Bluetooth
  //Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad
  //delay(2000);
  
}
 
void loop()
{
   while(BT.available())
  {
    //Serial.write(BT.read());
    inicio = 1;
    char c = BT.read(); // libera el buffer
//    BT.write(BT.read());
  }
 
//  if(Serial.available())
if(inicio == 1)
  {
//     BT.write(Serial.read());
     BT.println("recived");
     inicio = 0;
     
  }
  /*
  while (BT.available()) 
  {
    Serial.write(BT.read());
    inicio = 1;
  }
  
  if (inicio == 1) {
     BT.write("HOLA");
     inicio = 0;
  }
  */
/*  if(Serial.available())
  {
     BT.write(Serial.read());
  }*/


}
