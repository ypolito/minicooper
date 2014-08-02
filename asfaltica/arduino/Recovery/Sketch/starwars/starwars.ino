
/*
  Melody
 
 Plays a melody 
 
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
 NOTE_C5,NOTE_C5, NOTE_C5, NOTE_GS4, NOTE_DS5, NOTE_C5, NOTE_GS4, NOTE_DS5, NOTE_C5, NOTE_G6, NOTE_G6, NOTE_G6, NOTE_GS6, NOTE_DS6, NOTE_C6, NOTE_GS4, NOTE_DS5, NOTE_C5 };

// note durations: 4 = quarter note, 8 = eighth note, etc.:
int noteDurations[] = { 4,4,4,4,8,4,4,8,4,4,4,4,4,8,4,4,8,3 };

void setup() {
  Serial.begin(9600); 
  Serial.println("Hello world!");
  Serial.println(sizeof(noteDurations));
  // iterate over the notes of the melody:
  for (int thisNote = 0; thisNote < 18; thisNote++) {

    // to calculate the note duration, take one second 
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1500/noteDurations[thisNote];
    tone(8, melody[thisNote],noteDuration);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    // stop the tone playing:
    noTone(8);
  }
}

void loop() {
  // no need to repeat the melody.
}
