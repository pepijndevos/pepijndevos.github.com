---
layout: post
title: Sensing pressure with a SoftPot
image: /images/synth/pressure.PNG
categories:
- music
- electronics
- arduino
---

In my [previous post](/2018/12/25/making-a-new-music-instrument.html) I connected a SoftPot to an Arduino and used a Game Boy as the synth. In this post I'm focusing more on the SoftPot, and specifically on reading pressure.

<iframe width="560" height="315" src="https://www.youtube.com/embed/OmRLW98EcNU" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen> </iframe>

The SoftPot consists of a resistive bottom layer and a conductive top layer. When pressed together, the bottom resistor forms a voltage divider with the top conductor.
When the conductive top side is pressed down, it shorts part of the bottom track, creating a lower total resistance.
The harder you press down, the larger the area of the bottom track that is shorted, the lower the resistance.

So to measure both pressure and position, we'll need to simultaneously read the voltage on the top track, and the resistance of the bottom track. To get an accurate position, we'd like to have the full voltage swing available, without any changes in voltage due to pressure. Meanwhile, we'd like to sense small DC variations in resistance of a large total resistance. This is my best attempt so far.

![circuit](/images/synth/circuit.PNG)

It uses a current mirror to maintain an almost fixed voltage on the pot (R1) and only lose 0.6V of the full swing. The current mirror then feeds into a biased transimpedance amplifier. The rightmost voltage divider is used to set the DC output voltage. (I later added a capacitor in parallel to the TIA feedback resistor to reject noise)

In reality the voltage divider on the positive TIA input is a trim pot. The changes in resistance of the SoftPot are smaller than the process variations. For example, this particular pot changes from 20.7k to 20.3k when pressed, so the bias voltage has to be adjusted for this.

![sensing pressuer and pitch](/images/synth/pressure.PNG)

In the above figure, the blue line is me sliding my finger up and down, while the yellow line is the pressure of my finger, with the green/red lines being the max/min. This seems to work just fine, except after letting it run for a while the pressure value starts to drift. Remember, we need the constant, absolute value, not some high-pass filtered version.

I was really confused, and tried various things to reproduce the problem and improve the situation. Then I blew on the circuit.

![temperature sensitivity](/images/synth/temperature.PNG)

OMG, look at that! The current mirror goes completely crazy when it gets warm. One solution is to add more degeneration, which adds voltage drop. Another is to use a Wilson current mirror, which also adds voltage drop. And finally, you could get two integrated transistors on the same die, which makes them the same temperature and improves matching. I guess that's what I'll have to do. To be continued.

{% highlight C++ %}
const int pitchPin = A0;
const int pressurePin = A1;
const int pwmPin = 12;

const int pressureThreshold = 20;

int pitchValue = 0;        // value from SoftPot
int pressureValue = 0;        // value from TIA
int minPressureValue = INT16_MAX;
int maxPressureValue = 0;
int pressure = 0;
int frequency = 0;
int polarity = 1;

void setup() {
  Serial.begin(9600);
}

void loop() {
  delay(1);
  pitchValue = analogRead(pitchPin);
  delay(1);
  pressureValue = analogRead(pressurePin);
  delay(1);
  minPressureValue = min(pressureValue, minPressureValue);
  maxPressureValue = max(pressureValue, maxPressureValue);
  pressure = map(pressureValue, minPressureValue, maxPressureValue, 0, 10);
  frequency = map(pitchValue, 0, 1023, 440, 1320);
  if (pressureValue < minPressureValue+pressureThreshold) {
    pitchValue = 0;
    noTone(pwmPin);
  } else if (pressure > 6) {
    tone(pwmPin, frequency+pressure*polarity);
    polarity *= -1;
  } else {
    tone(pwmPin, frequency);
  }
  Serial.print(pitchValue);
  Serial.print(", ");
  Serial.print(minPressureValue);
  Serial.print(", ");
  Serial.print(maxPressureValue);
  Serial.print(", ");
  Serial.println(pressureValue);
  delay(50);
}
{% endhighlight %}
