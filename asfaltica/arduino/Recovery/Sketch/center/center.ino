
/*
  Center
 
 Testing de la funcion tone()
 
 circuit:
 * 8-ohm speaker on digital pin 8
 
 created 21 Jan 2010
 modified 30 Aug 2011
 by Tom Igoe 

This example code is in the public domain.
 
 http://arduino.cc/en/Tutorial/Tone
 
 C->Do
 D->Re
 E->Mi
 F->Fa
 G->Sol
 A->La
 B->Si
 
 */
 #include "pitches.h"

// notes in the melody:
int melody[] = {
  NOTE_C8, NOTE_D8, NOTE_C8, NOTE_D8, NOTE_C8, NOTE_D8, NOTE_C8, NOTE_D8, 
  NOTE_D4, NOTE_B3, NOTE_D4, NOTE_B3, NOTE_D4, NOTE_B3, NOTE_D4, NOTE_B3};

// note durations: 4 = quarter note, 8 = eighth note, etc.:
int noteDurations[] = {
  3, 8, 3, 8, 3, 8, 3, 8,
  16, 16, 16, 16, 16, 16, 16, 16
  };

void setup() {
  // iterate over the notes of the melody:
  for (int thisNote = 0; thisNote < 17; thisNote++) {

    // to calculate the note duration, take one second 
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 2000/noteDurations[thisNote];
    tone(8, melody[thisNote],noteDuration);
      //tone(8, melody[thisNote]);
    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    /*
    Procesmiento para el centrado de los LVDT
    */
    // stop the tone playing:
    noTone(8);
  }
}

void loop() {
  // no need to repeat the melody.
}
