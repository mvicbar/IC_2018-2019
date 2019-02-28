#include <queue>
#include <iostream>
#include <fstream>
#include <vector>
#include <utility>
#include <math.h>

#include "IndexPQ.h"

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

	Nodo nodo_ini, meta;	//Nodos inicio y meta
	
	//	Vector para generar las casillas adyacentes
	const vector<pair<int, int>> dirs = { { 0,1 },{ 0,-1 },{ 1,0 },{ -1,0 },{ 1,1 },{ -1,-1 },{ 1,-1 },{ -1,1 } };

	//	Función que indica si un par <fila, columna> se encuentra dentro del mapa
	bool correct(int i, int j) {
		return 0 <= i && i < F && 0 <= j && j < C;
	}

public:

	Astar(vector<string> const& mapa, pair<int, int> const& inicio, pair<int, int> const& meta) : F(mapa.size()), C(mapa[0].size()),
		nodo_ini(inicio.first, inicio.second, sqrt(pow((inicio.first - meta.first), 2) + pow((inicio.second - meta.second), 2))) {

		vector<int> padres(F*C, -1);	//Guarda en cada posición el padre del nodo cuyo id es el índice del vector

		nodo_ini.id = C * nodo_ini.fila + nodo_ini.columna;

		abierta.push(nodo_ini);
		bool meta_seleccionada = false;

		while (!abierta.empty() && !meta_seleccionada) {

			Nodo n = abierta.top(); abierta.pop();	
			cerrada.push(n);

			meta_seleccionada = n.fila == meta.first && n.columna == meta.second;

			for (auto d : dirs) {
				int nf = n.fila + d.first;
				int nc = n.columna + d.second;

				if (correct(nf, nc) && mapa[nf][nc] != 'X' && (nf != n.fila || nc != n.columna) && (nf*C + nc) != padres[n.id] && padres[nf*C + nc] == -1) {

					double coste_nodo = sqrt(pow((nf - meta.first), 2) + pow((nc - meta.second), 2));	// Distancia relativa a la meta

					if (d.first == 0 || d.second == 0)
						coste_nodo += 1;
					else coste_nodo += sqrt(2);

					Nodo nuevo = { nf, nc, coste_nodo };
					nuevo.id = C * nuevo.fila + nuevo.columna;

					if (padres[nuevo.id] == -1) {
						padres[nuevo.id] = n.id;	//Actualización del padre
						abierta.push(nuevo);
					}

				}
			}
		}

		if (meta_seleccionada) {
			int n = cerrada.back().id;
			vector<pair<int, int>> camino;
			camino.push_back({ -1, n });

			while (padres[n] != -1) {
				camino.push_back({ -1, padres[n] });
				n = padres[n];
			}

			cout << "Mapa: \n";
			cout << "   ";
			for (int i = 0; i < F; i++)
				cout << i + 1 << "  ";

			cout << "\n";

			for (int i = 0; i < F; i++) {
				cout << i + 1 << " | ";
				for (int j = 0; j < C; ++j) {
					if (mapa[i][j] == 'X')
						cout << "X ";
					else if (meta.first == i && meta.second == j)
						cout << "M ";
					else if (nodo_ini.fila == i && nodo_ini.columna == j)
						cout << "I ";
					else cout << ". ";
				}
				cout << "|\n";
			}

			std::cout << "Camino óptimo: \n";

			for (int i = camino.size() - 1; i >= 0; --i) {
				std::cout << "(" << camino[i].first << ", " << camino[i].second << ")";
				
				if (i != 0)
					cout << " -> ";
			}

			cout << "\n";
		}
		else
			std::cout << "No se ha encontrado el camino óptimo\n";
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

	system("PAUSE");

	return 0;
}

