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
		Nodo() : fila(-1), columna(-1), coste(-1), padre(NULL) {};
		Nodo(int fila, int columna, double coste, Nodo *padre) :
			fila(fila), columna(columna), coste(coste), padre(padre) {};

		int fila;
		int columna;
		double coste;
		Nodo* padre;

		/*float coste() {
			return coste;
		}*/
	};

	int F, C;
	queue<Nodo> cerrada;
	priority_queue<Nodo> abierta;

	Nodo nodo_ini, meta;
	
	const vector<pair<int, int>> dirs = { { 0,1 },{ 0,-1 },{ 1,0 },{ -1,0 },{ 1,1 },{ -1,-1 },{ 1,-1 },{ -1,1 } };

	bool correct(int i, int j) {
		return 0 <= i && i < F && 0 <= j && j < C;
	}

public:

	Astar(vector<string> const& mapa, pair<int, int> const& inicio, pair<int, int> const& meta) : F(mapa.size()), C(mapa[0].size()),
		nodo_ini(inicio.first, inicio.second, sqrt(pow((inicio.first - meta.first), 2) + pow((inicio.second - meta.second), 2)), nullptr) {

		abierta.push(nodo_ini);
		bool meta_seleccionada = false;

		while (!abierta.empty() && !meta_seleccionada) {

			Nodo n = abierta.top(); abierta.pop();	//	FALLO: eligen al nodo con mayor coste, en vez de el de menor coste
			cerrada.push(n);

			meta_seleccionada = n.fila == meta.first && n.columna == meta.second;

			for (auto d : dirs) {
				int nf = n.fila + d.first;
				int nc = n.columna + d.second;

				if (correct(nf, nc) && mapa[nf][nc] != 'X' && (nf != n.fila || nc != n.columna)) {	//Comprobar que la nueva casilla no es la del padre

					double coste_nodo = sqrt(pow((nf - meta.first), 2) + pow((nc - meta.second), 2));

					Nodo padre = n;	//FALLO: los padres se sobreescriben
					Nodo nuevo = { nf, nc, coste_nodo + n.coste + 1, &padre };	//	Arreglar el coste de cada nodo

					abierta.push(nuevo);

				}
			}
		}

		if (meta_seleccionada) {
			Nodo n = cerrada.back();
			vector<pair<int, int>> camino;

			while (n.padre != NULL) {
				camino.push_back({ n.padre->fila, n.padre->columna });
				n = *n.padre;
			}

			std::cout << "Camino óptimo: \n";

			for (int i = camino.size() - 1; i >= 0; --i) {
				std::cout << "(" << camino[i].first << ", " << camino[i].second << ") -> ";
			}
		}
		else
			std::cout << "No se ha encontrado el camino óptimo\n";
	}

	friend bool operator<(Astar::Nodo const n1, Astar::Nodo const n2);
};

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

bool operator<(Astar::Nodo const n1, Astar::Nodo const n2) {
	return n1.coste < n2.coste;
}

int main() {
	int F, C;
	pair<int, int> inicio, meta;
	int f, c;
	vector<pair<int, int>> prohibidas;

	std::cout << "Introduce las dimensiones de la matriz (F x C): ";
	std::cin >> F >> C;

	cout << "Introduce la fila y la columna de inicio: ";
	cin >> f >> c;
	inicio = { f - 1, c - 1 };

	cout << "Introduce la fila y la columna de meta: ";
	cin >> f >> c;
	meta = { f - 1, c - 1 };

	cout << "Introduce las casillas prohibidas (0 0 para parar): ";
	do {
		cin >> f >> c;
		prohibidas.push_back({ f - 1, c - 1 });
	} while (f != 0);

	prohibidas.pop_back();
	vector<string> mapa = construirTablero(F, C, prohibidas);

	Astar(mapa, inicio, meta);

	return 0;
}

