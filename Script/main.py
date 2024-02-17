import re
from Modules.invariantes import get_invariants, check_invT
from Modules.transiciones import get_transitions

# Definición de los invariantes de transición
invariantes = [
        [1, 2, 4, 6, 8],
        [1, 3, 5, 7, 8],
        [9, 10, 11, 12]
    ]

reggex = r"(T1(?:T2T4T6|T3T5T7)T8)|(T9T10T11T12)"

#Direccion del archivo que quiero leer 
path = '/home/agustin/Desktop/Concurrente/pc-rdp-final/log.txt'

# Diccionario para contar las ocurrencias de cada transición
transiciones_contador = {'T' + str(i): 0 for i in range(1, 13)}

# Contador de ejecuciones para cada invariante
invariantes_contador = [0 for _ in invariantes]

# Lista para almacenar los tiempos de disparo de las transiciones
tiempos = []

# Expresión regular para identificar las líneas que indican una transición disparada
patron_transicion = re.compile(r'(\d+) Transition (T\d+) fired')

#Cadena con las transiciones para posteriormente verificar los invariantes de plaza
secuencia = ""

secuencia = get_transitions(path, patron_transicion, transiciones_contador, tiempos, secuencia)
get_invariants(invariantes_contador, transiciones_contador, invariantes)
check_invT(secuencia, reggex)

