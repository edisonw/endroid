package com.edisonwang.adktest.utils;

public enum ArduinoCommand {
  LED_OFF(0),
  LED_ON(1),
  FORWARD(2),
  STOP_ALL(3),
  BACKWARDS(4),
  RIGHT_TURN(5),
  LEFT_TURN(6);

  public int command;

  ArduinoCommand(int command) {
    this.command = command;
  }

  public int value() {
    return command;
  }
}
