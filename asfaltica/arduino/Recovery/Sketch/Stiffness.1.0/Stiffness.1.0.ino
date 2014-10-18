#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins

//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //10 RX, 11 TX.
int comando = 0;
long randNumber;
String comIn, comOut, part;
int comBuild = 0; // Indica cuando se debe resetear el string que contitne el comando entrante

void setup()
{
  BT.begin(9600); //Velocidad del puerto del módulo Bluetooth
  Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad
  Serial.println("Inicio de servicios...");
}

void loop()
{

  while (BT.available())
  {
    if (   comBuild == 1 ) {
      comIn == "";
      comBuild = 0;
    }
    //part = BT.read();
    comIn += BT.read();
  }
  
  comBuild = 1;
  Serial.print(comIn);
  delay(1000);
  
  if (comIn == "SF+1")
  {
    randNumber = random(300);
    BT.println(randNumber);    
  }
      BT.println(randNumber);    
}


