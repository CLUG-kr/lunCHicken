import time
import picamera
import io
import socket
import struct

HOST = "owlsogul.iptime.org"
PORT = 17779

# Connect client socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((HOST, PORT))

connection = client_socket.makefile('wb')

try:
    with picamera.PiCamera() as camera:
        camera.resolution = (1024, 768)
        # Camera warm-up time
        time.sleep(2)
        
        start_time = time.time()

        stream = io.BytesIO()
        camera.capture(stream, 'jpeg')
        stream.seek(0)
        connection.write(stream.read())
        
        print("send second: %s" %(time.time()-start_time))
        # Reset stream for next capture
        stream.seek(0)
        stream.truncate()

finally:
    connection.close()
    client_socket.close()
