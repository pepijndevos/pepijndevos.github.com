import RPi.GPIO as GPIO
import socket
from time import sleep

GPIO.setmode(GPIO.BOARD)
GPIO.setup(23, GPIO.IN)
GPIO.setup(21, GPIO.IN)
GPIO.setup(19, GPIO.IN)
GPIO.setup(15, GPIO.IN)

s = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
s.connect('/home/pi/.cmus/socket')

def play_pause(e):
    s.send(b'player-pause\n')

def next(e):
    s.send(b'player-next\n')

def volume_up(e):
    s.send(b'vol +10%\n')

def volume_down(e):
    s.send(b'vol -10%\n')

GPIO.add_event_detect(23, GPIO.FALLING, callback=play_pause, bouncetime=200)
GPIO.add_event_detect(21, GPIO.FALLING, callback=next, bouncetime=200)
GPIO.add_event_detect(19, GPIO.FALLING, callback=volume_up, bouncetime=200)
GPIO.add_event_detect(15, GPIO.FALLING, callback=volume_down, bouncetime=200)

while True:
    sleep(1)
