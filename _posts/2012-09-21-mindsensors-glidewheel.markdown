---
author: pepijndevos
comments: true
date: 2012-09-21 12:29:04+00:00
excerpt: None
layout: post
link: http://studl.es/2012/09/mindsensors-glidewheel/
slug: mindsensors-glidewheel
title: Mindsensors Glidewheel
wordpress_id: 375
categories:
- studl.es
---

I just got two <a href="http://www.mindsensors.com/index.php?module=pagemaster&amp;PAGE_user_op=view_page&amp;PAGE_id=153">Glidewheels</a> from Mindsensors for testing. The Glidewheel allows you to integrate other motors into your NXT models, providing precise control over even the oldest 9V motors.

The Glidewheel is designed for Power Functions motors, but works just as well for my RCX motors.

The hard part of using them with RCX LEGO is that they don't fit directly or close to the RCX motors. You'll have to mount them elsewhere and stick an axle through them. This is further complicated by the stud-based RCX LEGO.

The first thing I made with them is this small car that uses a drive and steer motor. Even with the rotation sensor that you could buy for the RCX, it was incredibly hard to steer a robot like this.

With the Glidewheel it is incredibly easy. Well, almost. My first attempt looked much like my past attempts with the RCX.

<iframe width="500" height="281" src="http://www.youtube.com/embed/9WpOrWgnGeY" frameborder="0" allowfullscreen> </iframe>

The RCX motors are a lot faster than the NXT motors, so what happens is that the <a title="About PID control" href="http://studl.es/2012/02/about-pid-control/">PID controller</a> in the NXT starts overreacting.

To stop this, I used an algorithm called gradual descent(or twiddle, as prof Thrun calls it), which basically modifies P, I or D a little, and sees if it gets better or worse.

<code>
{% highlight plaintext %}
float pid[];
float delta[];
int err;
int newerr;
mutex running;

task record() {
	newerr = 0;
	int tacho;
	Acquire(running);
	for(int i=0; i<200; i++) {
		tacho = MotorRotationCount(OUT_A);
		newerr += abs(tacho - 365);
		Wait(10);
	}
	Release(running);
}

inline void run() {
	ResetRotationCount(OUT_A);
	start record;
	RotateMotorPID(OUT_A, 100, 365, pid[0], pid[1], pid[2]);
	Acquire(running);
	printf("%d", newerr)
	Release(running);
}


task main() {
	ArrayInit(pid, 32, 3);
	ArrayInit(delta, 5, 3);
	err = INT_MAX;
	run();
	while(ArraySum(delta, NA, NA) > 0.1) {
		NumOut(0, LCD_LINE2, pid[0]);
		NumOut(0, LCD_LINE3, pid[1]);
		NumOut(0, LCD_LINE4, pid[2]);
		for(int i=0; i<3; i++) {
			pid[i] += delta[i];
			run();
			if(newerr < err) {
				err = newerr;
				delta[i] *= 1.1;
			} else {
				pid[i] -= 2*delta[i];
				run();
				if(newerr < err) {
					err = newerr;
					delta[i] *= 1.1;
				} else {
					pid[i] += delta[i];
					delta *= 0.9;
				}
			}
		}
	}
	PlayTone(432,1000);
	Wait(10000);
}
{% endhighlight %}
</code>

The result of this code for my little car was
<table>
<tr><td></td><td>P</td><td>I</td><td>D</td></tr>
<tr><td>Default</td><td>96</td><td>32</td><td>32</td></tr>
<tr><td>Free</td><td>40</td><td>40</td><td>32</td></tr>
<tr><td>Load</td><td>40</td><td>32</td><td>40</td></tr>
</table>

Inserting these values in my code, I get this smooth motion.

<iframe width="500" height="281" src="http://www.youtube.com/embed/pOqsYIkqc4A" frameborder="0" allowfullscreen> </iframe>

Code:

<code>
{% highlight plaintext %}
mutex inControl;

task avoid() {
	while(true) {
		while(SensorUS(IN_4)>30);
		Acquire(inControl);
		PosRegSetAngle(OUT_C, 90);
		OnRevRegPID(OUT_A, 50, OUT_REGMODE_SPEED, 40, 32, 40);
		Wait(2000);
		OnFwdRegPID(OUT_A, 50, OUT_REGMODE_SPEED, 40, 32, 40);
		Release(inControl);
	}
}

task turn() {
        while(true) {
                Wait(1000);
		Acquire(inControl);
                PosRegSetAngle(OUT_C, 90);
		Release(inControl);
                Wait(1000);
		Acquire(inControl);
                PosRegSetAngle(OUT_C, -90);
		Release(inControl);
                Wait(1000);
		Acquire(inControl);
                PosRegSetAngle(OUT_C, 0);
		Release(inControl);
        }       

}

task main() {
	SetSensorLowspeed(IN_4);
        OnFwdRegPID(OUT_A, 50, OUT_REGMODE_SPEED, 40, 32, 40);
        PosRegEnable(OUT_C, 40, 40, 32);
	Precedes(turn, avoid);
}
{% endhighlight %}
</code>