#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>
#define  LED_PIN  13
#define  LEFT_MOTOR_PIN 7
#define  RIGHT_MOTOR_PIN 9
AndroidAccessory acc("Edison",
		"Endroid",
		"Awesomeness",
		"1.0",
		"http://edisonwang.com",
                "0000000012345678");
#include <Servo.h>
Servo left_motor; 
Servo right_motor; 

const int MF = 20;  // angle that moves motor forward
const int MB = 138; // angle that moves motor backward
const int MS = 91; // angle that stops the motor

int mode;
void setup()
{
  Serial.begin(115200);
  pinMode(LED_PIN, OUTPUT);
  acc.powerOn();
  left_motor.attach(LEFT_MOTOR_PIN);
  right_motor.attach(RIGHT_MOTOR_PIN);
}
 
void loop()
{
  byte msg[0];
  int m;
  if (acc.isConnected()) {
    int len = acc.read(msg, sizeof(msg), 1);
    if(len>0){
      m=msg[0];
      Serial.println(String(len)+" received: "+String(m)); 
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
