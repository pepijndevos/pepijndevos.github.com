import pygame
from nxt import locator, motor
from time import sleep

# edit this to reflect your joystick axis and buttons
axis = {'x':0, 'y':1, 'x*':3, 'y*':5}

b = locator.find_one_brick()

left = motor.Motor(b, motor.PORT_B)
right = motor.Motor(b, motor.PORT_A)
action = motor.Motor(b, motor.PORT_C)

closed = False

def limit(nr):
    if nr > 50 or nr < -50:
        return min(127, max(-128, nr))
    else:
        return 0

def move(fwd=0, turn=0):
    lp = int((fwd - turn) * -100)
    rp = int((fwd + turn) * -100)
    left.run(limit(lp))
    right.run(limit(rp))

def pincer(button):
    global closed
    try:
        if button and not closed:
            closed = True
            action.turn(-40, 70, emulate=False)
        elif not button and closed:
            closed = False
            action.turn(30, 70, emulate=False, brake=False)
    except motor.BlockedException:
        print action.get_tacho()

pygame.init()
j = pygame.joystick.Joystick(0) # first joystick
j.init()
print 'Initialized Joystick : %s' % j.get_name()
try:
    while True:
        pygame.event.pump()
        sleep(0.1)
        
        # get_axis returns a value between -1 and 1
        move(j.get_axis(axis['y']), j.get_axis(axis['x']))
        pincer(j.get_button(0))
        
except KeyboardInterrupt:
    j.quit()
