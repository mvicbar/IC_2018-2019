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

		dibujarMapa();

		if (meta_seleccionada) {
			vector<pair<int, int>> camino;
			Nodo nodo = cerrada.back();
			int id = nodo.id;

			camino.push_back({ nodo.fila + 1, nodo.columna + 1 });

			while (padres[id].fila != -1) {
				nodo = padres[id];
				camino.push_back({ nodo.fila + 1, nodo.columna + 1 });
				id = nodo.id;
			}

			escribirCaminoOptimo(camino);
		}
		else
			std::cout << "No se ha podido encontrar el camino optimo\n";
	}

	void dibujarMapa() {
		cout << "\nMapa: \n";
		cout << "   ";
		for (int i = 0; i < F; i++)
			cout << i + 1 << "  ";

		cout << "\n";

		for (int i = 0; i < F; i++) {
			cout << i + 1 << " | ";
			for (int j = 0; j < C; ++j) {
				if (mapa[i][j] == 'X')
					cout << "X ";
				else if (nodo_meta.fila == i && nodo_meta.columna == j)
					cout << "M ";
				else if (nodo_ini.fila == i && nodo_ini.columna == j)
					cout << "I ";
				else cout << ". ";
			}
			cout << "|\n";
		}

		cout << "\n";
	}

	void escribirCaminoOptimo(vector<pair<int, int>>& camino) {
		std::cout << "Camino optimo: \n";

		for (int i = camino.size() - 1; i >= 0; --i) {
			std::cout << "(" << camino[i].first << ", " << camino[i].second << ")";

			if (i != 0)
				cout << " -> ";
		}

		cout << "\n\n";
	}

	friend bool operator<(Astar::Nodo const n1, Astar::Nodo const n2);
};


bool operator<(Astar::Nodo const n1, Astar::Nodo const n2) {
	return n1.coste > n2.coste;
}

vector<string> construirTablero(int F, int C, vector<pair<int, int>>& prohibidas) {
	vector<string> tablero(F);

	//	Inicialización del tablero sin casillas prohibidas
	for (int i = 0; i < F; ++i)
		for (int j = 0; j < C; ++j)
			tablero[i].push_back('.');

	//	Colocación de las casillas prohibidas
	for (auto p : prohibidas) 
		tablero[p.first][p.second] = 'X';

	return tablero;
}


int  menu() {
	int op;

	cout << "===   ALGORITMO A*   ===\n";
	cout << "\tFuncionalidades posibles:\n";
	cout << "\t\t1.- Calcular el camino optimo\n";
	cout << "\t\t2.- way points\n";
	cout << "\t\t0.- Salir\n";

	cout << "Seleccione una opcion: ";
	cin >> op;

	return op;
}


int main() {
	int option = -1;
	vector<pair<int, int>> prohibidas;	//Guarda las casillas prohibidas
	vector<string> mapa;	//Guarda el mapa con la casilla de inicio, la casilla de meta, y las casillas prohibidas
	pair<int, int> inicio, meta;	//Pares que representan la <fila, columna> de inicio y la meta
	int F, C;
	int f = -1, c = -1;

	do {
		option = menu();

		switch (option) {
		case 1:
			std::cout << "\nIntroduce las dimensiones de la matriz <F C>: ";
			std::cin >> F >> C;

			while (f > F || f < 0 || c > C || c < 0) {
				cout << "Introduce la fila y la columna de inicio <fila columna>: ";
				cin >> f >> c;
			}
			inicio = { f - 1, c - 1 };

			f = -1, c = -1;

			while (f > F || f <= 0 || c > C || c <= 0) {
				cout << "Introduce la fila y la columna de meta <fila columna>: ";
				cin >> f >> c;
			}
			meta = { f - 1, c - 1 };


			cout << "Introduce las casillas prohibidas (0 0 para parar): ";
			do {
				cin >> f >> c;

				while ((f - 1 == inicio.first && c - 1 == inicio.second) || (f - 1 == meta.first && c - 1 == meta.second) || f < 0 || f > F || c < 0 || c > C) {
					if(f < 0 || f > F || c < 0 || c > C)
						cout << "Casilla erronea, introduzca una valida: ";
					else
						cout << "Las casillas prohibidas no pueden coincidir con la meta o el inicio: ";

					cin >> f >> c;
				}

				prohibidas.push_back({ f - 1, c - 1 });
			} while (f != 0);

			prohibidas.pop_back();
			mapa = construirTablero(F, C, prohibidas);

			Astar(mapa, inicio, meta);
			break;
		case 2:
			cout << "Esta opcion aun no se encuentra disponible\n";
			break;
		case 0:
			break;
		}

	} while (option != 0);

	system("PAUSE");

	return 0;
}

