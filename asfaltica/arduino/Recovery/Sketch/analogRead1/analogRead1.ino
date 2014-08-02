// Declaracion de constantes
const int analogInPin = A3; 
const int analogOutPin = 9;

int sensorValue = 0;        // valor leido desde el potenciometro
int outputValue = 0;        // valor de la salida PWM (salida analógica)

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

  // espera 2 milisegundos antes del siguiente ciclo
  delay(1000);                    
}
