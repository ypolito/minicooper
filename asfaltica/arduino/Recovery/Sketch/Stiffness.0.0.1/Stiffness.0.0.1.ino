#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins

//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //10 RX, 11 TX.
int comando = 0;
long randNumber;
String comIn, comOut;
int comBuild = 0; // Indica cuando se debe resetear el string que contitne el comando entrante
int recived=0;

void setup()
{
  BT.begin(9600); //Velocidad del puerto del módulo Bluetooth
  comIn = "inicio";
  //Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad
  //Serial.println("Inicio de servicios...");
}

void loop()
{
  comBuild = 1;
  while (BT.available())
  {
    recived = 1;
    if (   comBuild == 1 ) {
      comIn = "";
      comBuild = 0;
    }
    char singleChar = BT.read();
    comIn += singleChar;
  }

  if (comIn == "SF+1")
  {
    randNumber = random(300);
    BT.println(randNumber);
    comIn = "SF+2";
  }
    //  BT.println(randNumber);    
}



