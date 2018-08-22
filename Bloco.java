package Noobchain;
import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDate;

public class Bloco {
	private String hash;						//hash do bloco.
	private String hashAnterior;				//hash do bloco anterior.
	private String conteudo;					//conteudo do bloco.
	private LocalDate data;						//data de criacao do bloco.
	private long nanoTempo;						//tempo de criacao do bloco em nanosegundos.
	
	private static final int dificuldade = 4;	//dificuldade de criacao dos blocos ( numero de zeros encontrados ) 
	
	//Construtor privado para so ser possivel criar um bloco a partir da mineracao.
	private Bloco(String conteudo, String hashAnterior, String hash, long nanoTempo) {
		this.conteudo = conteudo;
		this.hashAnterior = hashAnterior;
		this.nanoTempo = nanoTempo;
		this.data = LocalDate.now();
		
		this.hash = hash;
	}
	
	//Metodo para minerar um novo bloco. ( DuuH. )
	public static Bloco minerarBloco(String conteudo, String hashAnterior) {
		String hash = "                                                                 ";
		long auxNano = 0;
		
		//Proof of work (prova de trabalho)  
		while(!hash.startsWith(gerarStringDificuldade())) {
			auxNano = System.nanoTime();
			hash = geradorHash(conteudo + hashAnterior + auxNano);
		}
		
		Bloco bloco = new Bloco(conteudo, hashAnterior, hash, auxNano);
		
		return bloco;
	}
	
	//Metodo utilizado para retornar o Hash de cada bloco ( Funcao Hash Sha-256 )
	public static String geradorHash(String entrada){		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
			byte[] hash = digest.digest(entrada.getBytes("UTF-8"));	        
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	//Concatena uma String com o mesmo numero de zeros da dificuldade.
	private static String gerarStringDificuldade() {
		String aux = "";
		for(int i = 0; i < dificuldade; i++) {
			aux += 0;
		}
		return aux;
	}
	
	public String getHash() {
		return this.hash;
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public String getConteudo() {
		return conteudo;
	}
	
	public void setConteudo(String conteudoAdulterado) {
		this.conteudo = conteudoAdulterado;
		this.hash = this.geradorHash(conteudoAdulterado + this.hashAnterior + this.nanoTempo);
	}
	
	//Altera o conteudo do bloco e refaz a prova de trabalho para q o novo hash seja valido.
	public void setConteudoRefazendoHash(String conteudoAdulterado) {
		this.conteudo = conteudoAdulterado;
		String hash = "                          ";
		long auxNano = 0;

		while(!hash.startsWith(this.gerarStringDificuldade())) {
			auxNano = System.nanoTime();
			hash = geradorHash(conteudo + hashAnterior + auxNano);
		}
		this.nanoTempo = auxNano;
		this.hash = hash;
	}
	
	//Altera o hash anterior do bloco e refaz a prova de trabalho para q o novo hash seja valido.
	public void setHashAnterior(String hashAnterior) throws IOException {
		this.hashAnterior = hashAnterior;
		String hash = "                          ";
		long auxNano = 0;

		while(!hash.startsWith(this.gerarStringDificuldade())) {
			auxNano = System.nanoTime();
			hash = geradorHash(conteudo + hashAnterior + auxNano);
		}
		this.nanoTempo = auxNano;
		this.hash = hash;
		
//		this.hashAnterior = hashAnterior;
//		
//		Bloco.minerarBloco(this.conteudo, hashAnterior);
	}
	
	public String getHashAnterior() {
		return this.hashAnterior;
	}

	public long getNanoTempo() {
		return nanoTempo;
	}

	@Override
	public String toString() {
		return "Bloco [hash=" + hash + ", hashAnterior=" + hashAnterior + ", conteudo=" + conteudo + ", data=" + data
				+ ", nanoTempo=" + nanoTempo + "]";
	}
	
	
}
