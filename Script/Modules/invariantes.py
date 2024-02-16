import re

def get_invariants():
    transiciones_contador = {'T' + str(i): 0 for i in range(1, 13)}

    tiempos = []

    invariantes = [
        [1, 2, 4, 6, 8],
        [1, 3, 5, 7, 8],
        [9, 10, 11, 12]
    ]

    invariantes_contador = [0 for _ in invariantes]

    patron_transicion = re.compile(r'(\d+) Transition (T\d+) fired')

    with open('../log.txt', 'r') as archivo:
        for linea in archivo:
            # Buscar las coincidencias con el patrón de transición y tiempo
            coincidencia = patron_transicion.search(linea)
            if coincidencia:
                tiempo = int(coincidencia.group(1))
                transicion = coincidencia.group(2)
                tiempos.append(tiempo)
                transiciones_contador[transicion] += 1

    total_disparos = sum(transiciones_contador.values())

    for i, invariante in enumerate(invariantes):
        invariantes_contador[i] = min([transiciones_contador['T' + str(t)] for t in invariante])

    invariantes_porcentaje = [(contador / total_disparos * 100) if total_disparos > 0 else 0 for contador in invariantes_contador]

    print("\nEjecuciones por invariante y su porcentaje respecto al total de disparos:")
    for i, (contador, porcentaje) in enumerate(zip(invariantes_contador, invariantes_porcentaje)):
        print(f"Invariante {i+1}: {contador} veces ejecutado, {porcentaje:.2f}% del total de disparos")
