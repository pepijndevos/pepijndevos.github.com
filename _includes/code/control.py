import os, pygame, cyber
from time import sleep

os.environ["SDL_VIDEODRIVER"] = "dummy"
pygame.display.init()
pygame.init()
j = pygame.joystick.Joystick(1)
j.init()

#axes = {cyber.base:0, cyber.shoulder:1, cyber.elbow:2, cyber.leftwrist:3, cyber.rightwrist:4, cyber.grip:5}
axes = {cyber.base:0, -cyber.shoulder:1, -cyber.elbow:4}

def dead_int(real, deadzone=0.5):
    if real > deadzone:
        return 1
    elif real < -deadzone:
        return -1
    else:
        return 0

print 'Initialized Joystick : %s' % j.get_name()
try:
    while True:
        pygame.event.pump()
        #sleep(0.01)
        movements = [k*dead_int(j.get_axis(v)) for k, v in axes.iteritems()]

	x, y = j.get_hat(0)
	if x == 0 and y != 0:
		wr = (cyber.leftwrist+cyber.rightwrist) * y
		movements.append(wr)
	elif x != 0 and y ==0:
		movements.append(cyber.leftwrist * x)
		movements.append(cyber.rightwrist * -x)

	if j.get_button(0):
		movements.append(cyber.grip)
	elif j.get_button(1):
		movements.append(-cyber.grip)	

        cyber.move(1, *movements)
        
except KeyboardInterrupt:
    j.quit()
