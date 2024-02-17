import re

def get_transitions(path, patron_transicion, transiciones_contador, tiempos, secuencia) -> str:
    # Abrir y leer el archivo
    with open(path, 'r') as archivo:
        for linea in archivo:
            # Buscar las coincidencias con el patrón de transición y tiempo
            coincidencia = patron_transicion.search(linea)
            if coincidencia:
                tiempo = int(coincidencia.group(1))
                transicion = coincidencia.group(2)
                tiempos.append(tiempo)
                transiciones_contador[transicion] += 1
                secuencia += transicion

    # Calcular el rango total de tiempo y dividirlo por 100 para obtener los intervalos de 100 ms
    if tiempos:
        tiempo_minimo = min(tiempos)
        tiempo_maximo = max(tiempos)
        rango_total_ms = tiempo_maximo - tiempo_minimo
        intervalos_100ms = rango_total_ms / 100.0

        # Calcular el promedio de disparos cada 100 ms para cada transición
        promedios = {transicion: contador / intervalos_100ms for transicion, contador in transiciones_contador.items()}

        # Imprimir el resultado
        print("Conteo total de disparos por transición:")
        for transicion, contador in transiciones_contador.items():
            print(f"{transicion}: {contador} veces disparada")

        print("\nPromedio de disparos cada 100 ms por transición:")
        for transicion, promedio in promedios.items():
            print(f"{transicion}: {promedio:.2f} disparos cada 100 ms")
    else:
        print("No se encontraron tiempos de disparo en el archivo.")

    return secuencia
