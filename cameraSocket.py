import time
import picamera
import io
import socket
import struct
import trigger

HOST = "owlsogul.iptime.org"
PORT = 17779

# Create client socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def socket():
    # Connect client socket
    client_socket.connect((HOST, PORT))
    print("SOCKET: connected")
    global connection 
    connection = client_socket.makefile('wb')

def captureAndSend():
    with picamera.PiCamera() as camera:
        camera.resolution = (1024, 768)
        # camera warm-up time
        time.sleep(2)
            
        # send start time
        start_time = time.time()
        
        # capture image
        stream = io.BytesIO()
        camera.capture(stream, 'jpeg')
        
        # send image
        stream.seek(0)
        connection.write(stream.read())
        
        # send time check
        print("SEND sec: %s" %(time.time()-start_time))
        
        # Reset stream for next capture
        stream.seek(0)
        stream.truncate()

if __name__ == "__main__":
    socket()
    try:
        while True:
            if trigger.onClick():
                print("TRIGER: clicked")
                captureAndSend()
    finally:
        connection.close()
        client_socket.close()
        print("SOCKET: disconnected")
