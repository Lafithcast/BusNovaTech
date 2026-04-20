//Author = Nyko
package com.mycompany.proyectoavance1;
public class GrafoBuses {private NodoVertices primeraLocalidad;
    private int cantidadLocalidades;
 
    public GrafoBuses() {
        primeraLocalidad = null;
        cantidadLocalidades = 0;
    }
 
   //agrega vertices
    public void agregarLocalidad(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }
        if (buscarNodoLocalidad(nombre) != null) {
            return;
        }
 
        NodoVertices nuevo = new NodoVertices(nombre.trim());
 
        if (primeraLocalidad == null) {
            primeraLocalidad = nuevo;
        } else {
            NodoVertices actual = primeraLocalidad;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        cantidadLocalidades++;
    }
 
    //Busca vertices por nombre
    private NodoVertices buscarNodoLocalidad(String nombre) {
        NodoVertices actual = primeraLocalidad;
        while (actual != null) {
            if (actual.getNombre().equalsIgnoreCase(nombre)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }
 
    //revisa si existen los vertices
    public boolean existeLocalidad(String nombre) {
        return buscarNodoLocalidad(nombre) != null;
    }
 
   //crea aristas 
    public void agregarRuta(String origen, String destino, double peso) {
        if (origen == null || destino == null || peso <= 0) {
            return;
        }
 
        NodoVertices nodoOrigen = buscarNodoLocalidad(origen);
        if (nodoOrigen == null || buscarNodoLocalidad(destino) == null) {
            return;
        }
 
        //verificar que no exista ya esa arista
        NodoArista arista = nodoOrigen.getPrimeraArista();
        while (arista != null) {
            if (arista.getDestino().equalsIgnoreCase(destino)) {
                return;
            }
            arista = arista.getSiguiente();
        }
 
        NodoArista nuevaArista = new NodoArista(destino, peso);
        if (nodoOrigen.getPrimeraArista() == null) {
            nodoOrigen.setPrimeraArista(nuevaArista);
        } else {
            NodoArista actual = nodoOrigen.getPrimeraArista();
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevaArista);
        }
    }
 
    //print bonito xd
    public String imprimirGrafo() {
        if (primeraLocalidad == null) {
            return "El grafo no tiene localidades registradas.";
        }
 
        String resultado = "=== Grafo de Rutas BusNovaTech ===\n\n";
        NodoVertices localidad = primeraLocalidad;
 
        while (localidad != null) {
            resultado += "[" + localidad.getNombre() + "]\n";
 
            NodoArista arista = localidad.getPrimeraArista();
            if (arista == null) {
                resultado += "  (sin rutas salientes)\n";
            }
            while (arista != null) {
                resultado += "  --> " + arista.getDestino()
                        + "  (peso: " + arista.getPeso() + ")\n";
                arista = arista.getSiguiente();
            }
 
            resultado += "\n";
            localidad = localidad.getSiguiente();
        }
 
        return resultado;
    }
 
    //intenta buscar la ruta mas corta 
    public String rutaMasCorta(String origenNombre, String destinoNombre) {
        if (cantidadLocalidades == 0) {
            return "El grafo no tiene localidades.";
        }
 
        NodoVertices nodoOrigen = buscarNodoLocalidad(origenNombre);
        NodoVertices nodoDestino = buscarNodoLocalidad(destinoNombre);
 
        if (nodoOrigen == null) {
            return "La localidad origen '" + origenNombre + "' no existe.";
        }
        if (nodoDestino == null) {
            return "La localidad destino '" + destinoNombre + "' no existe.";
        }
        if (origenNombre.equalsIgnoreCase(destinoNombre)) {
            return "El origen y el destino son la misma localidad.";
        }
 
        int n = cantidadLocalidades;
 
        //Arreglos
        String[] nombres    = new String[n];
        double[] distancia  = new double[n];
        boolean[] visitado  = new boolean[n];
        int[] predecesor    = new int[n];
 
        int indice = 0;
        NodoVertices actual = primeraLocalidad;
        while (actual != null) {
            nombres[indice] = actual.getNombre();
            distancia[indice] = 99999;
            visitado[indice] = false;
            predecesor[indice] = -1;
            indice++;
            actual = actual.getSiguiente();
        }
 
        int indiceOrigen = buscarIndice(nombres, n, origenNombre);
        int indiceDestino = buscarIndice(nombres, n, destinoNombre);
 
        distancia[indiceOrigen] = 0;
        //Algoritmo para la busqueda
        int paso = 0;
        while (paso < n) {
            int u = -1;
            int k = 0;
            while (k < n) {
                if (!visitado[k] && (u == -1 || distancia[k] < distancia[u])) {
                    u = k;
                }
                k++;
            }
 
            if (u == -1 || distancia[u] >= 99999) {
                break;
            }
 
            visitado[u] = true;
 
            NodoVertices nodoU = buscarNodoLocalidad(nombres[u]);
            NodoArista arista = nodoU.getPrimeraArista();
            while (arista != null) {
                int v = buscarIndice(nombres, n, arista.getDestino());
                if (v >= 0 && !visitado[v]) {
                    double nuevaDist = distancia[u] + arista.getPeso();
                    if (nuevaDist < distancia[v]) {
                        distancia[v] = nuevaDist;
                        predecesor[v] = u;
                    }
                }
                arista = arista.getSiguiente();
            }
 
            paso++;
        }
 
        if (distancia[indiceDestino] >= 99999) {
            return "No existe ruta entre '" + origenNombre + "' y '" + destinoNombre + "'.";
        }
 
        //Reconstruir ruta usando arreglo
        int[] pila = new int[n];
        int tope = 0;
        int nodoActual = indiceDestino;
 
        while (nodoActual != -1) {
            pila[tope] = nodoActual;
            tope++;
            nodoActual = predecesor[nodoActual];
        }
 
        //Construir  la pila al reves
        String ruta = "";
        int i = tope - 1;
        while (i >= 0) {
            ruta += nombres[pila[i]];
            if (i > 0) {
                ruta += " --> ";
            }
            i--;
        }
 
        return "Ruta mas corta de '" + origenNombre + "' a '" + destinoNombre + "':\n"
                + ruta + "\nCosto total: " + distancia[indiceDestino];
    }
 
    private int buscarIndice(String[] nombres, int n, String nombre) {
        int i = 0;
        while (i < n) {
            if (nombres[i] != null && nombres[i].equalsIgnoreCase(nombre)) {
                return i;
            }
            i++;
        }
        return -1;
    }
    //conexion de grafo al json
    public String aJson() {
        String json = "{\n";
        json += "  \"localidades\": [\n";
 
        NodoVertices localidad = primeraLocalidad;
        while (localidad != null) {
            json += "    {\n";
            json += "      \"nombre\": \"" + JsonUtilSimple.escape(localidad.getNombre()) + "\",\n";
            json += "      \"rutas\": [\n";
 
            NodoArista arista = localidad.getPrimeraArista();
            while (arista != null) {
                json += "        {\n";
                json += "          \"destino\": \"" + JsonUtilSimple.escape(arista.getDestino()) + "\",\n";
                json += "          \"peso\": " + arista.getPeso() + "\n";
                json += "        }";
                if (arista.getSiguiente() != null) {
                    json += ",";
                }
                json += "\n";
                arista = arista.getSiguiente();
            }
 
            json += "      ]\n";
            json += "    }";
            if (localidad.getSiguiente() != null) {
                json += ",";
            }
            json += "\n";
            localidad = localidad.getSiguiente();
        }
 
        json += "  ]\n";
        json += "}\n";
        return json;
    }
 
    //jsoon a grafo
    public void desdeJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return;
        }
 
        primeraLocalidad = null;
        cantidadLocalidades = 0;
 
        int pos = 0;
 
        while (pos < json.length()) {
            //buscar inicio de bloque de localidad
            int inicioNombre = json.indexOf("\"nombre\"", pos);
            if (inicioNombre < 0) break;
 
            String nombreLocalidad = JsonUtilSimple.extraerString(json.substring(inicioNombre), "nombre");
            if (nombreLocalidad == null) break;
 
            agregarLocalidad(nombreLocalidad);
 
            //buscar bloque de rutas de esta localidad
            int inicioRutas = json.indexOf("\"rutas\"", inicioNombre);
            if (inicioRutas < 0) break;
 
            int abreLista = json.indexOf("[", inicioRutas);
            int cierraLista = encontrarCierreLista(json, abreLista);
            if (abreLista < 0 || cierraLista < 0) break;
 
            String bloqueRutas = json.substring(abreLista, cierraLista + 1);
 
            //parsear cada ruta dentro del bloque
            int posRuta = 0;
            while (posRuta < bloqueRutas.length()) {
                int inicioDestino = bloqueRutas.indexOf("\"destino\"", posRuta);
                if (inicioDestino < 0) break;
 
                String destino = JsonUtilSimple.extraerString(bloqueRutas.substring(inicioDestino), "destino");
                double peso = extraerDouble(bloqueRutas, "peso", inicioDestino);
 
                if (destino != null && peso > 0) {
                    agregarRuta(nombreLocalidad, destino, peso);
                }
 
                posRuta = inicioDestino + 1;
            }
 
            pos = cierraLista + 1;
        }
    }
 
    //cierra
    private int encontrarCierreLista(String json, int inicio) {
        int profundidad = 0;
        int i = inicio;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '[') profundidad++;
            else if (c == ']') {
                profundidad--;
                if (profundidad == 0) return i;
            }
            i++;
        }
        return -1;
    }
 
    //Extrae un valor 
    private double extraerDouble(String json, String key, int desde) {
        try {
            String fragmento = json.substring(desde);
            String patron = "\"" + key + "\"";
            int pos = fragmento.indexOf(patron);
            if (pos < 0) return -1;
 
            int dosP = fragmento.indexOf(":", pos);
            if (dosP < 0) return -1;
 
            int inicio = dosP + 1;
            while (inicio < fragmento.length() && (fragmento.charAt(inicio) == ' '
                    || fragmento.charAt(inicio) == '\n'
                    || fragmento.charAt(inicio) == '\r'
                    || fragmento.charAt(inicio) == '\t')) {
                inicio++;
            }
 
            int fin = inicio;
            while (fin < fragmento.length()) {
                char c = fragmento.charAt(fin);
                if ((c >= '0' && c <= '9') || c == '.' || c == '-') fin++;
                else break;
            }
 
            return Double.parseDouble(fragmento.substring(inicio, fin).trim());
        } catch (Exception e) {
            return -1;
        }
    }
 
    //getters funcionales
    public int getCantidadLocalidades() {
        return cantidadLocalidades;
    }
 
    public boolean estaVacio() {
        return primeraLocalidad == null;
    }
 
    //return
    public String listarLocalidades() {
        if (primeraLocalidad == null) {
            return "(ninguna)";
        }
        String resultado = "";
        NodoVertices actual = primeraLocalidad;
        while (actual != null) {
            resultado += "- " + actual.getNombre() + "\n";
            actual = actual.getSiguiente();
        }
        return resultado;
    }
}
