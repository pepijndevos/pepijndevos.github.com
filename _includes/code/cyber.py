import parallel
from time import sleep

base = 1
shoulder = 2
elbow = 4
leftwrist = 8
rightwrist = 16
grip = 32
prf = 64
strobe = 128

p = parallel.Parallel()

def direction(m):
    p.setData(m)
    p.setData(m+strobe)
    p.setData(m)

def step(m):
    p.setData(m)
    p.setData(m+prf)

def move(steps, *motors):
    dir_pattern = abs(sum([m for m in motors if m < 0]))
    step_pattern = sum(map(abs, motors))
    
    direction(dir_pattern)
    for i in range(steps):
        step(step_pattern)
        sleep(0.01)
