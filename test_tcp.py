import socket
import time
import json
from datetime import datetime

def send_location(sock, imei, lat, lon, speed):
    data = f"{imei},{lat},{lon},{speed}\n"
    sock.send(data.encode())
    print(f"Enviado: {data.strip()}")

def main():
    # Conecta ao servidor TCP
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect(('localhost', 8090))
    
    # IMEI do veículo simulado
    imei = "TESTE123456"
    
    # Coordenadas iniciais (região de São Paulo)
    lat = -23.5505
    lon = -46.6333
    
    try:
        while True:
            # Simula movimento do veículo
            lat += 0.0001  # Move para norte
            lon += 0.0001  # Move para leste
            speed = 60     # 60 km/h
            
            # Envia localização
            send_location(sock, imei, lat, lon, speed)
            
            # Espera 2 segundos antes de enviar próxima localização
            time.sleep(2)
            
    except KeyboardInterrupt:
        print("\nEncerrando simulação...")
    finally:
        sock.close()

if __name__ == "__main__":
    main() 