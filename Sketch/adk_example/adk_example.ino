#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>
#define  LED_PIN  13
#define  LEFT_MOTOR_PIN 7
#define  RIGHT_MOTOR_PIN 9
#define  LIGHT_SENSOR_INPUT A0
#define  LIGHT_OUTPUT 12
AndroidAccessory acc("Edison",
		"Endroid",
		"Awesomeness",
		"1.0",
		"http://edisonwang.com",
                "0000000012345678");
                
//Motor control                
#include <Servo.h>
Servo left_motor; 
Servo right_motor; 

const int MF = 20;  // angle that moves motor forward
const int MB = 138; // angle that moves motor backward
const int MS = 91; // angle that stops the motor

//Sensor controls
unsigned long delayTimeMark=0;  //a millisecond time stamp used by the IsTime() function. initialize to 0
unsigned long int delayInterval=100;  //How many milliseconds we want for the flash cycle. 1000mS is 1 second.

const int no_light=350;
const int low_light=250;
const int good_light=150;
const int high_light=60;

int sensorValue=0;

int mode;
void setup()
{
  Serial.begin(115200);
  pinMode(LED_PIN, OUTPUT);
  pinMode(LIGHT_OUTPUT, OUTPUT);  
  acc.powerOn();
  left_motor.attach(LEFT_MOTOR_PIN);
  right_motor.attach(RIGHT_MOTOR_PIN);
}
 
void loop()
{
  android_receive();
  if(IsTime(&delayTimeMark,delayInterval)) {
    toggle_light();
  }
}

void toggle_light(){
  sensorValue = analogRead(LIGHT_SENSOR_INPUT);    
  if(Serial.available()){
     Serial.println("Light sensor: "+String(sensorValue)); 
  }
  if(sensorValue>0){
    if(sensorValue>low_light){
      digitalWrite(LIGHT_OUTPUT, HIGH);  
    }else{
      digitalWrite(LIGHT_OUTPUT, LOW);    
    }
  }
}

void android_receive(){
  byte msg[0];
  int m;
  if (acc.isConnected()) {
    int len = acc.read(msg, sizeof(msg), 1);
    if(len>0){
      m=msg[0];
      if(Serial.available()){
        Serial.println(String(len)+" received: "+String(m)); 
      }
      if(m>1){
        mode=m;
      }else{
        if(m==0){
          digitalWrite(LED_PIN,LOW);
        }else{
          digitalWrite(LED_PIN,HIGH); 
        }
      }
      switch(mode){
        case 2: 
          left_motor.write(MF);
          right_motor.write(MB);
          break;
        case 3: 
          left_motor.write(MS);
          right_motor.write(MS);
          break;
        case 4: 
          left_motor.write(MB);
          right_motor.write(MF);
          break;
        case 5: 
          left_motor.write(MF);
          right_motor.write(MF);
          break;
        case 6: 
          left_motor.write(MB);
          right_motor.write(MB);
          break;
      }
    }
  } else{
    digitalWrite(LED_PIN , LOW); // turn off light
  }
}


//**********************************************************
//IsTime() function - David Fowler, AKA uCHobby, http://www.uchobby.com 01/21/2012

#define TIMECTL_MAXTICKS  4294967295L
#define TIMECTL_INIT      0

int IsTime(unsigned long *timeMark, unsigned long timeInterval){
  unsigned long timeCurrent;
  unsigned long timeElapsed;
  int result=false;
  
  timeCurrent=millis();
  if(timeCurrent<*timeMark) {  //Rollover detected
    timeElapsed=(TIMECTL_MAXTICKS-*timeMark)+timeCurrent;  //elapsed=all the ticks to overflow + all the ticks since overflow
  }
  else {
    timeElapsed=timeCurrent-*timeMark;  
  }

  if(timeElapsed>=timeInterval) {
    *timeMark=timeCurrent;
    result=true;
  }
  return(result);  
}
