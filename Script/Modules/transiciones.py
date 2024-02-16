import re

def get_transitions():
    transiciones_contador = {'T' + str(i): 0 for i in range(1, 13)}

    tiempos = []

    patron_transicion = re.compile(r'(\d+) Transition (T\d+) fired')

    with open('/home/agustin/Desktop/Concurrente/pc-rdp-final/log.txt', 'r') as archivo:
        for linea in archivo:
            coincidencia = patron_transicion.search(linea)
            if coincidencia:
                tiempo = int(coincidencia.group(1))
                transicion = coincidencia.group(2)
                tiempos.append(tiempo)
                transiciones_contador[transicion] += 1

    if tiempos:
        tiempo_minimo = min(tiempos)
        tiempo_maximo = max(tiempos)
        rango_total_ms = tiempo_maximo - tiempo_minimo
        intervalos_100ms = rango_total_ms / 100.0

        promedios = {transicion: contador / intervalos_100ms for transicion, contador in transiciones_contador.items()}

        print("Conteo total de disparos por transición:")
        for transicion, contador in transiciones_contador.items():
            print(f"{transicion}: {contador} veces disparada")

        print("\nPromedio de disparos cada 100 ms por transición:")
        for transicion, promedio in promedios.items():
            print(f"{transicion}: {promedio:.2f} disparos cada 100 ms")
    else:
        print("No se encontraron tiempos de disparo en el archivo.")
