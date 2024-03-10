import re

def set_log2(path, patron_transicion, transiciones_contador, tiempos) -> str:
    secuencia: str = ""
    with open(path, 'r') as archivo:
        for linea in archivo:
            coincidencia = patron_transicion.search(linea)
            if coincidencia:
                tiempo = int(coincidencia.group(1))
                transicion = coincidencia.group(2)
                tiempos.append(tiempo)
                transiciones_contador[transicion] += 1
                secuencia+=transicion

    write_log2(secuencia)

def check_invT(log):
    secuencia = read_log2(log)
    regex = '(T1)(.*?)((T2)(.*?)(T4)(.*?)(T6)(.*?)|(T3)(.*?)(T5)(.*?)(T7)(.*?))(T8)(.*?)|(T9)(.*?)(TA)(.*?)(TB)(.*?)(TC)'
    substitution = '\g<2>\g<5>\g<7>\g<9>\g<11>\g<13>\g<15>\g<17>\g<19>\g<21>\g<23>'

    while len(secuencia) > 0:
        secuencia, num_coincidencias = re.subn(regex, substitution, secuencia)

        if num_coincidencias == 0:
            print("\nVerificacion fallida\n")
            exit(0)

    print("\n-- OK! --\n")

def write_log2(secuencia):
    file = open("log2.txt","w")
    file.write(secuencia)
    file.close()

def read_log2(log) -> str:
    file = open(log,"r")
    return str(file.readline()).replace("T10","TA").replace("T11","TB").replace("T12","TC")

def print_data(tiempos, transiciones_contador):
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

    print("\nPorcentaje de de disparo sobre el numero total de disparos: ")
    for transicion, contador in transiciones_contador.items():
        print(f"{transicion}: {(contador*100)/sum(transiciones_contador.values()):.2f}%")
    

#Direccion del archivo que quiero leer 
path = '../log.txt'

# Expresión regular para identificar las líneas que indican una transición disparada
patron_transicion = re.compile(r'(\d+) Transition (T\d+) fired')

# Diccionario para contar las ocurrencias de cada transición
transiciones_contador: dict = {'T' + str(i): 0 for i in range(1, 13)}

# Lista para almacenar los tiempos de disparo de las transiciones
tiempos = []

#Cadena con las transiciones para posteriormente verificar los invariantes de plaza
set_log2(path, patron_transicion, transiciones_contador, tiempos)

#secuencia = "T1T2T4T6T8T9T10T11T12T10T2T4T6T8"
check_invT("log2.txt")

num = sum(transiciones_contador.values())
#Imprimir datos sobre el disparo de transiciones
print_data(tiempos, transiciones_contador)
