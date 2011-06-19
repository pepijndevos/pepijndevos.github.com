import pygame
from pymouse import PyMouse
from time import sleep

# edit this to reflect your joystick axis and buttons
action = {'x':0, 'y':1, 'multiplier':3, 'left':0, 'right':1}

pygame.init()
j = pygame.joystick.Joystick(0) # first joystick
j.init()
m = PyMouse()
print 'Initialized Joystick : %s' % j.get_name()
state = [0, 0]
try:
    while True:
        pygame.event.pump()
        sleep(0.1)
        # check if any button state has changed and change mouse state accordingly
        if j.get_button(action['left']) and not state[0]:
            state[0] = 1
            print "left press"
            m.press(*m.position())
        elif not j.get_button(action['left']) and state[0]:
            state[0] = 0
            print "left release"
            m.release(*m.position())
        elif j.get_button(action['right']) and not state[1]:
            state[1] = 1
            print "right press"
            m.press(*m.position(), button=2)
        elif not j.get_button(action['right']) and state[1]:
            state[1] = 0
            print "right release"
            m.release(*m.position(), button=2)
        
        x, y = m.position()
        m.move(
            # get_axis returns a value between -1 and 1
            # fumble a bit here to reverse axis
            x + (j.get_axis(action['x']) * 50 * abs(j.get_axis(action['multiplier']) - 1)),
            y + (j.get_axis(action['y']) * 50 * abs(j.get_axis(action['multiplier']) - 1))
        )
except KeyboardInterrupt:
    j.quit()
