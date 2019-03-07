#include <queue>
#include <iostream>
#include <fstream>
#include <vector>
#include <utility>
#include <math.h>

using namespace std;

class Astar {
private:

	class Nodo {
	public:
		Nodo() : fila(-1), columna(-1), coste(-1) {};
		Nodo(int fila, int columna, double coste) :
			fila(fila), columna(columna), coste(coste) {};

		int id;
		int fila;
		int columna;
		double coste;
	};

	int F, C;
	queue<Nodo> cerrada;	//Lista CERRADA donde se guardan los nodos ya visitados y desplegados
	priority_queue<Nodo> abierta;	//Lista ABIERTA donde se guardan en orden de menor coste los nodos que aún quedan por visitar y desplegar
	vector<string> mapa;

	vector<pair<int, int>> camino;	//Vector que contendrá las celdas del camino óptimo

	Nodo nodo_ini, nodo_meta;	//Nodo inicio
	
	//	Vector para generar las casillas adyacentes
	const vector<pair<int, int>> dirs = { { 0,1 },{ 0,-1 },{ 1,0 },{ -1,0 },{ 1,1 },{ -1,-1 },{ 1,-1 },{ -1,1 } };

	//	Función que indica si un par <fila, columna> se encuentra dentro del mapa
	bool correct(int i, int j) {
		return 0 <= i && i < F && 0 <= j && j < C;
	}

public:

	Astar(vector<string> const& mapa, pair<int, int> const& inicio, pair<int, int> const& meta) : F(mapa.size()), C(mapa[0].size()), mapa(mapa),
		nodo_ini(inicio.first, inicio.second, sqrt(pow((inicio.first - meta.first), 2) + pow((inicio.second - meta.second), 2))),
		nodo_meta(meta.first, meta.second, 0) {

		vector<Nodo> padres(F*C, { -1, -1, -1 });	//Guarda en cada posición el padre del nodo cuyo id es el índice del vector

		nodo_ini.id = C * nodo_ini.fila + nodo_ini.columna;

		abierta.push(nodo_ini);
		bool meta_seleccionada = false;

		while (!abierta.empty() && !meta_seleccionada) {

			Nodo n = abierta.top(); abierta.pop();
			cerrada.push(n);

			meta_seleccionada = n.fila == nodo_meta.fila && n.columna == nodo_meta.columna;

			if (!meta_seleccionada) {

				for (auto d : dirs) {
					int nf = n.fila + d.first;
					int nc = n.columna + d.second;

					if (correct(nf, nc) && mapa[nf][nc] != 'X' && (nf*C + nc) != padres[n.id].id && padres[nf*C + nc].fila == -1) {

						double coste_nodo = sqrt(pow((nf - nodo_meta.fila), 2) + pow((nc - nodo_meta.columna), 2));	// Distancia relativa a la meta

						//	Coste del desplazamiento de la casilla n a la nueva casilla
						if (d.first == 0 || d.second == 0)
							coste_nodo += 1;
						else coste_nodo += sqrt(2);

						Nodo nuevo = { nf, nc, coste_nodo };
						nuevo.id = C * nuevo.fila + nuevo.columna;

						padres[nuevo.id] = n;	//Actualización del padre
						abierta.push(nuevo);

					}
				}
			}
		}

		if (meta_seleccionada) {
			Nodo nodo = cerrada.back();
			int id = nodo.id;

			camino.push_back({ nodo.fila + 1, nodo.columna + 1 });

			while (padres[id].fila != -1) {
				nodo = padres[id];
				camino.push_back({ nodo.fila + 1, nodo.columna + 1 });
				id = nodo.id;
			}
		}
	}

	vector<pair<int, int>> getCamino() {
		return camino;
	}

	friend bool operator<(Astar::Nodo const n1, Astar::Nodo const n2);
};


bool operator<(Astar::Nodo const n1, Astar::Nodo const n2) {
	return n1.coste > n2.coste;
}


//	Muestra de forma gráfica el mapa construido por el usuario, con la casilla de inicio, meta y las casillas prohibidas
void dibujarMapa(int F, int C, pair<int,int>& ini, pair<int, int>& meta, vector<string>& mapa) {
	cout << "\n\nMapa: \n";
	cout << "    ";
	for (int i = 0; i < C; i++)
		cout << i + 1 << "  ";

	cout << "\n";

	for (int i = 0; i < F; i++) {
		cout << i + 1 << " | ";
		for (int j = 0; j < C; ++j) {
			if (mapa[i][j] == 'X')
				cout << "X  ";
			else if (meta.first == i && meta.second == j)
				cout << "M  ";
			else if (ini.first == i && ini.second == j)
				cout << "I  ";
			else if (mapa[i][j] == 'w')
				cout << "w  ";
			else cout << ".  ";
		}
		cout << "|\n";
	}

	cout << "\n";
}


//	Muetra por pantalla la secuencia de casillas que forman el camino óptimo
void escribirCaminoOptimo(vector<pair<int, int>>& camino) {
	if(camino.size() == 0)
		cout << "No se ha podido encontrar el camino optimo\n\n";
	else {
		std::cout << "\nCamino optimo: \n";

		for (int i = camino.size() - 1; i >= 0; --i) {
			std::cout << "(" << camino[i].first << ", " << camino[i].second << ")";

			if (i != 0)
				cout << " -> ";
		}

		cout << "\n\n";
	}
}


//	Construye un mapa marcando las casillas prohibidas
vector<string> construirMapa(int F, int C, vector<pair<int, int>>& prohibidas) {
	vector<string> mapa(F);

	//	Inicialización del tablero sin casillas prohibidas
	for (int i = 0; i < F; ++i)
		for (int j = 0; j < C; ++j)
			mapa[i].push_back('.');

	//	Colocación de las casillas prohibidas
	for (auto p : prohibidas)
		mapa[p.first][p.second] = 'X';

	return mapa;
}

void anadirWayPoints(vector<string>& mapa, vector<pair<int, int>>& way_points) {
	for (auto w : way_points)
		mapa[w.first][w.second] = 'w';
}


//	Pide al usuario los datos de un mapa (dimensiones, casilla de inicio, casilla de meta, casillas prohibidas), muestra el mapa construido y pide
//	su confirmación.
vector<string> confirmarMapa(pair<int, int>& inicio, pair<int, int>& meta) {
	int F, C;
	int f = -1, c = -1;
	vector<pair<int, int>> prohibidas;
	vector<string> mapa;
	char respuesta;
	char basura[80];

	do {

		do {
			cout << "\nIntroduce las dimensiones de la matriz <F C>: ";
			cin >> F >> C;
		} while (F == 0 || F < 0 || C == 0 || C < 0);

		do {
			cout << "Introduce la fila y la columna de inicio <fila columna>: ";
			cin >> f >> c;
		} while (f > F || f < 0 || c > C || c < 0);
		inicio = { f - 1, c - 1 };

		do {
			cout << "Introduce la fila y la columna de meta <fila columna>: ";
			cin >> f >> c;
		} while (f > F || f <= 0 || c > C || c <= 0);
		meta = { f - 1, c - 1 };


		cout << "Introduce las casillas prohibidas (0 0 para parar): ";
		do {
			cin >> f >> c;

			while ((f - 1 == inicio.first && c - 1 == inicio.second) || (f - 1 == meta.first && c - 1 == meta.second) || f < 0 || f > F || c < 0 || c > C) {
				if (f < 0 || f > F || c < 0 || c > C)
					cout << "Casilla erronea, introduzca una valida: ";
				else
					cout << "Las casillas prohibidas no pueden coincidir con la meta o el inicio: ";

				cin >> f >> c;
			}

			prohibidas.push_back({ f - 1, c - 1 });
		} while (f != 0);

		prohibidas.pop_back();	//Borra el par <-1, -1> 

		mapa = construirMapa(F, C, prohibidas);

		cout << "\n¿Es el siguiente mapa correcto?";
		dibujarMapa(F, C, inicio, meta, mapa);
		cout << "Respuesta (s/n): ";
		cin >> respuesta;

	} while (respuesta != 's');
	
	return mapa;
}


//	Calcula el camino óptimo pasando por los distintos way points introducidos por el usuario
vector<pair<int, int>> caminoConWayPoints(pair<int,int>& inicio, pair<int,int>& meta, vector<string>& mapa) {
	vector<pair<int, int>> camino;
	int f = -1, c = -1;
	int size;
	bool hay_camino = true;

	vector<pair<int, int>> way_points;
	vector<pair<int, int>> caminoAux;

	cout << "\nIntroduce los way points (0 0 para parar): ";
	do {
		cin >> f >> c;
		
		if (f != 0 && c != 0) {

			while ((f - 1 == inicio.first && c - 1 == inicio.second) || (f - 1 == meta.first && c - 1 == meta.second) || f < 0 || f > mapa.size() || c < 0 || c > mapa[0].size() || mapa[f - 1][c - 1] == 'X') {
				if (f < 0 || f > mapa.size() || c < 0 || c > mapa[0].size())
					cout << "Casilla erronea, introduzca una valida: ";
				else if (mapa[f - 1][c - 1] == 'X')
					cout << "No se puede establecer un way point en una casilla prohibida.\nPor favor, introduzca una casilla valida: ";
				else
					cout << "Los way points no pueden coincidir con la meta o el inicio. \nPor favor, introduzca una casilla valida: ";

				cin >> f >> c;

				if (f == 0 || c == 0)
					break;
			}
		}

		way_points.push_back({ f - 1, c - 1 });
	} while (f != 0);

	way_points.pop_back();	//Borra el par <-1, -1>
	anadirWayPoints(mapa, way_points);
	dibujarMapa(mapa.size(), mapa[0].size(), inicio, meta, mapa);
	camino.push_back({ inicio.first + 1, inicio.second + 1 });

	int i = 0;
	while (i < way_points.size() && hay_camino) {
		caminoAux = Astar(mapa, inicio, way_points[i]).getCamino();

		inicio = way_points[i];

		hay_camino = caminoAux.size() > 0;

		for (int j = caminoAux.size() - 2; j >= 0; --j)
			camino.push_back(caminoAux[j]);

		i++;
	}

	if (!hay_camino) {
		return vector<pair<int, int>>();
	}

	caminoAux = Astar(mapa, inicio, meta).getCamino();
	for (int j = caminoAux.size() - 2; j >= 0; --j)
		camino.push_back(caminoAux[j]);

	size = camino.size() - 1;
	for (int x = 0; x < size; x++, size--) {
		pair<int, int> temp = camino[x];
		camino[x] = camino[size];
		camino[size] = temp;
	}
	
	return camino;
}


//	Permite al usuario elegir entre cosntruir un mapa o salir del programa
int menu() {
	int op;

	cout << "===   ALGORITMO A*   ===\n";

	cout << "\tOpciones:\n";
	cout << "\t\t1.- Construir un mapa\n";
	cout << "\t\t0.- Salir\n";

	cout << "Seleccione una opcion: ";
	cin >> op;

	while (op < 0 || op > 1) {
		cout << "Por favor, seleccione una opcion valida (0, 1): ";
		cin >> op;
	}

	return op;
}


//	Permite elegir al usuario una de las funcionalidades del programa
int  menuFuncionalidades() {
	int op;

	cout << "\n\tFuncionalidades posibles:\n";
	cout << "\t\t1.- Calcular el camino optimo\n";
	cout << "\t\t2.- way points\n";
	cout << "\t\t0.- Salir\n";

	cout << "Seleccione una opcion: ";
	cin >> op;

	return op;
}


int main() {
	int option = -1;
	vector<string> mapa;	//Guarda el mapa con las casillas prohibidas
	pair<int, int> inicio, meta;	//Pares que representan la <fila, columna> del inicio y la meta
	vector<pair<int, int>> camino;	//Contiene los pares que representan las casillas del camino óptimo

	do {
		option = menu();

		if (option == 1) {
			mapa = confirmarMapa(inicio, meta);

			option = menuFuncionalidades();

			switch (option) {
			case 1:
				camino = Astar(mapa, inicio, meta).getCamino();
				escribirCaminoOptimo(camino);
				break;
			case 2:
				camino = caminoConWayPoints(inicio, meta, mapa);
				escribirCaminoOptimo(camino);
				break;
			case 0:
				break;
			}
		}

	} while (option != 0);

	return 0;
}

