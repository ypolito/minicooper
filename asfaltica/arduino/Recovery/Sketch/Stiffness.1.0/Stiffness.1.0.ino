#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins

//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //10 RX, 11 TX.
int comando = 0;
long randNumber;
void setup()
{
  BT.begin(9600); //Velocidad del puerto del módulo Bluetooth
  //  Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad
}

void loop()
{
  if(BT.available())
  {
    comando = BT.read();
  }
  else
  {
    if (comando == 0)
    {
      delay(500);
    }
    if (comando == 1)
    {
      delay(500);
      comando = 2;
      BT.write("OK");
    }
    if (comando == 2)
      randNumber = random(300);
      BT.write(randNumber);
  }
  if (comando == 3)
  {
    delay(500);
  }


}

