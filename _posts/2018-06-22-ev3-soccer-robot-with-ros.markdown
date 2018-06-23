---
layout: post
title: EV3 soccer robot with ROS
image: /images/robocup/legoteamtwente.jpg
categories:
- studl.es
- Robots
---

When I said that [ROS would be a steep learning curve](/2018/06/13/lego-ev3-robocup-robot.html), I did not expect to become a [Docker expert](https://github.com/pepijndevos/ev3dev-ros/blob/master/ev3-ros-cross.dockerfile) and a [kernel developer](https://github.com/ev3dev/ev3-kernel/commit/3a8f45f0a1953e441823e3804e260beb9ba12b3e) in the progress. But in the end it worked, with only a few seconds of lag.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/efCNAxrmhRA?rel=0" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen> </iframe>

Meanwhile RoboTeam Twente [won a game against Robodragons](https://www.youtube.com/watch?v=0ePLMrKmb4U&t=4235s), but that's sadly the only game they won. The finals looked a bit more fast-paced than this game, but not by a lot. It's 10 minutes game time in an hour clock time. These top teams have impressive ball control though. There is some work to be done for Twente... But for now, back to ROS and EV3.

### Installing ROS on the EV3

I headed over the ROS installation page, which states that on Debian they only provide x64 packages, not ARM. So I was kindly redirected to their page about compiling from source. However, the EV3 is far from powerful enough to compile ROS on the device, so a cross-compiler is needed.

Ev3dev uses Docker images with Qemu to do this. But they provide several kinds of images, useful for either cross-compilation or for generating boot images for the EV3. However, ROS does not have a nicely self-contained installation process, so what I needed is a cross-compilation image that could also generate a boot image.

ROS is like a fractal of package management, breaking at every stage. First you install Python packages to install stuff in `/etc` and then you install a bunch of stuff into a workspace, and then you tell it to install system packages for the dependencies of that stuff. Then you combine this into a standalone workspace, which can be used install *more* packages in *more* workspaces.

So at this point I was learning about Dockerfiles and base images and Qemu and ROS releases and contexts and environments and source lists. And after a day of trying and hours of waiting, I had a shiny boot image with my cross-compiled ROS installation. And then David Lechner casually mentioned Debian does actually provide ARM packages, available with a simple `apt-get install ros-robot`.

### Learning ROS

![graph](/images/robocup/rosgraph.png)

So after you're three levels deep in workspaces that you have `source`d, you can begin creating your own packages, by -- you guessed it -- more package management. So you edit your package XML file for the dependencies of your package and run `rosmake` which is like CMake for ROS. You also need to run `rosmake` on Python projects to resolve dependencies and generate more code.

In exchange for all this work, ROS provides a lot of powerful tools, like `roscd`, which is like `cd` but for ROS, `rosed`, which is like `vim` but for ROS, and `rosrun` which is like running your code, but for ROS. I think you can guess what `rosls` is for.

After you have created a package, you can start creating nodes. Nodes can be written in a number of languages, as long as they can talk to the central ROS server which routes all the messages on different topics to all the interested nodes. As per Greenspun's tenth rule, ROS contains an ad-hoc, informally-specified, bug-ridden, slow implementation of half of Erlang. An actor system is not provided, only callbacks.

So now you have all these packages and nodes, and of course it would be silly to `rosrun` all of them individually. So they provide `roslaunch`, which is like `init` but for ROS. So you write some more XML, and `roslaunch` will do the rest. It will even `ssh` into remote machines to launch nodes on it.

The goal was to run ROS on the EV3, but not all of ROS. To be more specific, one single node. The EV3 does not have enough RAM for two operating systems at the same time. So on the EV3 you `source` all the workspaces and `roscd` into your package where you can `rosrun` the node. But before doing that, you have to tell it where the server lives with `export ROS_MASTER_URI=http://pepijn-Latitude-E6420.local:11311`. I tried to get `roslaunch` to do it, but it'd run out of RAM and crash.

So next I ran the listener from the tutorial on the EV3 and the talker on my laptop. Everything would appear to be fine, but no messages arrived. Long story short, it turns out you also need to set `ROS_HOSTNAME` or `ROS_IP` on both ends so that they can actually reach each other.

### Writing a ROS node

With ROS running on my laptop and the EV3, and the basic listener working, it was time to actually write some code. But first, some more package management!

ROS has a `joy` package, to read joysticks, and a `teleop_twist_joy` package that provides a node that converts `joy` to `twist` messages. These packages are supposed to be in your system package manager, but they are not. So I installed them from source, but... package management... fast forward... I `apt-get purge` the Ubuntu packages and use the official ROS Melodic packages.

{% highlight xml %}
<launch>

  <node pkg="joy" name="joy" type="joy_node" output="screen">
    <param name="dev" type="str" value="/dev/input/js0" />
    <param name="deadzone" type="double" value="0.1" />
  </node>

  <node pkg="teleop_twist_joy" name="twist" type="teleop_node" output="screen">
    <param name="enable_button" type="int" value="4" />
    <param name="axis_linear/x" type="int" value="0" />
    <param name="axis_linear/y" type="int" value="1" />
    <param name="scale_linear/x" type="double" value="1" />
    <param name="scale_linear/y" type="double" value="1" />
    <param name="axis_angular" type="int" value="3" />
    <param name="scale_angular" type="double" value="1" />
  </node>

</launch>
{% endhighlight %}

*Now* I can finally start writing code on the EV3. So I copy the Python code from last time, and replace the joystick code with `rospy` listener code. As I get ready to test my code, it dawns on my that `rospy` is Python 2, while `ev3dev-lang-python` is Python 3. I go back to my Docker image and change it to Python 3. An hour later it crashes with an Unicode error, and another hour later with a package version mismatch. The next day it [finally works](https://hub.docker.com/r/pepijndevos/ev3dev-ros/), but it seems to be missing half the packages.

I consider writing my node in C++, but worry about all the package management needed to get `ev3dev-lang-c++` into the ROS universe. I consider my alternatives, and without further package management, I succeed in using a Vala/GTK/GObject binding called `Ev3devKit` from Python 2.

{% highlight python %}
import gi
gi.require_version('Ev3devKit', '0.5')
from gi.repository import Ev3devKit

manager = Ev3devKit.DevicesDeviceManager()
motors = {m.get_address(): m for m in manager.get_tacho_motors()}

def run(port, speed):
    m = motors['ev3-ports:'+port]
    if speed==0:
        m.send_command('stop')
    else:
        m.set_speed_sp(speed)
        m.send_command('run-forever')
{% endhighlight %}

With that in place, all that is left is a few trivial changes to the code from last time.

{% highlight python %}
#!/usr/bin/env python2

import threading
import numpy as np
from geometry_msgs.msg import Twist
from sensor_msgs.msg import Joy
import rospy
import kit

## Initializing ##

angles = np.deg2rad([-36.8, -90, 36.8])
coef = np.array([np.sin(angles), np.cos(angles), [-1,1,1]]).T

speed = np.zeros(3)
kick = 0
running = True

class MotorThread(threading.Thread):
    def run(self):
        print("Engine running!")
        while running:
            sp = coef.dot(speed)
            kit.run('outA', sp[0])
            kit.run('outB', sp[1])
            kit.run('outD', sp[2])
            kit.run('outC', kick)

        kit.run('outA', 0)
        kit.run('outB', 0)
        kit.run('outD', 0)
        kit.run('outC', 0)

motor_thread = MotorThread()
motor_thread.start()

def callback(data):
    #rospy.loginfo(data)
    speed[0] = data.linear.x*-300
    speed[1] = data.linear.y*300
    speed[2] = data.angular.z*100

def btn_callback(data):
    global kick, running
    btn = data.buttons
    if btn[1]:
        kick = 1000
    elif btn[3]:
        kick = -1000
    else:
        kick = 0
    
    if btn[2]:
        running = False
        rospy.signal_shutdown('button pressed')
    
def listener():
    rospy.init_node('ev3dev', anonymous=True)
    rospy.Subscriber("cmd_vel", Twist, callback)
    rospy.Subscriber("joy", Joy, btn_callback)
    print("subscribed")
    rospy.spin()

if __name__ == '__main__':
    listener()

{% endhighlight %}









