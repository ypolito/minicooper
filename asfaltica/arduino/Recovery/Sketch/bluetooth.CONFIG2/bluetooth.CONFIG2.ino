#include <SoftwareSerial.h> //Librería que permite establecer comunicación serie en otros pins

//Aquí conectamos los pins RXD,TDX del módulo Bluetooth.
SoftwareSerial BT(10,11); //10 RX, 11 TX.
int ledPin = 13;
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
  pinMode(ledPin,OUTPUT);
  digitalWrite(ledPin,HIGH);
  Serial.begin(9600); //Abrimos la comunicación serie con el PC y establecemos velocidad
  for (int r=1; r <= 9; r++)
  {
   
switch (r) {
    case 1:
      Serial.write("BAUD1");        
      BT.begin(1200); //Velocidad del puerto del módulo Bluetooth
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      //do something when var equals 1
      break;
    case 2:
      Serial.write("BAUD2");    
      BT.begin(2400); //Velocidad del puerto del módulo Bluetooth    
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      //do something when var equals 2
      break;
    case 3:
      Serial.write("BAUD3");    
      BT.begin(4800); //Velocidad del puerto del módulo Bluetooth    
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      break;
    case 4:
      Serial.write("BAUD4");    
      BT.begin(9600); //Velocidad del puerto del módulo Bluetooth    
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      break;
    case 5:
      Serial.write("BAUD5");    
      BT.begin(19200); //Velocidad del puerto del módulo Bluetooth    
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      break;
    case 6:
      Serial.write("BAUD6");    
      BT.begin(38400); //Velocidad del puerto del módulo Bluetooth      
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      break;
    case 7:
      Serial.write("BAUD7");    
      BT.begin(57600); //Velocidad del puerto del módulo Bluetooth
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      break;
    case 8:
      Serial.write("BAUD8");    
      BT.begin(115200); //Velocidad del puerto del módulo Bluetooth    
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      break;
    case 9:
      Serial.write("BAUD9");    
      BT.begin(230400); //Velocidad del puerto del módulo Bluetooth    
      BT.write("AT");
      delay(2000);
      BT.write("AT+BAUD4");
      break;
  default:
   break;
  }
       BT.write("AT");
       delay(2000);
       if(BT.available())
       {
          Serial.write(BT.read());
       }
       else
       {
            Serial.println(" sin resp.");
       }
   } 
  digitalWrite(ledPin,LOW);  
  BT.begin(9600);
  Serial.write("Servicio disponible...");
 
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

     BT.write(Serial.read());
//     digitalWrite(ledPin,HIGH);     
//     delay(2000);
//     digitalWrite(ledPin,LOW);
  }
}

