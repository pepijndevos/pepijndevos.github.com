import parallel
from time import sleep

p = parallel.Parallel()

def write(s):
    bs = map(ord, s)
    bs.append(13)
    for b in bs:
        p.setData(b)
        p.setDataStrobe(1)
        p.setDataStrobe(0)
        sleep(0.001)

def command(cmd, *params):
    write(cmd+",".join(map(str, params)))

def move(m1=0, m2=0, m3=0, m4=0, m5=0, m6=0):
    "Move a number of steps"
    command("M", m1, m2, m3, m4, m5, m6)

def here(n):
    "Save current position in slot n 1-100"
    command("H", n)

def position(n, m1, m2, m3, m4, m5, m6):
    "Save the specified position in slot n 1-100"
    command("P", n, m1, m2, m3, m4, m5, m6)

def zero():
    "Set the reference point"
    command("Z")

def goto(n):
    "Go to position in slot n 1-100"
    command("G", n)

def home():
    "Return to the reference"
    command("N")

def open():
    "open the claw"
    command("O")

def close():
    "Close the claw"
    command("C")

def speed(n):
    "Set the speed 1-5"
    command("S", n)

def delay(n):
    "Wait for n seconds"
    command("D", n)

def limit(b):
    "enable/disable limits"
    commands("L", b)
