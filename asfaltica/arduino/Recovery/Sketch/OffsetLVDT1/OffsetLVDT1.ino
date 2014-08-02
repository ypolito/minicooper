/* 

  OffsetLVDT1
  
  genera una señal sonora que facilita establecer el offset
  de un dispositivo LVDT
  
  creado 2 Ago 2014
  modified 
  por Claudio Aguila

  este codigo de ejemplo es de dominio publico.

*/

// declaracion de constantes
const int analogInPin = A3; 

int sensorValue = 0;        // valor leido desde el potenciometro
int outputValue = 0;        // valor de la salida PWM (salida analógica)

 #include "pitches.h"

void setup() {
  // inicializa la comunicacion serial con 9600 bits por segundo
  Serial.begin(9600);
}

void loop() {

  // lectura de la entrada analógica
  sensorValue = analogRead(analogInPin);            
  // ubica el valor de la entrada en el rango de la salida analógica
  outputValue = map(sensorValue, 0, 1023, 0, 5);  

  // muestra el resultado en el Serial Monitor:
  Serial.print("sensor = " );                      
  Serial.print(sensorValue);      
  Serial.print("\t output = ");      
  Serial.println(outputValue);  

if (outputValue > 3)
{ tone(8, NOTE_C8 , 8);}
else if (outputValue < 3)
{ tone(8, NOTE_D4 , 8);}
else
{  noTone(8);}
}
