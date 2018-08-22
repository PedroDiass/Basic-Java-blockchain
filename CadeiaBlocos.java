package Noobchain;
import java.io.IOException;
import java.util.ArrayList;

public class CadeiaBlocos {
	public static void main(String[] args) throws IOException {
		ArrayList<Bloco> cadeia = new ArrayList<>(6);
		
		// Minera 6 blocos e os adiciona a cadeia de blocos.
		cadeia.add(Bloco.minerarBloco("Ola, eu sou o bloco genesis", "0"));
		cadeia.add(Bloco.minerarBloco("Ola, eu sou o segundo bloco da cadeia.", cadeia.get(cadeia.size() - 1).getHash()));
		cadeia.add(Bloco.minerarBloco("E eu sou o terceiro bloco :)", cadeia.get(cadeia.size() - 1).getHash()));
		cadeia.add(Bloco.minerarBloco("Chega de blocos!!", cadeia.get(cadeia.size() - 1).getHash()));
		cadeia.add(Bloco.minerarBloco("so mais dois blocos plz!!", cadeia.get(cadeia.size() - 1).getHash()));
		cadeia.add(Bloco.minerarBloco("Pronto, agr acabou!!", cadeia.get(cadeia.size() - 1).getHash()));
		
		
		// Corrompe a cadeia de blocos e execulta alguns testes para verificar o estado da cadeia.
		CorromperCadeia(1, cadeia);
		System.out.println(cadeia.get(1).getConteudo());
		System.out.println(ValidarCadeia(cadeia));
	}
	
	// Metodo de validacao da cadeia de blocos, retorna true se a cadeia estiver valida e false se a cadeia estiver corrompida.
	static boolean ValidarCadeia(ArrayList<Bloco> cadeia) {
		
		// Verifica se todos os blocos da cadeia possuem o proof of work ( prova de trabalho ).
		for(int i = 0; i < cadeia.size(); i++) {
			if(!cadeia.get(i).getHash().substring(0, 4).equals("0000")) {
				return false;
			}
		}
		
		//Verifica se os hashs de cada bloco estao de acordo com as suas informacoes
		for(int i = 0; i < cadeia.size(); i++) {
			if(!cadeia.get(i).geradorHash(cadeia.get(i).getConteudo() + cadeia.get(i).getHashAnterior() + cadeia.get(i).getNanoTempo()).equals(cadeia.get(i).getHash())) {
				return false;
			}
		}
		
		// Verifica se a conexao entre os bocos da cadeia esta valida.
		for(int i = 1; i < cadeia.size(); i++) {
			if(!cadeia.get(i).getHashAnterior().equals(cadeia.get(i - 1).getHash())) {
				System.out.println("Erro de validacao, o bloco " + (i - 1) + " foi adulterado.");
				return false;
			}
		}
		
		return true;
	}
	
	// Tenta corromper a cadeia de blocos refazendo todas as provas de trabalho.
	static void CorromperCadeia(int index, ArrayList<Bloco> cadeia) throws IOException {
		cadeia.get(index).setConteudoRefazendoHash("Muahahaha, alterei sua cadeia de blocos");
		int aux = index;
		for(int i = ++index; i < cadeia.size(); i++) {
			cadeia.get(i).setHashAnterior(cadeia.get(aux++).getHash());
		}
	}
}
