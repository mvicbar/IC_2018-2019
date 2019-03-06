#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>
#include <math.h>

using namespace std;

float infor(float& a, float& b) {
	if (a != 0 && b != 0)
		return -a * log2(a) - b * log2(b);
	else if (a == 0 && b != 0)
		return -b * log2(b);
	else if (b == 0 && a != 0)
		return -a * log2(a);
	else
		return 0;
}

string ID3(vector<vector<string>>& ejemplos, vector<string>& atributos) {
	bool todos_positivos = true, todos_negativos = true;

	if (ejemplos.size() == 0)
		return;
	else if (atributos.size() == 0)
		return;
	else {
		int i = 0;
		while (i < ejemplos.size() && todos_positivos) {
			todos_positivos = ejemplos[i][ejemplos[i].size() - 1] == "si";	
		}

		if (todos_positivos)
			return "SI";

		while (i < ejemplos.size() && todos_negativos) {
			todos_negativos = ejemplos[i][ejemplos[i].size() - 1] == "si";
		}

		if (todos_negativos)
			return "NO";
	}

}

int main() {
	string linea, palabra;
	char delim = ',';
	vector<string> atributos, lineaEjemplo;
	vector<vector<string>> ejemplos;

	ifstream in("AtributosJuego.txt");
	auto cinbuf = cin.rdbuf(in.rdbuf());

	cin >> linea;
	istringstream isstream(linea);

	while(getline(isstream, palabra, delim)){
        atributos.push_back(palabra);
    }

	ifstream in2("Juego.txt");
	cinbuf = cin.rdbuf(in2.rdbuf());

	while (cin >> linea) {
		istringstream isstream2(linea);

		while (getline(isstream2, palabra, delim)) {
			lineaEjemplo.push_back(palabra);

		}
		ejemplos.push_back(lineaEjemplo);
		lineaEjemplo.clear();
	}

	system("PAUSE");
	return 0;
}