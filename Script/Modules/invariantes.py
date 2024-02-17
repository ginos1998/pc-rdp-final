import re

def get_invariants(invariantes_contador, transiciones_contador, invariantes):
    # Procesar cada invariante
    for i, invariante in enumerate(invariantes):
        # Calcular el número de veces que se ejecutó cada invariante
        invariantes_contador[i] = min([transiciones_contador['T' + str(t)] for t in invariante])

    # Calcular el total de ejecuciones de todos los invariantes
    total_ejecuciones_invariantes = sum(invariantes_contador)

    # Calcular el porcentaje de ejecución de cada invariante respecto al total de ejecuciones de invariantes
    if total_ejecuciones_invariantes > 0:
        invariantes_porcentaje = [(contador / total_ejecuciones_invariantes * 100) for contador in invariantes_contador]

        # Imprimir el resultado
        print("\nEjecuciones por invariante y su porcentaje respecto al total de ejecuciones de invariantes:")
        for i, (contador, porcentaje) in enumerate(zip(invariantes_contador, invariantes_porcentaje)):
            print(f"Invariante {i+1}: {contador} veces ejecutado, {porcentaje:.2f}% del total de ejecuciones de invariantes")
    else:
        print("No se encontraron ejecuciones de invariantes.")

def check_invT(secuencia, reggex):
    # Contador de ocurrencias del invariante
    contador_invariantes = 0

    ocurrencias = re.findall(reggex, secuencia, re.DOTALL)

    # La cantidad de ocurrencias del invariante
    contador_invariantes = len(ocurrencias)

    print(f"\nInvariantes ejecutados de forma exitosa: {contador_invariantes}")
    
