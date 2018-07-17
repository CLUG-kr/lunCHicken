## server.py

import socket

def run_server(port=19191):
	host = ''
	with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
	s.bind((host, port))	
	s.listen(1)
	conn, addr = s.accept()

	while True	
		conn.sendall(msg)
	
	conn.close()

if __name__ == '__main__':
	run_server()