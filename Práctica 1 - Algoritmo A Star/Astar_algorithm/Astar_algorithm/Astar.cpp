#include <queue>
#include <iostream>
#include <fstream>
#include <vector>
#include <utility>

template <class T>
class Nodo {
public:
	Nodo(int fila, int columna, T coste, Nodo<T> *padre) :
		fila(fila), columna(columna), coste(coste), padre(padre) {};

	int fila;
	int columna;
	T coste;
	Nodo* padre;
};

bool operator<(Nodo<int> n1, Nodo<int> n2) {
	return n1.coste < n2.coste;
}

std::queue<Nodo<int>> cerrada;
std::priority_queue<Nodo<int>> abierta;

const std::vector<std::pair<int, int>> dirs = { {0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {-1,-1}, {1,-1}, {-1,1} };

void Astar(Nodo<int>& meta, int M, int N) {
	bool meta_seleccionada = false;
	char mapa[4][4] = { '.' };

	while (!abierta.empty() && !meta_seleccionada) {

		Nodo<int> n = abierta.top(); abierta.pop();
		cerrada.push(n);

		meta_seleccionada = n.fila == meta.fila && n.columna == meta.columna;

		for (std::pair<int, int> d : dirs) {
			int nf = n.fila + d.first;
			int nc = n.columna + d.second;

			if (nf >= 0 && nf < M && nc >= 0 && nc < N && mapa[nf][nc] != 'X' && (nf != n.fila || nc != n.columna)) {

				int coste_nodo = sqrt((nf - meta.fila) ^ 2 + (nc - meta.columna) ^ 2);

				Nodo<int> nuevo = { nf, nc, coste_nodo + n.coste + 1, &n };

				abierta.push(nuevo);

			}
		}
	}

	if (meta_seleccionada) {
		Nodo<int> n = cerrada.back();
		std::vector<std::pair<int, int>> camino;

		while (n.padre != NULL) {
			camino.push_back({ n.padre->fila, n.padre->columna});
			n = *n.padre;
		}

		std::cout << "Camino óptimo: \n";

		for (int i = camino.size()-1; i >= 0; --i) {
			std::cout << "(" << camino[i].first << ", " << camino[i].second << ") -> ";
		}
	}
	else
		std::cout << "No se ha encontrado el camino óptimo\n";

	
}


int main() {
	int N, M;
	int fila_ini, columna_ini;
	int fila_meta, columna_meta;

	/*std::cout << "Introduce las dimensiones de la matriz (M x N): \n";
	std::cin >> M >> N;*/

	std::cout << "Introduce la fila y la columna de inicio: \n";
	std::cin >> fila_ini >> columna_ini;

	std::cout << "Introduce la fila y la columna de meta: \n";
	std::cin >> fila_meta >> columna_meta;

	int coste_inicio = sqrt((fila_ini - fila_meta) ^ 2 + (columna_ini - columna_meta) ^ 2);

	Nodo<int> inicio = { fila_ini, columna_ini, coste_inicio, nullptr };
	Nodo<int> meta = { fila_meta, columna_meta, 0, nullptr };

	abierta.push(inicio);

	Astar(meta, 4, 4);

	return 0;
}