import usb.core
import binascii
import re

dev = usb.core.find(idProduct=0x001e, idVendor=0x0b0c)
dev.set_configuration()

def hex_print(s):
    print(binascii.hexlify(s).decode(), re.sub("[^a-zA-Z0-9]", ".", str(s, 'ascii', 'replace')))

def write(data):
    s = binascii.unhexlify(data)
    hex_print(s)
    dev.write(0x02, s)

def read(t=100):
    try:
        while True:
            s = dev.read(0x81, 8, t)
            hex_print(s)
            t = 100
    except usb.core.USBError:
        pass
    print('---')

def message(data, display=0x01):
    #write = print
    data = chr(display).encode() + chr(len(data)).encode() + data
    write(b"010305" + binascii.hexlify(chr(len(data)).encode()) + b"00000000")
    for i in range(int(len(data)/6)):
        s = data[i*6:i*6+6]
        b = binascii.hexlify(s)
        write(b"0006" + b)

    i += 1
    s = data[i*6:i*6+6]
    b = binascii.hexlify(s.ljust(6))
    write(b'0004' + b)

# always the same lenth
def confirm_login(data, lang='nl'):
    data = b'\x03' + data
    write(b"0103081500000000")
    for i in range(3):
        s = data[i*6:i*6+6]
        b = binascii.hexlify(s.ljust(6))
        write(b"0006" + b)

    write(b'000300' + binascii.hexlify(lang) + b'000000')

if __name__ == '__main__':
    write(b"0209000000000000") # shield
    read()
    write(b"0103020000000000") # version
    read()
    write(b"0103010200000000") # insert card
    write(b"00026e6c00000000")
    read(10000)
    write(b"0103030000000000") # card info
    read()
    write(b"0103040000000000") # ask pin
    read(60000)
    message(b'abbalalalala', 0x00) # sign data
    read()

    message(b"Never gonna give you up Never gonna let you down                    ")
    read(10000)

    write(b"0103060000000000") # cryptogram
    read()

    confirm_login(b'You where drunk', b'en')
    read()
